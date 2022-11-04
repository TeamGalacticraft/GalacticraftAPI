/*
 * Copyright (c) 2019-2022 Team Galacticraft
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

import com.mojang.serialization.Codec;
import dev.galacticraft.api.accessor.SatelliteAccessor;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.Satellite;
import dev.galacticraft.api.satellite.SatelliteOwnershipData;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import dev.galacticraft.api.universe.celestialbody.satellite.Orbitable;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.dyndims.api.DynamicDimensionRegistry;
import dev.galacticraft.dyndims.api.PlayerRemover;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.world.gen.SatelliteChunkGenerator;
import dev.galacticraft.impl.internal.world.gen.biome.GcApiBiomes;
import dev.galacticraft.impl.universe.display.config.IconCelestialDisplayConfig;
import dev.galacticraft.impl.universe.display.type.IconCelestialDisplayType;
import dev.galacticraft.impl.universe.position.config.OrbitalCelestialPositionConfig;
import dev.galacticraft.impl.universe.position.config.SpaceStationConfig;
import dev.galacticraft.impl.universe.position.type.OrbitalCelestialPositionType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalLong;

public class SpaceStationType extends CelestialBodyType<SpaceStationConfig> implements Satellite<SpaceStationConfig>, Landable<SpaceStationConfig> {
    public static final SpaceStationType INSTANCE = new SpaceStationType(SpaceStationConfig.CODEC);
    private static final PlayerRemover PLAYER_REMOVER = (server, player) -> {
        CelestialBody<CelestialBodyConfig, ? extends CelestialBodyType<CelestialBodyConfig>> spaceStation = CelestialBody.getByDimension(server.registryAccess(), player.getLevel().dimension()).orElse(null);
        if (spaceStation != null) {
            CelestialBody<?, ?> parent = spaceStation.parent(server.registryAccess());
            if (parent != null) {
                if (parent.type() instanceof Landable landable) {
                    ServerLevel level = server.getLevel(landable.world(parent.config()));
                    player.teleportTo(level, player.getX(), level.getMaxBuildHeight() * 2, player.getZ(), player.getYRot(), player.getXRot());
                    player.setDeltaMovement((level.random.nextDouble() - 0.5) * 10.0, level.random.nextDouble() * 12.5, (level.random.nextDouble() - 0.5) * 10.0);
                    return;
                }
            }
        }
        ServerLevel overworld = server.overworld();
        player.teleportTo(overworld, 0, 1000, 0, player.getYRot(), player.getXRot());
        player.setDeltaMovement((overworld.random.nextDouble() - 0.5) * 10.0, overworld.random.nextDouble() * 20.0, (overworld.random.nextDouble() - 0.5) * 10.0);
    };
    private static final GasComposition EMPTY_GAS_COMPOSITION = new GasComposition.Builder().build();
    private static final Component NAME = Component.translatable("ui.galacticraft-api.space_station.name");
    private static final Component DESCRIPTION = Component.translatable("ui.galacticraft-api.space_station.description");

    protected SpaceStationType(Codec<SpaceStationConfig> codec) {
        super(codec);
    }

    @ApiStatus.Experimental
    public static CelestialBody<SpaceStationConfig, SpaceStationType> registerSpaceStation(@NotNull MinecraftServer server, @NotNull ServerPlayer player, @NotNull CelestialBody<?, ?> parent, @NotNull StructureTemplate structure) {
        ResourceLocation identifier = Objects.requireNonNull(server.registryAccess().registryOrThrow(AddonRegistry.CELESTIAL_BODY_KEY).getKey(parent));
        ResourceLocation id = new ResourceLocation(identifier.getNamespace(), "sat_" + identifier.getPath() + "_" + player.getGameProfile().getName().toLowerCase(Locale.ROOT));
        DimensionType type = new DimensionType(OptionalLong.empty(), true, false, false, true, 1, false, false, 0, 256, 256, TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Constant.MOD_ID, "infiniburn_space")), new ResourceLocation(Constant.MOD_ID, "space_sky"), 1, new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0));
        SatelliteOwnershipData ownershipData = SatelliteOwnershipData.create(player.getUUID(), player.getScoreboardName(), new LinkedList<>(), false);
        CelestialPosition<?, ?> position = new CelestialPosition<>(OrbitalCelestialPositionType.INSTANCE, new OrbitalCelestialPositionConfig(1550, 10.0f, 0.0F, false));
        CelestialDisplay<?, ?> display = new CelestialDisplay<>(IconCelestialDisplayType.INSTANCE, new IconCelestialDisplayConfig(new ResourceLocation(Constant.MOD_ID, "satellite"), 0, 0, 16, 16, 1));
        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, id);
        ResourceKey<DimensionType> key2 = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, id);
        assert server.getLevel(key) == null : "Level already registered?!";
        assert server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).get(key2) == null : "Dimension Type already registered?!";
        return create(id, server, parent, position, display, new SatelliteChunkGenerator(server.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), Holder.direct(GcApiBiomes.SPACE), structure), type, ownershipData, player.getGameProfile().getName() + "'s Space Station");
    }

    @ApiStatus.Experimental
    public static CelestialBody<SpaceStationConfig, SpaceStationType> create(ResourceLocation id, @NotNull MinecraftServer server, CelestialBody<?, ?> parent, CelestialPosition<?, ?> position, CelestialDisplay<?, ?> display,
                                                                             ChunkGenerator chunkGenerator, DimensionType type, SatelliteOwnershipData ownershipData, String name) {
        if (!(parent.type() instanceof Orbitable)) {
            throw new IllegalArgumentException("Parent must be orbitable!");
        }

        Constant.LOGGER.debug("Attempting to create a level dynamically ({})", id);
        if (!((DynamicDimensionRegistry) server).addDynamicDimension(id, chunkGenerator, type)) {
            throw new RuntimeException("Failed to create dynamic level!");
        }

        SpaceStationConfig config = new SpaceStationConfig(ResourceKey.create(AddonRegistry.CELESTIAL_BODY_KEY, Objects.requireNonNull(server.registryAccess().registryOrThrow(AddonRegistry.CELESTIAL_BODY_KEY).getKey(parent))), parent.galaxy(), position, display, ownershipData, ResourceKey.create(Registry.DIMENSION_REGISTRY, id), EMPTY_GAS_COMPOSITION, 0.0f, parent.type() instanceof Landable ? ((Landable) parent.type()).accessWeight(parent.config()) : 1);
        config.customName(Component.translatable(name));
        CelestialBody<SpaceStationConfig, SpaceStationType> satellite = INSTANCE.configure(config);
        ((SatelliteAccessor) server).addSatellite(id, satellite);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            CompoundTag compound = (CompoundTag) SpaceStationConfig.CODEC.encode(satellite.config(), NbtOps.INSTANCE, new CompoundTag()).get().orThrow();
            ServerPlayNetworking.send(player, new ResourceLocation(Constant.MOD_ID, "add_satellite"), new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(id).writeNbt(compound));
        }
        return satellite;
    }

    @ApiStatus.Experimental
    public static boolean removeSatellite(@NotNull MinecraftServer server, ResourceLocation id) {
        if (((DynamicDimensionRegistry) server).removeDynamicDimension(id, PLAYER_REMOVER)) {
            ((SatelliteAccessor) server).removeSatellite(id);

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(player, new ResourceLocation(Constant.MOD_ID, "remove_satellite"), new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(id));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public @NotNull Component name(SpaceStationConfig config) {
        return NAME;
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(@NotNull Registry<CelestialBody<?, ?>> registry, @NotNull SpaceStationConfig config) {
        return registry.get(config.parent());
    }

    @Override
    public @NotNull ResourceKey<Galaxy> galaxy(@NotNull SpaceStationConfig config) {
        return config.galaxy();
    }

    @Override
    public @NotNull Component description(SpaceStationConfig config) {
        return DESCRIPTION;
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(@NotNull SpaceStationConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(@NotNull SpaceStationConfig config) {
        return config.display();
    }

    @Override
    public SatelliteOwnershipData ownershipData(@NotNull SpaceStationConfig config) {
        return config.ownershipData();
    }

    @Override
    public void setCustomName(@NotNull Component text, @NotNull SpaceStationConfig config) {
        config.customName(text);
    }

    @Override
    public @NotNull Component getCustomName(@NotNull SpaceStationConfig config) {
        return config.customName();
    }

    @Override
    public @NotNull ResourceKey<Level> world(@NotNull SpaceStationConfig config) {
        return config.world();
    }

    @Override
    public @NotNull GasComposition atmosphere(@NotNull SpaceStationConfig config) {
        return config.atmosphere();
    }

    @Override
    public float gravity(@NotNull SpaceStationConfig config) {
        return config.gravity();
    }

    @Override
    public int accessWeight(@NotNull SpaceStationConfig config) {
        return config.accessWeight();
    }

    @Override
    public CelestialBody<SpaceStationConfig, SpaceStationType> configure(SpaceStationConfig config) {
        return new CelestialBody<>(this, config);
    }

    @Override
    public int temperature(RegistryAccess access, long time, SpaceStationConfig config) {
        return time % 24000 < 12000 ? 121 : -157; //todo: gradual temperature change
    }
}
