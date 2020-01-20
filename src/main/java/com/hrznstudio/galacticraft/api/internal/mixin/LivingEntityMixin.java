package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "travel", at = @At(value = "FIELD"), ordinal = 0, name = "d")
    private double modifyGravity(double d) {
        Optional<CelestialBodyType> type = CelestialBodyType.getByDimType(((LivingEntity)(Object)this).world.dimension.getType());
        return type.map(celestialBodyType -> celestialBodyType.getGravity() * 0.08d).orElse(0.08d);
    }
}
