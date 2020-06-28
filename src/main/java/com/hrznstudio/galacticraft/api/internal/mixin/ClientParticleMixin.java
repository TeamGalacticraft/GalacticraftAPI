package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
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
public abstract class ClientParticleMixin {

    @Shadow
    protected float gravityStrength;

    @Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDD)V", at = @At("RETURN"))
    protected void Particle(ClientWorld world, double x, double y, double z, CallbackInfo ci) {
        this.gravityStrength = 1.0f;
        RegistryKey<World> worldRegistryKey = world.getRegistryKey();
        Optional<CelestialBodyType> body = CelestialBodyType.getByDimType(worldRegistryKey);
        if (body.isPresent()) {
            this.gravityStrength = body.get().getGravity();
        }
    }
}
