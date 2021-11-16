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
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.tick.ChunkTickScheduler;
import org.jetbrains.annotations.NotNull;
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
import java.util.function.Consumer;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements ChunkOxygenAccessor, ChunkOxygenSyncer, ChunkOxygenAccessorInternal {
    private final @Unique
    boolean[] updatable = new boolean[16];
    @Shadow
    @Final
    World world;
    private @Unique
    boolean defaultBreathable = false;
    private @Unique
    boolean update = false;

    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (((WorldChunk) (Object) this).isOutOfHeightLimit(y)) return this.defaultBreathable;
        ChunkSection section = ((WorldChunk)(Object)this).getSection(y >> 4);
        if (!section.isEmpty()) {
            return ((ChunkSectionOxygenAccessor) section).isBreathable(x & 15, y & 15, z & 15);
        }
        return this.defaultBreathable;
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (((WorldChunk) (Object) this).isOutOfHeightLimit(y)) return;
        ChunkSection section = ((WorldChunk)(Object)this).getSection(y >> 4);
        assert section != null;
        ChunkSectionOxygenAccessor accessor = ((ChunkSectionOxygenAccessor) section);
        if (value != accessor.isBreathable(x & 15, y & 15, z & 15)) {
            if (!this.world.isClient) {
                ((WorldChunk) (Object) this).setShouldSave(true);
                update = true;
                updatable[y >> 4] = true;
            }
            accessor.setBreathable(x & 15, y & 15, z & 15, value);
        }

    }

    @Override
    public @NotNull List<CustomPayloadS2CPacket> syncToClient_gc() {
        if (update && !world.isClient) {
            update = false;
            List<CustomPayloadS2CPacket> list = new LinkedList<>();
            for (int i = 0; i < 16; i++) {
                if (updatable[i]) {
                    updatable[i] = false;
                    ChunkPos pos = ((WorldChunk) (Object) this).getPos();
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer(1 + 1 + ((16 * 16 * 16) / 8) + (4 * 2), 50 + 1 + ((16 * 16 * 16) / 8) + (4 * 2)).writeByte(i).writeInt(pos.x).writeInt(pos.z));
                    ((ChunkSectionOxygenAccessorInternal) ((WorldChunk) (Object) this).getSection(i)).writeData_gc(buf);
                    list.add(new CustomPayloadS2CPacket(new Identifier(Constant.MOD_ID, "oxygen_update"), buf));
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean getDefaultBreathable_gc() {
        return this.defaultBreathable;
    }

    @Override
    public void setDefaultBreathable_gc(boolean defaultBreathable) {
        this.defaultBreathable = defaultBreathable;
    }

    @Override
    public void readOxygenUpdate(byte b, @NotNull PacketByteBuf buf) {
        ChunkSection section = ((WorldChunk) (Object) this).getSection(b);
        assert section != null;
        if (((ChunkSectionOxygenAccessorInternal) section).getChangedArray_gc() == null) {
            ((ChunkSectionOxygenAccessorInternal) section).setChangedArray_gc(new boolean[16 * 16 * 16]);
        }
        ((ChunkSectionOxygenAccessorInternal) section).readData_gc(buf);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/tick/ChunkTickScheduler;Lnet/minecraft/world/tick/ChunkTickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;Lnet/minecraft/world/gen/chunk/Blender;)V", at = @At("RETURN"))
    private void initDefaultValue_gc(World world, ChunkPos pos, UpgradeData upgradeData, ChunkTickScheduler<Block> blockTickScheduler, ChunkTickScheduler<Fluid> fluidTickScheduler, long inhabitedTime, ChunkSection[] sectionArrayInitializer, Consumer<WorldChunk> loadToWorldConsumer, Blender blendingData, CallbackInfo ci) {
        this.defaultBreathable = ((WorldOxygenAccessorInternal) world).getDefaultBreathable();
        for (ChunkSection section : ((WorldChunk) (Object) this).getSectionArray()) {
            assert section != null;
            ((ChunkSectionOxygenAccessorInternal) section).setDefaultBreathable_gc(this.defaultBreathable);
        }
    }

    @Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;isEmpty()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void passDefaultValue_gc(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir, int i, ChunkSection chunkSection) {
        ((ChunkSectionOxygenAccessorInternal) chunkSection).setDefaultBreathable_gc(this.defaultBreathable);
    }
}
