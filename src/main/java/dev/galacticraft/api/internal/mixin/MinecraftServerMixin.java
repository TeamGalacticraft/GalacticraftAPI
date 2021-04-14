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

package dev.galacticraft.api.internal.mixin;

import com.google.common.collect.ImmutableList;
import dev.galacticraft.api.celestialbodies.satellite.Satellite;
import dev.galacticraft.api.internal.accessor.SatelliteAccessor;
import dev.galacticraft.api.internal.data.MinecraftServerTeamsGetter;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.teams.ServerTeams;
import dev.galacticraft.api.teams.TeamsState;
import dev.galacticraft.api.teams.TeamsSync;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
@Implements(@Interface(iface = MinecraftServerTeamsGetter.class, prefix = "gcr$", remap = Interface.Remap.NONE))
public abstract class MinecraftServerMixin implements SatelliteAccessor {
    @Shadow @Final public LevelStorage.Session session;

    @Shadow public abstract SaveProperties getSaveProperties();

    @Shadow @Final public Executor workerExecutor;

    @Shadow @Nullable public abstract ServerWorld getWorld(RegistryKey<World> key);

    @Shadow @Final public Map<RegistryKey<World>, ServerWorld> worlds;

    @Shadow public abstract boolean save(boolean suppressLogs, boolean bl, boolean bl2);

    @Unique
    private final ServerTeams teams = new ServerTeams((MinecraftServer)(Object)this);
    @Unique
    private final List<Satellite> satellites = new ArrayList<>();

    @Inject(
            method = "initScoreboard(Lnet/minecraft/world/PersistentStateManager;)V",
            at = @At("INVOKE")
    )
    private void initScoreboard(PersistentStateManager manager, CallbackInfo info) {
        TeamsState state = manager.getOrCreate(TeamsState::new, "gcr-teams");
        state.setTeams(this.teams);
        this.teams.addListener(new TeamsSync(state));
    }

    public ServerTeams gcr$getSpaceRaceTeams() {
        return this.teams;
    }

    @Override
    public @Unmodifiable List<Satellite> getSatellites() {
        return ImmutableList.copyOf(this.satellites);
    }

    @Override
    public void addSatellite(Satellite satellite) {
        this.satellites.add(satellite);
    }

    @Override
    public void removeSatellite(Identifier id) {
        int index = -1;
        for (int i = 0; i < this.satellites.size(); i++) {
            if (this.satellites.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        assert index != -1;
        this.satellites.remove(index);
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void save_gcr(boolean suppressLogs, boolean bl, boolean bl2, CallbackInfoReturnable<Boolean> cir) {
        Path path = this.session.getDirectory(WorldSavePath.ROOT);
        ListTag tag = new ListTag();
        for (Satellite satellite : this.satellites) {
            tag.add(satellite.toTag(new CompoundTag()));
        }
        CompoundTag compound = new CompoundTag();
        compound.put("satellites", tag);
        try {
            NbtIo.writeCompressed(compound, new File(path.toFile(), "satellites.dat"));
        } catch (Throwable exception) {
            GalacticraftAPI.LOGGER.fatal("Failed to write satellite data!", exception);
        }
    }

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setupServer()Z", shift = At.Shift.AFTER))
    private void load_gcr(CallbackInfo ci) {
        Path path = this.session.getDirectory(WorldSavePath.ROOT);
        if (new File(path.toFile(), "satellites.dat").exists()) {
            try {
                ListTag tag = NbtIo.readCompressed(new File(path.toFile(), "satellites.dat")).getList("satellites", NbtType.COMPOUND);
                assert tag != null : "Listtag was null";
                for (Tag compound : tag) {
                    assert compound != null : "Compound in list was null?!";
                    this.satellites.add(Satellite.fromTag(((MinecraftServer) (Object) this), ((CompoundTag) compound)));
                }

                for (Satellite satellite : this.satellites) {
                    DimensionType dimensionType3 = satellite.getDimensionType();
                    ChunkGenerator chunkGenerator3 = satellite.getChunkGenerator();
                    UnmodifiableLevelProperties unmodifiableLevelProperties = new UnmodifiableLevelProperties(getSaveProperties(), getSaveProperties().getMainWorldProperties());
                    ServerWorld serverWorld2 = new ServerWorld((MinecraftServer)(Object)this, workerExecutor, session, unmodifiableLevelProperties, satellite.getWorld(), dimensionType3, Satellite.EMPTY_PROGRESS_LISTENER, chunkGenerator3, getSaveProperties().getGeneratorOptions().isDebugWorld(), BiomeAccess.hashSeed(getSaveProperties().getGeneratorOptions().getSeed()), ImmutableList.of(), false);
                    getWorld(World.OVERWORLD).getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(serverWorld2.getWorldBorder()));
                    worlds.put(satellite.getWorld(), serverWorld2);
                }
            } catch (Throwable exception) {
                GalacticraftAPI.LOGGER.fatal("Failed to read satellite data!", exception);
            }
        }
    }
}
