/*
 * Copyright (c) 2019-2021 HRZN LTD
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

package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.celestialbodies.SolarSystemType;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.hrznstudio.galacticraft.api.rocket.part.RocketParts;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(BuiltinRegistries.class)
public abstract class BuiltinRegistriesMixin {
    @Shadow
    private static <T, R extends MutableRegistry<T>> R addRegistry(RegistryKey<? extends Registry<T>> registryRef, R registry, Supplier<T> defaultValueSupplier, Lifecycle lifecycle) {
        return null;
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/BuiltinRegistries;addRegistry(Lnet/minecraft/util/registry/RegistryKey;Ljava/util/function/Supplier;)Lnet/minecraft/util/registry/Registry;", ordinal = 0))
    private static void addGCRegistries(CallbackInfo ci) {
        addRegistry(AddonRegistry.ATMOSPHERIC_GAS_KEY, AddonRegistry.ATMOSPHERIC_GASES, () -> AtmosphericGas.OXYGEN, Lifecycle.experimental());
        addRegistry(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY, AddonRegistry.SOLAR_SYSTEMS, () -> SolarSystemType.SOL, Lifecycle.experimental());
        addRegistry(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, AddonRegistry.CELESTIAL_BODIES, () -> CelestialBodyType.THE_SUN, Lifecycle.experimental());
        addRegistry(AddonRegistry.PERMISSIONS_KEY, AddonRegistry.PERMISSIONS, () -> Permission.INVITE_PLAYER, Lifecycle.experimental());
        addRegistry(AddonRegistry.ROCKET_PART_KEY, AddonRegistry.ROCKET_PARTS, () -> RocketParts.INVALID, Lifecycle.experimental());
    }
}