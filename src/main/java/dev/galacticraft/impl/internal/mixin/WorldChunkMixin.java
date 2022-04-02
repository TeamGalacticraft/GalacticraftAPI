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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.BlendingData;
import net.minecraft.world.tick.ChunkTickScheduler;
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

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin extends Chunk implements ChunkOxygenAccessor, ChunkOxygenSyncer, ChunkOxygenAccessorInternal {
    @Shadow @Final World world;
    private @Unique boolean /*@NotNull*/ [] sectionDirty;
    private @Unique boolean defaultBreathable = false;
    private @Unique boolean dirty = false;

    private WorldChunkMixin(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> biome, long inhabitedTime, @Nullable ChunkSection[] sectionArrayInitializer, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biome, inhabitedTime, sectionArrayInitializer, blendingData);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/tick/ChunkTickScheduler;Lnet/minecraft/world/tick/ChunkTickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Lnet/minecraft/world/chunk/WorldChunk$EntityLoader;Lnet/minecraft/world/gen/chunk/BlendingData;)V", at = @At("RETURN"))
    private void galacticraft_init(@NotNull World world, ChunkPos pos, UpgradeData upgradeData, ChunkTickScheduler<Block> blockTickScheduler, ChunkTickScheduler<Fluid> fluidTickScheduler, long inhabitedTime, ChunkSection[] sectionArrayInitializer, WorldChunk.EntityLoader entityLoader, BlendingData blendingData, CallbackInfo ci) {
        this.sectionDirty = new boolean[world.countVerticalSections()];
        this.defaultBreathable = ((WorldOxygenAccessorInternal) world).getDefaultBreathable();
        for (ChunkSection section : this.getSectionArray()) {
            assert section != null;
            ((ChunkSectionOxygenAccessorInternal) section).setDefaultBreathable(this.defaultBreathable);
        }
    }


    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (this.isOutOfHeightLimit(y)) return this.defaultBreathable;
        ChunkSection section = this.getSection(this.getSectionIndex(y));
        if (!section.isEmpty()) {
            return ((ChunkSectionOxygenAccessor) section).isBreathable(x & 15, y & 15, z & 15);
        }
        return this.defaultBreathable;
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (this.isOutOfHeightLimit(y)) return;
        ChunkSection section = this.getSection(this.getSectionIndex(y));
        assert section != null;
        ChunkSectionOxygenAccessor accessor = ((ChunkSectionOxygenAccessor) section);
        if (value != accessor.isBreathable(x & 15, y & 15, z & 15)) {
            if (!this.world.isClient) {
                this.needsSaving = true;
                this.dirty = true;
                sectionDirty[this.getSectionIndex(y)] = true;
            }
            accessor.setBreathable(x & 15, y & 15, z & 15, value);
        }
    }

    @Override
    public @NotNull List<CustomPayloadS2CPacket> syncOxygenPacketsToClient() {
        if (dirty && !world.isClient) {
            dirty = false;
            List<CustomPayloadS2CPacket> list = new LinkedList<>();
            for (int i = 0; i < sectionDirty.length; i++) {
                if (sectionDirty[i]) {
                    sectionDirty[i] = false;
                    ChunkPos pos = this.getPos();
                    ChunkSectionOxygenAccessorInternal accessor = (ChunkSectionOxygenAccessorInternal) this.getSection(i);
                    int size = 1 + ((Integer.SIZE / Byte.SIZE) * 2) + (1 + (Short.SIZE / Byte.SIZE) + accessor.getModifiedBlocks() > 0 ? (Constant.Chunk.CHUNK_SECTION_AREA / Byte.SIZE) : 0);
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer(size, size)
                            .writeByte(i).writeInt(pos.x).writeInt(pos.z));
                    accessor.writeOxygenPacket(buf);
                    list.add(new CustomPayloadS2CPacket(new Identifier(Constant.MOD_ID, "oxygen_update"), buf));
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
    public void readOxygenUpdate(byte b, @NotNull PacketByteBuf buf) {
        ChunkSection section = this.getSection(b);
        assert section != null;
        ((ChunkSectionOxygenAccessorInternal) section).readOxygenPacket(buf);
    }

    @Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;isEmpty()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void galacticraft_passDefaultValue(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir, int i, ChunkSection chunkSection) {
        this.setBreathable(pos.getX(), pos.getY(), pos.getZ(), this.defaultBreathable);
    }
}
