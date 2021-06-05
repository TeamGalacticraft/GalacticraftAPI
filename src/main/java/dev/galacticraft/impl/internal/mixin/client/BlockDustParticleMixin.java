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

import dev.galacticraft.api.registry.RegistryUtil;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDustParticle.class)
@Environment(EnvType.CLIENT)
public abstract class BlockDustParticleMixin extends Particle {
    public BlockDustParticleMixin(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {super(world, x, y, z, velocityX, velocityY, velocityZ);}

    @Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("RETURN"))
    protected void BlockDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci) {
        this.gravityStrength = 1.0f;
        RegistryUtil.getCelestialBodyByDimension(world.getRegistryManager(), world.getRegistryKey()).ifPresent(celestialBodyType -> this.gravityStrength = ((Landable) celestialBodyType.type()).gravity(celestialBodyType.config()));
    }
}
