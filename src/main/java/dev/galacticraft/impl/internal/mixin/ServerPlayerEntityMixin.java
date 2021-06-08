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

import dev.galacticraft.api.accessor.ServerResearchAccessor;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerResearchAccessor {
    @Unique
    private final List<Identifier> unlockedResearch = new ArrayList<>();
    @Unique
    private final Object2BooleanMap<Identifier> changes = new Object2BooleanArrayMap<>();

    @Override
    public boolean hasUnlocked_gcr(Identifier id) {
        return false;
    }

    @Override
    public void setUnlocked_gcr(Identifier id, boolean unlocked) {
        if (unlocked) {
            if (!this.unlockedResearch.contains(id)) {
                this.unlockedResearch.add(id);
                this.changes.put(id, true);
            }
        } else {
            if (unlockedResearch.remove(id)) {
                this.changes.put(id, false);
            }
        }
    }

    @Override
    public boolean changed_gcr() {
        return !this.changes.isEmpty();
    }

    @Override
    public PacketByteBuf writeResearchChanges_gcr(PacketByteBuf buf) {
        buf.writeByte(this.changes.size());

        for (Object2BooleanMap.Entry<Identifier> entry : this.changes.object2BooleanEntrySet()) {
            buf.writeBoolean(entry.getBooleanValue());
            buf.writeString(entry.getKey().toString());
        }
        this.changes.clear();
        return buf;
    }

    @Override
    public NbtCompound writeToNbt_gcr(NbtCompound nbt) {
        nbt.putInt("size", this.unlockedResearch.size());
        int i = 0;
        for (Identifier id : this.unlockedResearch) {
            nbt.putString("id_" + i, id.toString());
            i++;
        }
        return nbt;
    }

    @Override
    public void readFromNbt_gcr(NbtCompound nbt) {
        this.unlockedResearch.clear();
        int size = nbt.getInt("size");
        for (int i = 0; i < size; i++) {
            this.unlockedResearch.add(new Identifier(nbt.getString("id_" + i)));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt_gcr(NbtCompound nbt, CallbackInfo ci) {
        this.readFromNbt_gcr(nbt);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt_gcr(NbtCompound nbt, CallbackInfo ci) {
        this.writeToNbt_gcr(nbt);
    }
}
