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

package dev.galacticraft.api.internal.mixin;

import com.google.common.collect.ImmutableMap;
import dev.galacticraft.api.atmosphere.AtmosphericGas;
import dev.galacticraft.api.celestialbodies.CelestialBodyType;
import dev.galacticraft.api.celestialbodies.SolarSystemType;
import dev.galacticraft.api.regisry.AddonRegistry;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.teams.data.Permission;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DynamicRegistryManager.class)
public abstract class DynamicRegistryManagerMixin {
	@Unique
	private static boolean override = false;

	@Inject(method = "method_31141", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;get(Lnet/minecraft/util/registry/RegistryKey;)Lnet/minecraft/util/registry/MutableRegistry;", shift = At.Shift.BEFORE, ordinal = 0))
	private static <E> void testGC(DynamicRegistryManager.Impl impl, RegistryOps.EntryLoader.Impl impl2, DynamicRegistryManager.Info<E> info, CallbackInfo ci) {
		RegistryKey<? extends Registry<E>> registryKey = info.getRegistry();
		override = (registryKey.equals(AddonRegistry.CELESTIAL_BODY_TYPE_KEY) || registryKey.equals(AddonRegistry.ATMOSPHERIC_GAS_KEY) || registryKey.equals(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY) || registryKey.equals(AddonRegistry.PERMISSIONS_KEY));
	}

	@ModifyVariable(method = "method_31141", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;get(Lnet/minecraft/util/registry/RegistryKey;)Lnet/minecraft/util/registry/MutableRegistry;", ordinal = 0), name = "b", ordinal = 0, index = 4)
	private static boolean modifyGC(boolean b) {
		return b && !override;
	}

	@SuppressWarnings("UnresolvedMixinReference") //synthetic >:(
	@Inject(method = "method_30531", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistryManager;register(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void registerCustom(CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>>> ci, ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder) {
		builder.put(AddonRegistry.ATMOSPHERIC_GAS_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.ATMOSPHERIC_GAS_KEY, AtmosphericGas.CODEC, AtmosphericGas.CODEC));
		builder.put(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY, SolarSystemType.CODEC, SolarSystemType.CODEC));
		builder.put(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, CelestialBodyType.CODEC, CelestialBodyType.CODEC));
		builder.put(AddonRegistry.PERMISSIONS_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.PERMISSIONS_KEY, Permission.CODEC, Permission.CODEC));
		builder.put(AddonRegistry.ROCKET_PART_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.ROCKET_PART_KEY, RocketPart.CODEC, RocketPart.CODEC));
	}
}