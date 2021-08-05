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

import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv;
import dev.galacticraft.api.accessor.GearInventoryProvider;
import dev.galacticraft.api.accessor.SoundSystemAccessor;
import dev.galacticraft.api.client.accessor.ClientResearchAccessor;
import dev.galacticraft.api.item.Accessory;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
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
    @Shadow @Final public ClientWorld clientWorld;
    @Unique
    private final List<Identifier> unlockedResearch = new ArrayList<>();
    private final @Unique FullFixedItemInv gearInv = createInv();
    private final @Unique FixedItemInv tankInv = this.gearInv.getSubInv(4, 5 + 1);
    private final @Unique FixedItemInv thermalArmorInv = this.gearInv.getSubInv(0, 3 + 1);
    private final @Unique FixedItemInv accessoryInv = this.gearInv.getSubInv(6, 11 + 1);

    private FullFixedItemInv createInv() {
        FullFixedItemInv inv = new FullFixedItemInv(12);
        inv.setOwnerListener((invView, slot, prev, current) -> {
            if (current.getItem() instanceof Accessory accessory && accessory.enablesHearing()) {
                ((SoundSystemAccessor) MinecraftClient.getInstance().getSoundManager().soundSystem).gc_updateAtmosphericMultiplier(1.0f);
            } else if (prev.getItem() instanceof Accessory accessory && accessory.enablesHearing()) {
                boolean hasFreqModule = false;
                for (int i = 0; i < invView.getSlotCount(); i++) {
                    if (i == slot) continue;
                    if (invView.getInvStack(i).getItem() instanceof Accessory accessory2 && accessory2.enablesHearing()) {
                        ((SoundSystemAccessor) MinecraftClient.getInstance().getSoundManager().soundSystem).gc_updateAtmosphericMultiplier(1.0f);
                        hasFreqModule = true;
                        break;
                    }
                }
                if (!hasFreqModule) {
                    ((SoundSystemAccessor) MinecraftClient.getInstance().getSoundManager().soundSystem)
                            .gc_updateAtmosphericMultiplier(CelestialBody.getByDimension(this.clientWorld)
                                    .map(body -> body.atmosphere().pressure()).orElse(1.0f));
                }
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
    public FullFixedItemInv getGearInv() {
        return this.gearInv;
    }

    @Override
    public FixedItemInv getOxygenTanks() {
        return this.tankInv;
    }

    @Override
    public FixedItemInv getThermalArmor() {
        return this.thermalArmorInv;
    }

    @Override
    public FixedItemInv getAccessories() {
        return this.accessoryInv;
    }

    @Override
    public NbtCompound writeGearToNbt(NbtCompound tag) {
        tag.put("GearInv", this.getGearInv().toTag());
        return tag;
    }

    @Override
    public void readGearFromNbt(NbtCompound tag) {
        this.getGearInv().fromTag(tag.getCompound("GearInv"));
    }
}
