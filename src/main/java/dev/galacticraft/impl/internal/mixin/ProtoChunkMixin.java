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
import dev.galacticraft.impl.internal.accessor.ChunkOxygenAccessorInternal;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenSyncer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(ProtoChunk.class)
public abstract class ProtoChunkMixin implements ChunkOxygenAccessor, ChunkOxygenSyncer, ChunkOxygenAccessorInternal {
    @Shadow @Final private ChunkSection[] sections;
    private @Unique boolean defaultBreathable = false;

    @Override
    public boolean isBreathable(int x, int y, int z) {
        if (((ProtoChunk)(Object)this).isOutOfHeightLimit(y)) return this.defaultBreathable;
        ChunkSection section = sections[y >> 4];
        if (!ChunkSection.isEmpty(section)) {
            return ((ChunkSectionOxygenAccessor) section).isBreathable(x & 15, y & 15, z & 15);
        }
        return this.defaultBreathable;
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (((ProtoChunk)(Object)this).isOutOfHeightLimit(y)) return;
        ChunkSection section = sections[y >> 4];
        if (!ChunkSection.isEmpty(section)) {
            ((ChunkSectionOxygenAccessor) section).setBreathable(x & 15, y & 15, z & 15, value);
        }
    }

    @Override
    public @NotNull List<CustomPayloadS2CPacket> syncToClient_gc() {
        throw new UnsupportedOperationException("ProtoChunks shouldn't be synced");
    }

    @Override
    public void setDefaultBreathable_gc(boolean defaultBreathable) {
        this.defaultBreathable = defaultBreathable;
    }

    @Override
    public void readOxygenUpdate(byte b, @NotNull PacketByteBuf buf) {
        throw new UnsupportedOperationException("ProtoChunks shouldn't be synced");
    }
}
