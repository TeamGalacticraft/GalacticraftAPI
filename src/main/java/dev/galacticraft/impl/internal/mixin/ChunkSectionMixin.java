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

import dev.galacticraft.impl.internal.accessor.ChunkSectionOxygenAccessor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(ChunkSection.class)
public abstract class ChunkSectionMixin implements ChunkSectionOxygenAccessor {
    private @Unique boolean[] breathable;
    private @Unique short breathableCount = 0;

    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (breathableCount == 0 || this.breathable == null) return false;
        return this.breathable[x + (y << 4) + (z << 8)];
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (value && this.breathable == null) this.breathable = new boolean[16 * 16 * 16];
        boolean current = this.breathable[x + (y * 16) + (z * 16 * 16)];
        if (current != value) {
            if (value) {
                breathableCount++;
            } else {
                breathableCount--;
                if (breathableCount == 0) {
                    this.breathable = null;
                    return;
                }
            }
        }
        this.breathable[x + (y * 16) + (z * 16 * 16)] = value;
    }

    @Inject(method = "getPacketSize", at = @At("RETURN"), cancellable = true)
    private void addOxygenSize_gc(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValueI() + (this.breathableCount == 0 ? 0 : (4096 / 8)) + 2 + 1);
    }

    @Inject(method = "toPacket", at = @At("RETURN"))
    private void toPacket_gc(PacketByteBuf buf, CallbackInfo ci) {
        buf.writeShort(this.breathableCount);
        if (this.breathableCount > 0) {
            this.writeOxygen(buf);
        }
    }

    @Override
    public boolean[] getArray() {
        return this.breathable;
    }

    @Override
    public short getTotalOxygen() {
        return this.breathableCount;
    }

    @Override
    public void setTotalOxygen(short amount) {
        this.breathableCount = amount;
    }

    @Override
    public void writeOxygen(PacketByteBuf buf) {
        boolean[] arr = this.getArray();
        for (int p = 0; p < (16 * 16 * 16) / 8; p++) {
            byte b = -128;
            b += arr[(p * 8)] ? 1 : 0;
            b += arr[(p * 8) + 1] ? 2 : 0;
            b += arr[(p * 8) + 2] ? 4 : 0;
            b += arr[(p * 8) + 3] ? 8 : 0;
            b += arr[(p * 8) + 4] ? 16 : 0;
            b += arr[(p * 8) + 5] ? 32 : 0;
            b += arr[(p * 8) + 6] ? 64 : 0;
            b += arr[(p * 8) + 7] ? 128 : 0;
            buf.writeByte(b);
        }
    }

    @Override
    public void readOxygen(PacketByteBuf buf) {
        boolean[] oxygen = this.getArray();
        for (int i = 0; i < (16 * 16 * 16) / 8; i++) {
            short b = (short) (buf.readByte() + 128);
            oxygen[(i * 8)] = (b & 1) != 0;
            oxygen[(i * 8) + 1] = (b & 2) != 0;
            oxygen[(i * 8) + 2] = (b & 4) != 0;
            oxygen[(i * 8) + 3] = (b & 8) != 0;
            oxygen[(i * 8) + 4] = (b & 16) != 0;
            oxygen[(i * 8) + 5] = (b & 32) != 0;
            oxygen[(i * 8) + 6] = (b & 64) != 0;
            oxygen[(i * 8) + 7] = (b & 128) != 0;
        }
    }
}
