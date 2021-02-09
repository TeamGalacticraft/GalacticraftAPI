package com.hrznstudio.galacticraft.api.internal.mixin.client;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Particle.class)
@Environment(EnvType.CLIENT)
public abstract class ParticleMixin {

    @Shadow
    protected float gravityStrength;

    @Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDD)V", at = @At("RETURN"))
    protected void Particle(ClientWorld world, double x, double y, double z, CallbackInfo ci) {
        this.gravityStrength = 1.0f;
        CelestialBodyType.getByDimType(world.getRegistryKey()).ifPresent(celestialBodyType -> this.gravityStrength = celestialBodyType.getGravity());
    }
}
