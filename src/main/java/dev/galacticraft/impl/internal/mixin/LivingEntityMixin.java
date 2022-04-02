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
import dev.galacticraft.api.accessor.WorldOxygenAccessor;
import dev.galacticraft.api.entity.attribute.GcApiEntityAttributes;
import dev.galacticraft.api.gas.GasVariant;
import dev.galacticraft.api.gas.Gases;
import dev.galacticraft.api.item.Accessory;
import dev.galacticraft.api.item.OxygenGear;
import dev.galacticraft.api.item.OxygenMask;
import dev.galacticraft.api.transfer.v1.gas.GasStorage;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.impl.internal.fabric.GalacticraftAPI;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements GearInventoryProvider {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow protected abstract int getNextAirOnLand(int air);

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08))
    private double galacticraft_modifyGravity(double d) {
        return CelestialBody.getByDimension(this.world).map(celestialBodyType -> celestialBodyType.gravity() * 0.08d).orElse(0.08);
    }

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.01))
    private double galacticraft_modifySlowFallingGravity(double d) {
        return CelestialBody.getByDimension(this.world).map(celestialBodyType -> celestialBodyType.gravity() * 0.01d).orElse(0.01);
    }

    @Shadow
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void galacticraft_modifyFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        StatusEffectInstance effectInstance = this.getStatusEffect(StatusEffects.JUMP_BOOST);
        float ff = effectInstance == null ? 0.0F : (float) (effectInstance.getAmplifier() + 6);
        CelestialBody.getByDimension(this.world).ifPresent(celestialBodyType -> cir.setReturnValue((int) (MathHelper.ceil((fallDistance * celestialBodyType.gravity()) - 3.0F - ff) * damageMultiplier)));
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z", ordinal = 0))
    private boolean galacticraft_testForBreathability(LivingEntity entity, TagKey<Fluid> tag) {
        //noinspection ConstantConditions
        assert ((Object) entity) == this;
        return entity.isSubmergedIn(tag) || !((WorldOxygenAccessor) this.world).isBreathable(entity.getBlockPos().offset(Direction.UP, (int) Math.floor(this.getEyeHeight(entity.getPose(), entity.getDimensions(entity.getPose())))));
    }

    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void thistickAccessories(CallbackInfo ci) {
        LivingEntity thisEntity = ((LivingEntity) (Object) this);
        for (int i = 0; i < this.getAccessories().size(); i++) {
            ItemStack stack = this.getAccessories().getStack(i);
            if (stack.getItem() instanceof Accessory accessory) {
                accessory.tick(thisEntity);
            }
        }
    }

    @Inject(method = "getNextAirUnderwater", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getRespiration(Lnet/minecraft/entity/LivingEntity;)I"), cancellable = true)
    private void galacticraft_modifyAirLevel(int air, CallbackInfoReturnable<Integer> ci) {
        EntityAttributeInstance attribute = ((LivingEntity) (Object) this).getAttributeInstance(GcApiEntityAttributes.CAN_BREATHE_IN_SPACE);
        if (attribute != null && attribute.getValue() >= 0.99D) {
            ci.setReturnValue(this.getNextAirOnLand(air));
        }

        boolean mask = false;
        boolean gear = false;
        for (int i = 0; i < this.getAccessories().size(); i++) {
            Item item = this.getAccessories().getStack(i).getItem();
            if (!mask && item instanceof OxygenMask) {
                mask = true;
                if (gear) break;
            } else if (!gear && item instanceof OxygenGear) {
                gear = true;
                if (mask) break;
            }
        }

        if (mask && gear) {
            Inventory tankInv = this.getOxygenTanks();
            for (int i = 0; i < tankInv.size(); i++) {
                Storage<GasVariant> storage = ContainerItemContext.withInitial(tankInv.getStack(i)).find(GasStorage.ITEM);
                if (storage != null) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        if (storage.extract(GasVariant.of(Gases.OXYGEN), 1L, transaction) > 0) {
                            transaction.commit();
                            ci.setReturnValue(this.getNextAirOnLand(air));
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "dropInventory", at = @At(value = "RETURN"))
    private void galacticraft_dropGearInventory(CallbackInfo ci) {
        if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            Inventory gearInv = this.getGearInv();
            for (int i = 0; i < gearInv.size(); ++i) {
                ItemStack itemStack = gearInv.getStack(i);
                gearInv.setStack(i, ItemStack.EMPTY);
                if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                    //noinspection ConstantConditions
                    if (((Object) this) instanceof PlayerEntity player) {
                        player.dropItem(itemStack, true, false);
                    } else {
                        this.dropStack(itemStack);
                    }
                }
            }
        }
    }

    @Override
    public Inventory getGearInv() {
        return GalacticraftAPI.EMPTY_INV;
    }

    @Override
    public Inventory getOxygenTanks() {
        return GalacticraftAPI.EMPTY_INV;
    }

    @Override
    public Inventory getThermalArmor() {
        return GalacticraftAPI.EMPTY_INV;
    }

    @Override
    public Inventory getAccessories() {
        return GalacticraftAPI.EMPTY_INV;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void galacticraft_writeGearInventory(NbtCompound nbt, CallbackInfo ci) {
        this.writeGearToNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void galacticraft_readGearInventory(NbtCompound tag, CallbackInfo ci) {
        this.readGearFromNbt(tag);
    }

    @Override
    public void writeGearToNbt(NbtCompound tag) {
    }

    @Override
    public void readGearFromNbt(NbtCompound tag) {
    }
}
