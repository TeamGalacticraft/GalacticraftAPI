package com.hrznstudio.galacticraft.api.internal.mixin.client;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
@Environment(EnvType.CLIENT)
public abstract class BlockDustParticleMixin extends Particle {
    public BlockDustParticleMixin(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {super(world, x, y, z, velocityX, velocityY, velocityZ);}

    @Inject(method = "<init>", at = @At("RETURN"))
    protected void BlockDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState blockState, CallbackInfo ci) {
        this.gravityStrength = 1.0f;
        CelestialBodyType.getByDimType(world.getRegistryKey()).ifPresent(celestialBodyType -> this.gravityStrength = celestialBodyType.getGravity());
    }
}
