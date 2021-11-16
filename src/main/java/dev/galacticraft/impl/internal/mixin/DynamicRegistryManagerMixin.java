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

package dev.galacticraft.impl.internal.mixin;

import com.google.common.collect.ImmutableMap;
import dev.galacticraft.api.gas.Gas;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import net.minecraft.util.dynamic.EntryLoader;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Dynamic;
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

    @Inject(method = "method_31141", at = @At(value = "HEAD"))
    private static <E> void testForSync_gc(DynamicRegistryManager.Impl registryManager, EntryLoader.Impl entryLoader, DynamicRegistryManager.Info<E> info, CallbackInfo ci) {
        RegistryKey<? extends Registry<E>> registryKey = info.registry();
        override = (registryKey.equals(AddonRegistry.GAS_KEY) || registryKey.equals(AddonRegistry.GALAXY_KEY) || registryKey.equals(AddonRegistry.CELESTIAL_BODY_KEY) || registryKey.equals(AddonRegistry.ROCKET_PART_KEY));
    }

    @ModifyVariable(method = "method_31141", at = @At(value = "STORE", ordinal = 0), name = "bl", ordinal = 0, index = 4)
    private static boolean modifyForSync_gc(boolean bl) {
        return bl && !override;
    }

    @Dynamic("1.18-pre1 synthetic method")
    @Inject(method = "method_30531", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistryManager;register(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void registerCustomRegistries_gc(CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>>> ci, ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder) {
        builder.put(AddonRegistry.GAS_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.GAS_KEY, Gas.CODEC, Gas.CODEC));
        builder.put(AddonRegistry.GALAXY_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.GALAXY_KEY, Galaxy.CODEC, Galaxy.CODEC));
        builder.put(AddonRegistry.CELESTIAL_BODY_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.CELESTIAL_BODY_KEY, CelestialBody.CODEC, CelestialBody.CODEC));
        builder.put(AddonRegistry.ROCKET_PART_KEY, new DynamicRegistryManager.Info<>(AddonRegistry.ROCKET_PART_KEY, RocketPart.CODEC, RocketPart.CODEC));
    }
}
