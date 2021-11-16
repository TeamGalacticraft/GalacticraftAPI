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

package dev.galacticraft.impl.internal.mixin.client;

import dev.galacticraft.api.accessor.GearInventoryProvider;
import dev.galacticraft.api.accessor.SoundSystemAccessor;
import dev.galacticraft.api.client.accessor.ClientResearchAccessor;
import dev.galacticraft.api.item.Accessory;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.inventory.MappedInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin implements ClientResearchAccessor, GearInventoryProvider {
    @Unique
    private final List<Identifier> unlockedResearch = new ArrayList<>();
    @Shadow
    @Final
    public ClientWorld clientWorld;
    private final @Unique SimpleInventory gearInv = createInv();
    private final @Unique Inventory tankInv = MappedInventory.create(this.gearInv, 4, 5);
    private final @Unique Inventory thermalArmorInv = MappedInventory.create(this.gearInv, 0, 1, 2, 3);
    private final @Unique Inventory accessoryInv = MappedInventory.create(this.gearInv, 6, 7, 8, 9, 10, 11);

    private SimpleInventory createInv() {
        SimpleInventory inv = new SimpleInventory(12);
        inv.addListener((inventory) -> {
            float pressure = CelestialBody.getByDimension(this.clientWorld).map(body -> body.atmosphere().pressure()).orElse(1.0f);
            if (pressure != 1.0f) {
                for (int i = 0; i < inventory.size(); i++) {
                    ItemStack stack = inventory.getStack(i);
                    if (stack.getItem() instanceof Accessory accessory && accessory.enablesHearing()) {
                        ((SoundSystemAccessor) ((SoundManagerAccessor) MinecraftClient.getInstance().getSoundManager()).getSoundSystem())
                                .gc_updateAtmosphericMultiplier(1.0f);
                        return;
                    } else {
                        ((SoundSystemAccessor) ((SoundManagerAccessor) MinecraftClient.getInstance().getSoundManager()).getSoundSystem())
                                .gc_updateAtmosphericMultiplier(pressure);
                    }
                }
            } else {
                ((SoundSystemAccessor) ((SoundManagerAccessor) MinecraftClient.getInstance().getSoundManager()).getSoundSystem()).gc_updateAtmosphericMultiplier(pressure);
            }
        });
        return inv;
    }

    @Override
    public void readChanges(PacketByteBuf buf) {
        byte size = buf.readByte();

        for (byte i = 0; i < size; i++) {
            if (buf.readBoolean()) {
                this.unlockedResearch.add(new Identifier(buf.readString()));
            } else {
                this.unlockedResearch.remove(new Identifier(buf.readString()));
            }
        }
    }

    @Override
    public boolean hasUnlocked_gc(Identifier id) {
        return this.unlockedResearch.contains(id);
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
    public NbtCompound writeGearToNbt(NbtCompound tag) {
        tag.put(Constant.Nbt.GEAR_INV, this.getGearInv().toNbtList());
        return tag;
    }

    @Override
    public void readGearFromNbt(NbtCompound tag) {
        this.getGearInv().readNbtList(tag.getList(Constant.Nbt.GEAR_INV, NbtElement.COMPOUND_TYPE));
    }
}
