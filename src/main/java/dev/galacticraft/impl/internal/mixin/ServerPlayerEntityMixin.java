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

import dev.galacticraft.api.accessor.GearInventoryProvider;
import dev.galacticraft.api.accessor.ServerResearchAccessor;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.inventory.MappedInventory;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerResearchAccessor, GearInventoryProvider {
    private final @Unique List<Identifier> unlockedResearch = new ArrayList<>();
    private final @Unique Object2BooleanMap<Identifier> changes = new Object2BooleanArrayMap<>();

    private final @Unique SimpleInventory gearInv = this.createGearInv();
    private final @Unique Inventory tankInv = MappedInventory.create(this.gearInv, 4, 5);
    private final @Unique Inventory thermalArmorInv = MappedInventory.create(this.gearInv, 0, 1, 2, 3);
    private final @Unique Inventory accessoryInv = MappedInventory.create(this.gearInv, 6, 7, 8, 9, 10, 11);

    @Override
    public boolean hasUnlocked_gc(Identifier id) {
        return unlockedResearch.contains(id);
    }

    @Override
    public void setUnlocked_gc(Identifier id, boolean unlocked) {
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
    public boolean changed_gc() {
        return !this.changes.isEmpty();
    }

    @Override
    public PacketByteBuf writeResearchChanges_gc(PacketByteBuf buf) {
        buf.writeByte(this.changes.size());

        for (Object2BooleanMap.Entry<Identifier> entry : this.changes.object2BooleanEntrySet()) {
            buf.writeBoolean(entry.getBooleanValue());
            buf.writeString(entry.getKey().toString());
        }
        this.changes.clear();
        return buf;
    }

    @Override
    public NbtCompound writeToNbt_gc(NbtCompound nbt) {
        nbt.putInt("size", this.unlockedResearch.size());
        int i = 0;
        for (Identifier id : this.unlockedResearch) {
            nbt.putString("id_" + i, id.toString());
            i++;
        }
        return nbt;
    }

    @Override
    public void readFromNbt_gc(NbtCompound nbt) {
        this.unlockedResearch.clear();
        int size = nbt.getInt("size");
        for (int i = 0; i < size; i++) {
            this.unlockedResearch.add(new Identifier(nbt.getString("id_" + i)));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt_gc(NbtCompound nbt, CallbackInfo ci) {
        this.readFromNbt_gc(nbt);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt_gc(NbtCompound nbt, CallbackInfo ci) {
        this.writeToNbt_gc(nbt);
    }

    private SimpleInventory createGearInv() {
        SimpleInventory inv = new SimpleInventory(12);
        inv.addListener((inventory) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(((ServerPlayerEntity) (Object) this).getId());
            buf.writeInt(inventory.size());
            for (int i = 0; i < inventory.size(); i++) {
                buf.writeItemStack(inventory.getStack(i));
            }

            Collection<ServerPlayerEntity> tracking = PlayerLookup.tracking(((ServerPlayerEntity) (Object) this));
            //noinspection SuspiciousMethodCalls
            if (!tracking.contains(this)) {
                ServerPlayNetworking.send(((ServerPlayerEntity) (Object) this), new Identifier(Constant.MOD_ID, "gear_inv_sync"), buf);
            }
            for (ServerPlayerEntity player : tracking) {
                ServerPlayNetworking.send(player, new Identifier(Constant.MOD_ID, "gear_inv_sync"), PacketByteBufs.copy(buf));
            }
        });
        return inv;
    }

    @Override
    public SimpleInventory getGearInv() {
        return this.gearInv;
    }

    @Override
    public Inventory getOxygenTanks() {
        return this.tankInv;
    }

    @Override
    public Inventory getThermalArmor() {
        return this.thermalArmorInv;
    }

    @Override
    public Inventory getAccessories() {
        return this.accessoryInv;
    }

    @Override
    public void writeGearToNbt(NbtCompound tag) {
        tag.put(Constant.Nbt.GEAR_INV, this.getGearInv().toNbtList());
    }

    @Override
    public void readGearFromNbt(NbtCompound tag) {
        this.getGearInv().readNbtList(tag.getList(Constant.Nbt.GEAR_INV, NbtElement.COMPOUND_TYPE));
    }
}
