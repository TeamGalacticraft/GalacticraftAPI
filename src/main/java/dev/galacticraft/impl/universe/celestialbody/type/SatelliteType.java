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

package dev.galacticraft.impl.universe.celestialbody.type;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import dev.galacticraft.api.accessor.SatelliteAccessor;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.Satellite;
import dev.galacticraft.api.satellite.SatelliteOwnershipData;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.mixin.MinecraftServerAccessor;
import dev.galacticraft.impl.internal.world.gen.SatelliteChunkGenerator;
import dev.galacticraft.impl.internal.world.gen.biome.GcApiBiomes;
import dev.galacticraft.impl.universe.display.config.IconCelestialDisplayConfig;
import dev.galacticraft.impl.universe.display.type.IconCelestialDisplayType;
import dev.galacticraft.impl.universe.position.config.OrbitalCelestialPositionConfig;
import dev.galacticraft.impl.universe.position.config.SatelliteConfig;
import dev.galacticraft.impl.universe.position.type.OrbitalCelestialPositionType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Locale;
import java.util.OptionalLong;

public class SatelliteType extends CelestialBodyType<SatelliteConfig> implements Satellite<SatelliteConfig>, Landable<SatelliteConfig> {
    public static final SatelliteType INSTANCE = new SatelliteType(SatelliteConfig.CODEC);
    private static final GasComposition EMPTY_GAS_COMPOSITION = new GasComposition.Builder().build();
    private static final TranslatableText NAME = new TranslatableText("ui.galacticraft-api.satellite.name");
    private static final TranslatableText DESCRIPTION = new TranslatableText("ui.galacticraft-api.satellite.description");
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

    protected SatelliteType(Codec<SatelliteConfig> codec) {
        super(codec);
    }

