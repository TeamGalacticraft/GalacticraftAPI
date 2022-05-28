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

package dev.galacticraft.impl.internal.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.galacticraft.api.accessor.SatelliteAccessor;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.universe.celestialbody.type.SatelliteType;
import dev.galacticraft.impl.universe.position.config.SatelliteConfig;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements SatelliteAccessor {
    @Unique private final Map<Identifier, CelestialBody<SatelliteConfig, SatelliteType>> satellites = new HashMap<>();

    @Shadow @Final protected LevelStorage.Session session;

    @Shadow public abstract DynamicRegistryManager.Immutable getRegistryManager();

    @Override
    public @Unmodifiable Map<Identifier, CelestialBody<SatelliteConfig, SatelliteType>> getSatellites() {
        return ImmutableMap.copyOf(this.satellites);
    }

    @Override
    public void addSatellite(Identifier id, CelestialBody<SatelliteConfig, SatelliteType> satellite) {
        this.satellites.put(id, satellite);
    }

    @Override
    public void removeSatellite(Identifier id) {
        this.satellites.remove(id);
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void galacticraft_saveSatellites(boolean suppressLogs, boolean bl, boolean bl2, CallbackInfoReturnable<Boolean> cir) {
        Path path = this.session.getDirectory(WorldSavePath.ROOT);
        NbtList nbt = new NbtList();
        for (Map.Entry<Identifier, CelestialBody<SatelliteConfig, SatelliteType>> entry : this.satellites.entrySet()) {
            NbtCompound compound = (NbtCompound) SatelliteConfig.CODEC.encode(entry.getValue().config(), NbtOps.INSTANCE, new NbtCompound()).get().orThrow();
            compound.putString("id", entry.getKey().toString());
            nbt.add(compound);
        }
        NbtCompound compound = new NbtCompound();
        compound.put("satellites", nbt);
        try {
            NbtIo.writeCompressed(compound, new File(path.toFile(), "satellites.dat"));
        } catch (Throwable exception) {
            Constant.LOGGER.fatal("Failed to write satellite data!", exception);
        }
    }

    @Inject(method = "loadWorld", at = @At(value = "HEAD"))
    private void galacticraft_loadSatellites(CallbackInfo ci) {
        File worldFile = this.session.getDirectory(WorldSavePath.ROOT).toFile();
        if (new File(worldFile, "satellites.dat").exists()) {
            try {
                NbtList nbt = NbtIo.readCompressed(new File(worldFile, "satellites.dat")).getList("satellites", NbtType.COMPOUND);
                assert nbt != null : "NBT list was null";
                for (NbtElement compound : nbt) {
                    assert compound instanceof NbtCompound : "Not a compound?!";
                    this.satellites.put(new Identifier(((NbtCompound) compound).getString("id")), new CelestialBody<>(SatelliteType.INSTANCE, SatelliteConfig.CODEC.decode(RegistryOps.of(NbtOps.INSTANCE, this.getRegistryManager()), compound).get().orThrow().getFirst()));
                }
            } catch (Throwable exception) {
                throw new RuntimeException("Failed to read satellite data!", exception);
            }
        }
    }
}
