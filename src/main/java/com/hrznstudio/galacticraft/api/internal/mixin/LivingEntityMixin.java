package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "travel", at = @At(value = "FIELD"), ordinal = 0, name = "d")
    private double modifyGravity(double d) {
        Optional<CelestialBodyType> type = CelestialBodyType.getByDimType(((LivingEntity)(Object)this).world.getRegistryKey());
        return type.map(celestialBodyType -> celestialBodyType.getGravity() * 0.08d).orElse(0.08d);
    }

    @Shadow
    protected abstract int getNextAirUnderwater(int air);

    @Shadow
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void onComputeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        RegistryKey<World> worldRegistryKey = this.world.getRegistryKey();
        CelestialBodyType body = CelestialBodyType.getByDimType(worldRegistryKey).get();
        if (body != null) {
            StatusEffectInstance statusEffectInstanc = this.getStatusEffect(StatusEffects.JUMP_BOOST);
            float ff = statusEffectInstanc == null ? 0.0F : (float) (statusEffectInstanc.getAmplifier() + 6);
            cir.setReturnValue(MathHelper.ceil(((fallDistance / (1 / body.getGravity())) - 3.0F - ff) * damageMultiplier));
        }
    }
}