    @ApiStatus.Internal
    public static CelestialBody<SatelliteConfig, SatelliteType> registerSatellite(@NotNull MinecraftServer server, @NotNull ServerPlayerEntity player, @NotNull CelestialBody<?, ?> parent, Structure structure) {
        Identifier id = new Identifier(server.getRegistryManager().get(AddonRegistry.CELESTIAL_BODY_KEY).getId(parent).toString() + "_" + player.getEntityName().toLowerCase(Locale.ROOT));
        DimensionType type = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1, false, false, false, false, false, 0, 256, 256, (seed, x, y, z, storage) -> server.getRegistryManager().get(Registry.BIOME_KEY).get(new Identifier(Constant.MOD_ID, "space")), new Identifier(Constant.MOD_ID, "infiniburn_space"), new Identifier(Constant.MOD_ID, "space_sky"), 0);
        DimensionOptions options = new DimensionOptions(() -> type, new SatelliteChunkGenerator(GcApiBiomes.SPACE, structure));
        SatelliteOwnershipData ownershipData = new SatelliteOwnershipData(player.getUuid(), player.getEntityName(), new LinkedList<>(), false);
        CelestialPosition<?, ?> position = new CelestialPosition<>(OrbitalCelestialPositionType.INSTANCE, new OrbitalCelestialPositionConfig(1550, 10.0f, 0.0F, false));
        CelestialDisplay<?, ?> display = new CelestialDisplay<>(IconCelestialDisplayType.INSTANCE, new IconCelestialDisplayConfig(new Identifier(Constant.MOD_ID, "satellite"), 0, 0, 16, 16, 1));
        RegistryKey<World> key = RegistryKey.of(Registry.WORLD_KEY, id);
        RegistryKey<DimensionType> key2 = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);
        assert server.getWorld(key) == null : "World already registered?!";
        assert server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).get(key2) == null : "Dimension Type already registered?!";
        Registry.register(server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY), id, type);
        return create(id, server, parent, position, display, options, ownershipData, player.getGameProfile().getName() + "'s Space Station");
    }

    @ApiStatus.Internal
    public static CelestialBody<SatelliteConfig, SatelliteType> create(Identifier id, MinecraftServer server, CelestialBody<?, ?> parent, CelestialPosition<?, ?> position, CelestialDisplay<?, ?> display,
                                                                       DimensionOptions options, SatelliteOwnershipData ownershipData, String name) {
        SatelliteConfig config = new SatelliteConfig(RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, server.getRegistryManager().get(AddonRegistry.CELESTIAL_BODY_KEY).getId(parent)), parent.galaxy(), position, display, ownershipData, RegistryKey.of(Registry.WORLD_KEY, id), EMPTY_GAS_COMPOSITION, 0.0f, parent.type() instanceof Landable ? ((Landable) parent.type()).accessWeight(parent.config()) : 1, options);
        config.customName(new TranslatableText(name));
        CelestialBody<SatelliteConfig, SatelliteType> satellite = INSTANCE.configure(config);
        ((SatelliteAccessor) server).addSatellite(id, satellite);
        Constant.LOGGER.debug("Attempting to create a world dynamically ({})", id);

        DimensionType dimensionType3 = options.getDimensionType();
        ChunkGenerator chunkGenerator3 = options.getChunkGenerator();
        UnmodifiableLevelProperties unmodifiableLevelProperties = new UnmodifiableLevelProperties(server.getSaveProperties(), server.getSaveProperties().getMainWorldProperties());
        ServerWorld serverWorld2 = new ServerWorld(server, ((MinecraftServerAccessor) server).getWorkerExecutor(), ((MinecraftServerAccessor) server).getSession(), unmodifiableLevelProperties, RegistryKey.of(Registry.WORLD_KEY, id), dimensionType3, EMPTY_PROGRESS_LISTENER, chunkGenerator3, server.getSaveProperties().getGeneratorOptions().isDebugWorld(), BiomeAccess.hashSeed(server.getSaveProperties().getGeneratorOptions().getSeed()), ImmutableList.of(), false);
        server.getWorld(World.OVERWORLD).getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(serverWorld2.getWorldBorder()));
        ((MinecraftServerAccessor) server).getWorlds().put(RegistryKey.of(Registry.WORLD_KEY, id), serverWorld2);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NbtCompound compound = (NbtCompound) SatelliteConfig.CODEC.encode(satellite.config(), NbtOps.INSTANCE, new NbtCompound()).get().orThrow();
            ServerPlayNetworking.send(player, new Identifier(Constant.MOD_ID, "add_satellite"), new PacketByteBuf(Unpooled.buffer()).writeIdentifier(id).writeNbt(compound));
        }
        return satellite;
    }

    @Override
    public @NotNull TranslatableText name(SatelliteConfig config) {
        return NAME;
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(Registry<CelestialBody<?, ?>> registry, SatelliteConfig config) {
        return registry.get(config.parent());
    }

    @Override
    public @NotNull RegistryKey<Galaxy> galaxy(SatelliteConfig config) {
        return config.galaxy();
    }

    @Override
    public @NotNull TranslatableText description(SatelliteConfig config) {
        return DESCRIPTION;
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(SatelliteConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(SatelliteConfig config) {
        return config.display();
    }

    @Override
    public SatelliteOwnershipData ownershipData(SatelliteConfig config) {
        return config.ownershipData();
    }

    @Override
    public void setCustomName(@NotNull Text text, SatelliteConfig config) {
        config.customName(text);
    }

    @Override
    public @NotNull Text getCustomName(SatelliteConfig config) {
        return config.customName();
    }

    @Override
    public @NotNull RegistryKey<World> world(SatelliteConfig config) {
        return config.world();
    }

    @Override
    public @NotNull GasComposition atmosphere(SatelliteConfig config) {
        return config.atmosphere();
    }

    @Override
    public float gravity(SatelliteConfig config) {
        return config.gravity();
    }

    @Override
    public int accessWeight(SatelliteConfig config) {
        return config.accessWeight();
    }

    @Override
    public int dayTemperature(SatelliteConfig config) {
        return 121;
    }

    @Override
    public int nightTemperature(SatelliteConfig config) {
        return -157;
    }

    @Override
    public CelestialBody<SatelliteConfig, SatelliteType> configure(SatelliteConfig config) {
        return new CelestialBody<>(this, config);
    }
}
