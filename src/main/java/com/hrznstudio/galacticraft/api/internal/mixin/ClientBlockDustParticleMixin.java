package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BlockDustParticle.class)
public abstract class ClientBlockDustParticleMixin extends Particle {
    public ClientBlockDustParticleMixin(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {super(world, x, y, z, velocityX, velocityY, velocityZ);}

    @Inject(method = "<init>", at = @At("RETURN"))
    protected void BlockDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState blockState, CallbackInfo ci) {
        this.gravityStrength = 1.0f;
        RegistryKey<World> worldRegistryKey = world.getRegistryKey();
        Optional<CelestialBodyType> body = CelestialBodyType.getByDimType(worldRegistryKey);
        if (body.isPresent()) {
            this.gravityStrength = body.get().getGravity();
        }
    }
}
