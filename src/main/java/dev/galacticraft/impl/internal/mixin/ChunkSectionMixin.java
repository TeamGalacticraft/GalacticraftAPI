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

import dev.galacticraft.api.accessor.ChunkSectionOxygenAccessor;
import dev.galacticraft.impl.internal.accessor.ChunkSectionOxygenAccessorInternal;
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
public abstract class ChunkSectionMixin implements ChunkSectionOxygenAccessor, ChunkSectionOxygenAccessorInternal {
    private @Unique
    boolean[] inverted;
    private @Unique
    short changedCount = 0;
    private @Unique
    boolean defaultBreathable = false;

    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (this.changedCount == 0) return this.defaultBreathable;
        return this.defaultBreathable ^ this.inverted[x + (y << 4) + (z << 8)];
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (this.inverted == null) {
            assert this.changedCount == 0;
            if (this.defaultBreathable != value) {
                this.inverted = new boolean[16 * 16 * 16];
                this.inverted[x + (y * 16) + (z * 16 * 16)] = true;
                this.changedCount = 1;
            }
            return;
        }
        boolean current = this.inverted[x + (y * 16) + (z * 16 * 16)];
        if (current != value) {
            if (value) {
                changedCount++;
            } else {
                changedCount--;
                if (changedCount == 0) {
                    this.inverted = null;
                    return;
                }
            }
        }
        this.inverted[x + (y << 4) + (z << 8)] = (this.defaultBreathable ^ value);
    }

    @Inject(method = "getPacketSize", at = @At("RETURN"), cancellable = true)
    private void addOxygenSize_gc(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValueI() + (this.changedCount == 0 ? 0 : (4096 / 8)) + 2 + 1 + 1);
    }

    @Inject(method = "isEmpty()Z", at = @At("RETURN"), cancellable = true)
    private void testForOxygenEmpty_gc(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() && this.changedCount == 0);
    }

    @Inject(method = "toPacket", at = @At("RETURN"))
    private void toPacket_gc(PacketByteBuf buf, CallbackInfo ci) {
        buf.writeBoolean(this.defaultBreathable);
        buf.writeShort(this.changedCount);
        if (this.changedCount > 0) {
            this.writeData_gc(buf);
        }
    }

    @Override
    public boolean[] getChangedArray_gc() {
        return this.inverted;
    }

    @Override
    public void setChangedArray_gc(boolean[] inverted) {
        this.inverted = inverted;
    }

    @Override
    public short getChangedCount_gc() {
        return this.changedCount;
    }

    @Override
    public void setTotalChanged_gc(short amount) {
        this.changedCount = amount;
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
    public void writeData_gc(PacketByteBuf buf) {
        boolean[] inverted = this.getChangedArray_gc();
        if (this.inverted == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            for (int p = 0; p < (16 * 16 * 16) / 8; p++) {
                byte b = -128;
                b += inverted[(p * 8)] ? 1 : 0;
                b += inverted[(p * 8) + 1] ? 2 : 0;
                b += inverted[(p * 8) + 2] ? 4 : 0;
                b += inverted[(p * 8) + 3] ? 8 : 0;
                b += inverted[(p * 8) + 4] ? 16 : 0;
                b += inverted[(p * 8) + 5] ? 32 : 0;
                b += inverted[(p * 8) + 6] ? 64 : 0;
                b += inverted[(p * 8) + 7] ? 128 : 0;
                buf.writeByte(b);
            }
        }
    }

    @Override
    public void readData_gc(PacketByteBuf buf) {
        boolean[] inverted = this.getChangedArray_gc();
        boolean notEmpty = buf.readBoolean();
        if (notEmpty) {
            for (int i = 0; i < 512; i++) {
                short b = (short) (buf.readByte() + 128);
                inverted[(i * 8)] = (b & 1) != 0;
                inverted[(i * 8) + 1] = (b & 2) != 0;
                inverted[(i * 8) + 2] = (b & 4) != 0;
                inverted[(i * 8) + 3] = (b & 8) != 0;
                inverted[(i * 8) + 4] = (b & 16) != 0;
                inverted[(i * 8) + 5] = (b & 32) != 0;
                inverted[(i * 8) + 6] = (b & 64) != 0;
                inverted[(i * 8) + 7] = (b & 128) != 0;
            }
        } else {
            this.setChangedArray_gc(null);
        }
    }
}
