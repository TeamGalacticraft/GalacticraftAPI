/*
 * Copyright (c) 2019-2021 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.api.celestialbody.satellite;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.galacticraft.api.atmosphere.AtmosphericInfo;
import dev.galacticraft.api.celestialbody.CelestialBodyDisplayInfo;
import dev.galacticraft.api.celestialbody.CelestialBodyType;
import dev.galacticraft.api.celestialbody.CelestialObjectType;
import dev.galacticraft.api.celestialbody.SolarSystemType;
import dev.galacticraft.api.internal.accessor.SatelliteAccessor;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.internal.util.NetworkUtil;
import dev.galacticraft.api.internal.world.gen.VoidChunkGenerator;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Satellite extends CelestialBodyType {
    public static final AtmosphericInfo DEFAULT_ATMOSPHERE = new AtmosphericInfo.Builder().build(); //todo get joe to do the atmosphere stuff
    public static final WorldGenerationProgressListener EMPTY_PROGRESS_LISTENER = new WorldGenerationProgressListener() {
        @Override
        public void start(ChunkPos spawnPos) {
        }

        @Override
        public void setChunkStatus(ChunkPos pos, @Nullable ChunkStatus status) {
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }
    };
    private final SatelliteOwnershipData ownershipData;
    private String name;
    private final DimensionType dimensionType;
    private final ChunkGenerator chunkGenerator;

    public Satellite(Identifier id, String translationKey, @NotNull RegistryKey<World> worldKey, @NotNull SolarSystemType parentSystem, @NotNull CelestialBodyType parent, CelestialBodyDisplayInfo displayInfo, float gravity, SatelliteOwnershipData ownershipData, String name, DimensionType type, ChunkGenerator chunkGenerator) {
        this(id, translationKey, worldKey, parentSystem, parent, displayInfo, gravity, DEFAULT_ATMOSPHERE, ownershipData, name, type, chunkGenerator);
    }

    public Satellite(Identifier id, String translationKey, @NotNull RegistryKey<World> worldKey, @NotNull SolarSystemType parentSystem, @NotNull CelestialBodyType parent, CelestialBodyDisplayInfo displayInfo, float gravity, AtmosphericInfo atmosphere, SatelliteOwnershipData ownershipData, String name, DimensionType dimensionType, ChunkGenerator chunkGenerator) {
        super(id, translationKey, CelestialObjectType.SATELLITE, worldKey, parentSystem, parent.getAccessWeight(), parent, displayInfo, gravity, atmosphere, null);
        this.ownershipData = ownershipData;
        this.name = name;
        this.dimensionType = dimensionType;
        this.chunkGenerator = chunkGenerator;
        assert this.getParent() != this : "Recursive parent";
    }

    public @NotNull RegistryKey<World> getWorld() {
        assert super.getWorld() != null : "Satellite World is null";
        return super.getWorld();
    }

    public @NotNull CelestialBodyType getParent() {
        assert super.getParent() != null : "Satellite Parent is null";
        return super.getParent();
    }

    public ChunkGenerator getChunkGenerator() {
        return chunkGenerator;
    }

    public @NotNull SatelliteOwnershipData getOwnershipData() {
        return this.ownershipData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Satellite create(MinecraftServer server, ServerPlayerEntity player, CelestialBodyType parent) {
        Identifier id = new Identifier(parent.getId() + "_" + player.getEntityName().toLowerCase(Locale.ROOT));
        DimensionType type = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1, false, false, false, false, false, 0, 256, 256, (seed, x, y, z, storage) -> server.getRegistryManager().get(Registry.BIOME_KEY).get(new Identifier(GalacticraftAPI.MOD_ID, "space")), new Identifier(GalacticraftAPI.MOD_ID, "infiniburn_space"), new Identifier(GalacticraftAPI.MOD_ID, "space_sky"), 0);
        DimensionOptions options = new DimensionOptions(() -> type, VoidChunkGenerator.INSTANCE);
        CelestialBodyDisplayInfo info = new CelestialBodyDisplayInfo.Builder().time(24000 * 2).distance(5.0f).scale(0.5f).texture(new Identifier(GalacticraftAPI.MOD_ID, "satellite")).build();
        SatelliteOwnershipData ownershipData = new SatelliteOwnershipData(player.getUuid(), player.getEntityName());
//        ServerTeams teams = ((MinecraftServerTeamsGetter) server).getSpaceRaceTeams();
//        if (teams.getTeam(player.getUuid()) != null) {
//            Team team = teams.getTeam(player.getUuid());
//            for (Map.Entry<UUID, Identifier> entry : team.players.entrySet()) {
//                if (team.roles.get(entry.getValue()).permissions.contains(Permission.ACCESS_SPACE_STATION)) {
//                    ownershipData.addTrusted(entry.getKey());
//                }
//            }
//        }
        return create(id, server, parent, info, type, options, ownershipData, player.getEntityName() + "'s Space Station");
    }

    public static Satellite create(Identifier id, MinecraftServer server, CelestialBodyType parent, CelestialBodyDisplayInfo info, DimensionType type, DimensionOptions options, SatelliteOwnershipData ownershipData, String name) {
        RegistryKey<World> key = RegistryKey.of(Registry.WORLD_KEY, id);
        RegistryKey<DimensionType> key2 = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);
        assert server.getWorld(key) == null : "World already registered";
        assert server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).get(key2) == null : "Dimension Type already registered";
        Registry.register(server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY), id, type);

        Satellite satellite = new Satellite(id, "ui.galacticraft-api.bodies.satellite", key, parent.getParentSystem(), parent, info, 0.0f, ownershipData, name, type, options.getChunkGenerator());
        ((SatelliteAccessor) server).addSatellite(satellite);
        GalacticraftAPI.LOGGER.debug("Attempting to create a world dynamically (" + id + ')');
        {
                DimensionType dimensionType3 = options.getDimensionType();
                ChunkGenerator chunkGenerator3 = options.getChunkGenerator();
                UnmodifiableLevelProperties unmodifiableLevelProperties = new UnmodifiableLevelProperties(server.getSaveProperties(), server.getSaveProperties().getMainWorldProperties());
                ServerWorld serverWorld2 = new ServerWorld(server, server.workerExecutor, server.session, unmodifiableLevelProperties, key, dimensionType3, EMPTY_PROGRESS_LISTENER, chunkGenerator3, server.getSaveProperties().getGeneratorOptions().isDebugWorld(), BiomeAccess.hashSeed(server.getSaveProperties().getGeneratorOptions().getSeed()), ImmutableList.of(), false);
                server.getWorld(World.OVERWORLD).getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(serverWorld2.getWorldBorder()));
                server.worlds.put(key, serverWorld2);
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            satellite.serializeSatellite(buf, server.getRegistryManager());
            ServerPlayNetworking.send(player, new Identifier(GalacticraftAPI.MOD_ID, "add_satellite"), buf);
        }
        return satellite;
    }

    @Override
    public PacketByteBuf serialize(PacketByteBuf buf, DynamicRegistryManager registryManager, boolean registeredToClient) {
        throw new UnsupportedOperationException("Use Satellite#serializeSatellite instead!");
    }

    public PacketByteBuf serializeSatellite(PacketByteBuf buf, DynamicRegistryManager registryManager) {
        super.serialize(buf, registryManager, false);
        this.getOwnershipData().writePacket(buf);
        buf.writeString(this.getName());
        return buf;
    }

    @Environment(EnvType.CLIENT)
    public static Satellite deserializeSatellite(DynamicRegistryManager registryManager, PacketByteBuf buf) {
        Identifier id = buf.readIdentifier();
        assert !NetworkUtil.unpackBoolean(buf.readByte(), 0);
        return new Satellite(id,
                buf.readString(),
                RegistryKey.of(Registry.WORLD_KEY, buf.readIdentifier()),
                SolarSystemType.getById(registryManager, buf.readIdentifier()),
                CelestialBodyType.getById(registryManager, buf.readIdentifier()),
                CelestialBodyDisplayInfo.fromPacket(buf),
                buf.readFloat(),
                AtmosphericInfo.readPacket(registryManager, buf),
                SatelliteOwnershipData.fromPacket(buf),
                buf.readString(),
                null,
                null
        );
    }

    public NbtCompound toTag(NbtCompound tag) {
        tag.putString("id", this.getId().toString());
        tag.putString("key", this.getTranslationKey());
        tag.putString("world", this.getWorld().getValue().toString());
        tag.putString("parent", this.getParent().getId().toString());
        tag.putString("system", this.getParentSystem().getId().toString());
        tag.put("display", this.getDisplayInfo().toTag(new NbtCompound()));
        tag.put("ownership", this.getOwnershipData().toTag(new NbtCompound()));
        tag.putFloat("gravity", this.getGravity());
        tag.putString("name", this.getName());
        tag.put("dim_type", DimensionType.CODEC.encode(dimensionType, NbtOps.INSTANCE, new NbtCompound()).get().orThrow());
        tag.put("chunk_generator", ChunkGenerator.CODEC.encode(this.chunkGenerator, NbtOps.INSTANCE, new NbtCompound()).get().orThrow());
        return tag;
    }

    public static Satellite fromTag(MinecraftServer server, NbtCompound tag) {
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, new RegistryOps.EntryLoader.Impl(), server.getRegistryManager());

        return new Satellite(new Identifier(tag.getString("id")), tag.getString("key"),
                RegistryKey.of(Registry.WORLD_KEY, new Identifier(tag.getString("world"))),
                SolarSystemType.getById(server.getRegistryManager(), new Identifier(tag.getString("system"))),
                CelestialBodyType.getById(server.getRegistryManager(), new Identifier(tag.getString("parent"))),
                CelestialBodyDisplayInfo.fromTag(tag.getCompound("display")),
                tag.getFloat("gravity"),
                SatelliteOwnershipData.fromTag(tag.getCompound("ownership")),
                tag.getString("name"),
                DimensionType.CODEC.decode(ops, tag.get("dim_type")).get().orThrow().getFirst(),
                ChunkGenerator.CODEC.decode(ops, tag.get("chunk_generator")).get().orThrow().getFirst()
        );
    }

    public DimensionType getDimensionType() {
        return dimensionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Satellite satellite = (Satellite) o;
        return satellite.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
