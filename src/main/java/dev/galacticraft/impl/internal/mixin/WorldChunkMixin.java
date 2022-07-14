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

import dev.galacticraft.api.accessor.ChunkOxygenAccessor;
import dev.galacticraft.api.accessor.ChunkSectionOxygenAccessor;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenAccessorInternal;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenSyncer;
import dev.galacticraft.impl.internal.accessor.ChunkSectionOxygenAccessorInternal;
import dev.galacticraft.impl.internal.accessor.WorldOxygenAccessorInternal;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelChunkTicks;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(LevelChunk.class)
public abstract class WorldChunkMixin extends ChunkAccess implements ChunkOxygenAccessor, ChunkOxygenSyncer, ChunkOxygenAccessorInternal {
    @Shadow @Final Level level;
    private @Unique boolean /*@NotNull*/ [] sectionDirty;
    private @Unique boolean defaultBreathable = false;
    private @Unique boolean dirty = false;

    private WorldChunkMixin(ChunkPos pos, UpgradeData upgradeData, LevelHeightAccessor heightLimitView, Registry<Biome> biome, long inhabitedTime, @Nullable LevelChunkSection[] sectionArrayInitializer, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biome, inhabitedTime, sectionArrayInitializer, blendingData);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/ticks/LevelChunkTicks;Lnet/minecraft/world/ticks/LevelChunkTicks;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;Lnet/minecraft/world/level/levelgen/blending/BlendingData;)V", at = @At("RETURN"))
    private void galacticraft_init(@NotNull Level world, ChunkPos pos, UpgradeData upgradeData, LevelChunkTicks<Block> blockTickScheduler, LevelChunkTicks<Fluid> fluidTickScheduler, long inhabitedTime, LevelChunkSection[] sectionArrayInitializer, LevelChunk.PostLoadProcessor entityLoader, BlendingData blendingData, CallbackInfo ci) {
        this.sectionDirty = new boolean[world.getSectionsCount()];
        this.defaultBreathable = ((WorldOxygenAccessorInternal) world).getDefaultBreathable();
        for (LevelChunkSection section : this.getSections()) {
            assert section != null;
            ((ChunkSectionOxygenAccessorInternal) section).setDefaultBreathable(this.defaultBreathable);
        }
    }


    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (this.isOutsideBuildHeight(y)) return this.defaultBreathable;
        LevelChunkSection section = this.getSection(this.getSectionIndex(y));
        if (!section.hasOnlyAir()) {
            return ((ChunkSectionOxygenAccessor) section).isBreathable(x & 15, y & 15, z & 15);
        }
        return this.defaultBreathable;
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (this.isOutsideBuildHeight(y)) return;
        LevelChunkSection section = this.getSection(this.getSectionIndex(y));
        assert section != null;
        ChunkSectionOxygenAccessor accessor = ((ChunkSectionOxygenAccessor) section);
        if (value != accessor.isBreathable(x & 15, y & 15, z & 15)) {
            if (!this.level.isClientSide) {
                this.unsaved = true;
                this.dirty = true;
                sectionDirty[this.getSectionIndex(y)] = true;
            }
            accessor.setBreathable(x & 15, y & 15, z & 15, value);
        }
    }

    @Override
    public @NotNull List<ClientboundCustomPayloadPacket> syncOxygenPacketsToClient() {
        if (dirty && !level.isClientSide) {
            dirty = false;
            List<ClientboundCustomPayloadPacket> list = new LinkedList<>();
            for (int i = 0; i < sectionDirty.length; i++) {
                if (sectionDirty[i]) {
                    sectionDirty[i] = false;
                    ChunkPos pos = this.getPos();
                    ChunkSectionOxygenAccessorInternal accessor = (ChunkSectionOxygenAccessorInternal) this.getSection(i);
                    int size = 1 + ((Integer.SIZE / Byte.SIZE) * 2) + (1 + (Short.SIZE / Byte.SIZE) + accessor.getModifiedBlocks() > 0 ? (Constant.Chunk.CHUNK_SECTION_AREA / Byte.SIZE) : 0);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(size, size)
                            .writeByte(i).writeInt(pos.x).writeInt(pos.z));
                    accessor.writeOxygenPacket(buf);
                    list.add(new ClientboundCustomPayloadPacket(new ResourceLocation(Constant.MOD_ID, "oxygen_update"), buf));
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean getDefaultBreathable() {
        return this.defaultBreathable;
    }

    @Override
    public void setDefaultBreathable(boolean defaultBreathable) {
        this.defaultBreathable = defaultBreathable;
    }

    @Override
    public void readOxygenUpdate(byte b, @NotNull FriendlyByteBuf buf) {
        LevelChunkSection section = this.getSection(b);
        assert section != null;
        ((ChunkSectionOxygenAccessorInternal) section).readOxygenPacket(buf);
    }

    @Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;hasOnlyAir()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void galacticraft_passDefaultValue(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir, int i, LevelChunkSection chunkSection) {
        this.setBreathable(pos.getX(), pos.getY(), pos.getZ(), this.defaultBreathable);
    }
}
