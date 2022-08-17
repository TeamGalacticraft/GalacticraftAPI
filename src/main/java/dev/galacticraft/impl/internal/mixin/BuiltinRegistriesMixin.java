/*
 * Copyright (c) 2019-2022 Team Galacticraft
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

import com.mojang.serialization.Lifecycle;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.registry.RocketRegistry;
import dev.galacticraft.api.rocket.part.*;
import dev.galacticraft.api.rocket.part.type.*;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.rocket.part.config.*;
import dev.galacticraft.impl.universe.BuiltinObjects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinRegistries.class)
public abstract class BuiltinRegistriesMixin {
    @Shadow
    private static <T, R extends WritableRegistry<T>> R internalRegister(ResourceKey<? extends Registry<T>> registryRef, R registry, BuiltinRegistries.RegistryBootstrap<T> initializer, Lifecycle lifecycle) {
        throw new UnsupportedOperationException("Untransformed mixin");
    }

    @Shadow
    public static <T> Holder<T> register(Registry<T> registry, ResourceLocation resourceLocation, T object) {
        throw new UnsupportedOperationException("Untransformed mixin");
    }

    @Shadow
    public static <T> Holder<T> register(Registry<T> registry, ResourceKey<T> resourceKey, T object) {
        throw new UnsupportedOperationException("Untransformed mixin");
    }

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/BuiltinRegistries;registerSimple(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/data/BuiltinRegistries$RegistryBootstrap;)Lnet/minecraft/core/Registry;", ordinal = 0))
    private static void galacticraft_addDynamicRegistries(CallbackInfo ci) {
        BuiltinObjects.register();
        internalRegister(AddonRegistry.GALAXY_KEY, AddonRegistry.GALAXY, (registry) -> register(registry, BuiltinObjects.MILKY_WAY_KEY, BuiltinObjects.createMilkyWay()), Lifecycle.experimental());
        internalRegister(AddonRegistry.CELESTIAL_BODY_KEY, AddonRegistry.CELESTIAL_BODY, (registry) -> {
            register(registry, BuiltinObjects.EARTH_KEY, BuiltinObjects.createEarth());
            return register(registry, BuiltinObjects.SOL_KEY.location(), BuiltinObjects.createSol());
        }, Lifecycle.experimental());

        internalRegister(RocketRegistry.CONFIGURED_ROCKET_CONE_KEY, RocketRegistry.CONFIGURED_ROCKET_CONE, (registry) -> register(registry, Constant.Misc.INVALID, ConfiguredRocketCone.create(DefaultRocketConeConfig.INSTANCE, RocketConeType.INVALID)), Lifecycle.experimental());
        internalRegister(RocketRegistry.CONFIGURED_ROCKET_BODY_KEY, RocketRegistry.CONFIGURED_ROCKET_BODY, (registry) -> register(registry, Constant.Misc.INVALID, ConfiguredRocketBody.create(DefaultRocketBodyConfig.INSTANCE, RocketBodyType.INVALID)), Lifecycle.experimental());
        internalRegister(RocketRegistry.CONFIGURED_ROCKET_FIN_KEY, RocketRegistry.CONFIGURED_ROCKET_FIN, (registry) -> register(registry, Constant.Misc.INVALID, ConfiguredRocketFin.create(DefaultRocketFinConfig.INSTANCE, RocketFinType.INVALID)), Lifecycle.experimental());
        internalRegister(RocketRegistry.CONFIGURED_ROCKET_BOOSTER_KEY, RocketRegistry.CONFIGURED_ROCKET_BOOSTER, (registry) -> register(registry, Constant.Misc.INVALID, ConfiguredRocketBooster.create(DefaultRocketBoosterConfig.INSTANCE, RocketBoosterType.INVALID)), Lifecycle.experimental());
        internalRegister(RocketRegistry.CONFIGURED_ROCKET_BOTTOM_KEY, RocketRegistry.CONFIGURED_ROCKET_BOTTOM, (registry) -> register(registry, Constant.Misc.INVALID, ConfiguredRocketBottom.create(DefaultRocketBottomConfig.INSTANCE, RocketBottomType.INVALID)), Lifecycle.experimental());
        internalRegister(RocketRegistry.CONFIGURED_ROCKET_UPGRADE_KEY, RocketRegistry.CONFIGURED_ROCKET_UPGRADE, (registry) -> register(registry, Constant.Misc.INVALID, ConfiguredRocketUpgrade.create(DefaultRocketUpgradeConfig.INSTANCE, RocketUpgradeType.INVALID)), Lifecycle.experimental());
    }
}