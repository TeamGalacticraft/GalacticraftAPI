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

import dev.galacticraft.api.registry.RegistryUtil;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "travel", at = @At(value = "FIELD"), ordinal = 0, name = "d")
    private double modifyGravity(double d) {
        return RegistryUtil.getCelestialBodyByDimension(this.world.getRegistryManager(), this.world.getRegistryKey()).map(celestialBodyType -> ((Landable) celestialBodyType.type()).gravity(celestialBodyType.config()) * 0.08d).orElse(0.08d);
    }

    @Shadow
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void onComputeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        StatusEffectInstance effectInstance = this.getStatusEffect(StatusEffects.JUMP_BOOST);
        float ff = effectInstance == null ? 0.0F : (float) (effectInstance.getAmplifier() + 6);
        RegistryUtil.getCelestialBodyByDimension(this.world.getRegistryManager(), this.world.getRegistryKey()).ifPresent(celestialBodyType -> cir.setReturnValue(MathHelper.ceil(((fallDistance / (1 / ((Landable) celestialBodyType.type()).gravity(celestialBodyType.config()))) - 3.0F - ff) * damageMultiplier)));
    }
}
