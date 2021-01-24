/*
 * Copyright (c) 2020 HRZN LTD
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

import com.google.common.collect.ImmutableMap;
import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.celestialbodies.SolarSystemType;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DynamicRegistryManager.class)
public abstract class DynamicRegistryManagerMixin {
	@Inject(method = "method_30531", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistryManager;register(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void registerCustom(CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>>> ci, ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder) {
		builder.put(AddonRegistry.ATMOSPHERIC_GAS_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.ATMOSPHERIC_GAS_KEY, AtmosphericGas.CODEC, AtmosphericGas.CODEC));
		builder.put(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY, SolarSystemType.CODEC, SolarSystemType.CODEC));
		builder.put(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, CelestialBodyType.CODEC, CelestialBodyType.CODEC));
		builder.put(AddonRegistry.PERMISSIONS_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.PERMISSIONS_KEY, Permission.CODEC, Permission.CODEC));
	}

	@Redirect(method = "method_31141", at = @At(value = "INVOKE", target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z", ordinal = 0))
	private static boolean gc(Object o, Object obj) {
		return o.equals(obj) || o.equals(AddonRegistry.CELESTIAL_BODY_TYPE_KEY) || o.equals(AddonRegistry.ATMOSPHERIC_GAS_KEY) || o.equals(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY) || o.equals(AddonRegistry.PERMISSIONS_KEY);
	}
}