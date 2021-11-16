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

import com.mojang.serialization.Codec;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenAccessorInternal;
import dev.galacticraft.impl.internal.accessor.ChunkSectionOxygenAccessorInternal;
import dev.galacticraft.impl.internal.accessor.WorldOxygenAccessorInternal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.*;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {
    @Inject(method = "serialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;getBlockStateContainer()Lnet/minecraft/world/chunk/PalettedContainer;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void serialize_gc(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir, ChunkPos chunkPos, NbtCompound nbtCompound, ChunkSection chunkSections[], NbtList nbtList, LightingProvider lightingProvider, Registry registry, Codec codec, boolean bl, int i, int j, boolean bl2, ChunkNibbleArray chunkNibbleArray, ChunkNibbleArray chunkNibbleArray2, NbtCompound nbtCompound2, ChunkSection chunkSection) {
        NbtCompound tag = new NbtCompound();
        if (((WorldOxygenAccessorInternal) world).getDefaultBreathable() != ((ChunkSectionOxygenAccessorInternal) chunkSection).getDefaultBreathable_gc()) {
            tag.putBoolean(Constant.Nbt.DEFAULT_BREATHABLE, ((ChunkSectionOxygenAccessorInternal) chunkSection).getDefaultBreathable_gc());
        }
        tag.putShort(Constant.Nbt.CHANGE_COUNT, ((ChunkSectionOxygenAccessorInternal) chunkSection).getChangedCount_gc());
        if (((ChunkSectionOxygenAccessorInternal) chunkSection).getChangedCount_gc() > 0) {
            byte[] array = new byte[(16 * 16 * 16) / 8];
            boolean[] oxygenValues = ((ChunkSectionOxygenAccessorInternal) chunkSection).getChangedArray_gc();
            assert oxygenValues != null;
            for (int p = 0; p < oxygenValues.length - 8; p += 9) {
                byte serialized = -128;
                serialized |= oxygenValues[p] ? 0b1 : 0;
                serialized |= oxygenValues[p + 1] ? 0b10 : 0;
                serialized |= oxygenValues[p + 2] ? 0b100 : 0;
                serialized |= oxygenValues[p + 3] ? 0b1000 : 0;
                serialized |= oxygenValues[p + 4] ? 0b10000 : 0;
                serialized |= oxygenValues[p + 5] ? 0b100000 : 0;
                serialized |= oxygenValues[p + 6] ? 0b1000000 : 0;
                serialized |= oxygenValues[p + 7] ? 0b10000000 : 0;
                array[p / 8] = serialized;
            }
            tag.putByteArray(Constant.Nbt.OXYGEN, array);
        }
        nbtCompound2.put(Constant.Nbt.GC_API, tag);
    }

    @Inject(method = "deserialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestStorage;initForPalette(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/ChunkSection;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void deserialize_gc(ServerWorld world, PointOfInterestStorage poiStorage, ChunkPos chunkPos, NbtCompound nbt, CallbackInfoReturnable<ProtoChunk> cir, UpgradeData upgradeData, boolean bl, NbtList nbtList, int i, ChunkSection chunkSections[], boolean bl2, ChunkManager chunkManager, LightingProvider lightingProvider, Registry registry, Codec codec, int j, NbtCompound nbtCompound, int k, int l, PalettedContainer palettedContainer, PalettedContainer palettedContainer2, ChunkSection chunkSection) {
        NbtCompound compound = nbtCompound.getCompound(Constant.Nbt.GC_API);
        short changedCount = compound.getShort(Constant.Nbt.CHANGE_COUNT);
        if (compound.contains(Constant.Nbt.DEFAULT_BREATHABLE)) {
            ((ChunkSectionOxygenAccessorInternal) chunkSection).setDefaultBreathable_gc(compound.getBoolean(Constant.Nbt.DEFAULT_BREATHABLE));
        } else {
            ((ChunkOxygenAccessorInternal) cir.getReturnValue()).setDefaultBreathable_gc(((WorldOxygenAccessorInternal) world).getDefaultBreathable());
        }
        ((ChunkSectionOxygenAccessorInternal) chunkSection).setTotalChanged_gc(changedCount);
        if (changedCount > 0) {
            boolean[] oxygen = new boolean[16 * 16 * 16];
            byte[] bytes = compound.getByteArray("Inverted");
            for (int p = 0; p < 512; p++) {
                short b = (short) (bytes[p] + 128);
                oxygen[(p * 8)] = (b & 0b1) != 0;
                oxygen[(p * 8) + 1] = (b & 0b10) != 0;
                oxygen[(p * 8) + 2] = (b & 0b100) != 0;
                oxygen[(p * 8) + 3] = (b & 0b1000) != 0;
                oxygen[(p * 8) + 4] = (b & 0b10000) != 0;
                oxygen[(p * 8) + 5] = (b & 0b100000) != 0;
                oxygen[(p * 8) + 6] = (b & 0b1000000) != 0;
                oxygen[(p * 8) + 7] = (b & 0b10000000) != 0;
            }
            ((ChunkSectionOxygenAccessorInternal) chunkSection).setChangedArray_gc(oxygen);
        }
    }
}
