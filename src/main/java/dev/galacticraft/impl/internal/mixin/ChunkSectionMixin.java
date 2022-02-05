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
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.accessor.ChunkSectionOxygenAccessorInternal;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.chunk.ChunkSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private @Unique boolean @Nullable [] inverted;
    private @Unique short modifiedBlocks = 0;
    private @Unique boolean defaultBreathable = false;

    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (this.modifiedBlocks == 0) return this.defaultBreathable;
        assert this.inverted != null; // if modifiedBlocks > 0 inverted should not be null.
        boolean b = this.inverted[x + (y << 4) + (z << 8)];
        return (this.defaultBreathable && !b) || (!this.defaultBreathable && b);
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean breathable) {
        boolean inversion = (this.defaultBreathable && !breathable) || (!this.defaultBreathable && breathable);
        if (inversion) {
            if (this.inverted == null) {
                assert this.modifiedBlocks == 0;
                this.inverted = new boolean[16 * 16 * 16];
                this.inverted[x + (y << 4) + (z << 8)] = true;
                this.modifiedBlocks = 1;
            } else {
                if (!this.inverted[x + (y << 4) + (z << 8)]) {
                    this.inverted[x + (y << 4) + (z << 8)] = true;
                    this.modifiedBlocks++;
                }
            }
        } else if (this.inverted != null && this.inverted[x + (y << 4) + (z << 8)]) {
            this.inverted[x + (y << 4) + (z << 8)] = false;
            if (--this.modifiedBlocks == 0) {
                this.inverted = null;
            }
        }
    }

    @Inject(method = "getPacketSize", at = @At("RETURN"), cancellable = true)
    private void galacticraft_increaseChunkPacketSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValueI() + (this.modifiedBlocks == 0 ? 0 : (Constant.Chunk.CHUNK_SECTION_AREA / Byte.SIZE)) + 2 + 1);
    }

    @Inject(method = "isEmpty()Z", at = @At("RETURN"), cancellable = true)
    private void galacticraft_verifyOxygenEmpty(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() && this.modifiedBlocks == 0);
    }

    @Inject(method = "toPacket", at = @At("RETURN"))
    private void galacticraft_writeOxygenDataToPacket(PacketByteBuf buf, CallbackInfo ci) {
        this.writeOxygenPacket(buf);
    }

    @Override
    public boolean @Nullable [] getInversionArray() {
        return this.inverted;
    }

    @Override
    public void setInversionArray(boolean[] inverted) {
        this.inverted = inverted;
    }

    @Override
    public short getModifiedBlocks() {
        return this.modifiedBlocks;
    }

    @Override
    public void setModifiedBlocks(short amount) {
        this.modifiedBlocks = amount;
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
    public void writeOxygenPacket(@NotNull PacketByteBuf buf) {
        buf.writeBoolean(this.getDefaultBreathable());
        buf.writeShort(this.getModifiedBlocks());

        if (this.getModifiedBlocks() > 0) {
            boolean[] inverted = this.getInversionArray();
            assert inverted != null;
            for (int p = 0; p < Constant.Chunk.CHUNK_SECTION_AREA / Byte.SIZE; p++) {
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
    public void readOxygenPacket(@NotNull PacketByteBuf buf) {
        this.setDefaultBreathable(buf.readBoolean());
        this.setModifiedBlocks(buf.readShort());
        if (this.getModifiedBlocks() > 0) {
            boolean[] inverted = this.getInversionArray();
            if (inverted == null) {
                inverted = new boolean[Constant.Chunk.CHUNK_SECTION_AREA];
                this.setInversionArray(inverted);
            }

            for (int i = 0; i < Constant.Chunk.CHUNK_SECTION_AREA; i++) {
                int b = ((int)buf.readByte() + 128);
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
            this.setInversionArray(null);
        }
    }
}
