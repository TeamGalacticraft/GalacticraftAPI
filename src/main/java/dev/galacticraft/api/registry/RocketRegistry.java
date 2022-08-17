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

package dev.galacticraft.api.registry;

import com.mojang.serialization.Lifecycle;
import dev.galacticraft.api.rocket.part.*;
import dev.galacticraft.api.rocket.part.type.*;
import dev.galacticraft.impl.Constant;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class RocketRegistry {
    public static final ResourceKey<Registry<RocketConeType<?>>> ROCKET_CONE_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "rocket_cone_type"));
    public static final ResourceKey<Registry<RocketBodyType<?>>> ROCKET_BODY_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "rocket_body_type"));
    public static final ResourceKey<Registry<RocketFinType<?>>> ROCKET_FIN_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "rocket_fin_type"));
    public static final ResourceKey<Registry<RocketBoosterType<?>>> ROCKET_BOOSTER_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "rocket_booster_type"));
    public static final ResourceKey<Registry<RocketBottomType<?>>> ROCKET_BOTTOM_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "rocket_bottom_type"));
    public static final ResourceKey<Registry<RocketUpgradeType<?>>> ROCKET_UPGRADE_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "rocket_upgrade_type"));
    public static final ResourceKey<Registry<ConfiguredRocketCone<?, ?>>> CONFIGURED_ROCKET_CONE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "configured_rocket_cone"));
    public static final ResourceKey<Registry<ConfiguredRocketBody<?, ?>>> CONFIGURED_ROCKET_BODY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "configured_rocket_body"));
    public static final ResourceKey<Registry<ConfiguredRocketFin<?, ?>>> CONFIGURED_ROCKET_FIN_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "configured_rocket_fin"));
    public static final ResourceKey<Registry<ConfiguredRocketBooster<?, ?>>> CONFIGURED_ROCKET_BOOSTER_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "configured_rocket_booster"));
    public static final ResourceKey<Registry<ConfiguredRocketBottom<?, ?>>> CONFIGURED_ROCKET_BOTTOM_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "configured_rocket_bottom"));
    public static final ResourceKey<Registry<ConfiguredRocketUpgrade<?, ?>>> CONFIGURED_ROCKET_UPGRADE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "configured_rocket_upgrade"));

    public static final WritableRegistry<RocketConeType<?>> ROCKET_CONE_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Constant.Misc.INVALID.toString(),
                    ROCKET_CONE_TYPE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<RocketBodyType<?>> ROCKET_BODY_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Constant.Misc.INVALID.toString(),
                    ROCKET_BODY_TYPE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<RocketFinType<?>> ROCKET_FIN_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Constant.Misc.INVALID.toString(),
                    ROCKET_FIN_TYPE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<RocketBoosterType<?>> ROCKET_BOOSTER_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Constant.Misc.INVALID.toString(),
                    ROCKET_BOOSTER_TYPE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<RocketBottomType<?>> ROCKET_BOTTOM_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Constant.Misc.INVALID.toString(),
                    ROCKET_BOTTOM_TYPE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<RocketUpgradeType<?>> ROCKET_UPGRADE_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Constant.Misc.INVALID.toString(),
                    ROCKET_UPGRADE_TYPE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();
    
    public static final WritableRegistry<ConfiguredRocketCone<?, ?>> CONFIGURED_ROCKET_CONE = FabricRegistryBuilder.from(
            new MappedRegistry<>(CONFIGURED_ROCKET_CONE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<ConfiguredRocketBody<?, ?>> CONFIGURED_ROCKET_BODY = FabricRegistryBuilder.from(
            new MappedRegistry<>(CONFIGURED_ROCKET_BODY_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<ConfiguredRocketFin<?, ?>> CONFIGURED_ROCKET_FIN = FabricRegistryBuilder.from(
            new MappedRegistry<>(CONFIGURED_ROCKET_FIN_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<ConfiguredRocketBooster<?, ?>> CONFIGURED_ROCKET_BOOSTER = FabricRegistryBuilder.from(
            new MappedRegistry<>(CONFIGURED_ROCKET_BOOSTER_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<ConfiguredRocketBottom<?, ?>> CONFIGURED_ROCKET_BOTTOM = FabricRegistryBuilder.from(
            new MappedRegistry<>(CONFIGURED_ROCKET_BOTTOM_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static final WritableRegistry<ConfiguredRocketUpgrade<?, ?>> CONFIGURED_ROCKET_UPGRADE = FabricRegistryBuilder.from(
            new MappedRegistry<>(CONFIGURED_ROCKET_UPGRADE_KEY,
                    Lifecycle.experimental(),
                    null
            )).buildAndRegister();

    public static void initialize() {}

    static {
        Registry.register(ROCKET_CONE_TYPE, Constant.Misc.INVALID, RocketConeType.INVALID);
        Registry.register(ROCKET_BODY_TYPE, Constant.Misc.INVALID, RocketBodyType.INVALID);
        Registry.register(ROCKET_FIN_TYPE, Constant.Misc.INVALID, RocketFinType.INVALID);
        Registry.register(ROCKET_BOOSTER_TYPE, Constant.Misc.INVALID, RocketBoosterType.INVALID);
        Registry.register(ROCKET_BOTTOM_TYPE, Constant.Misc.INVALID, RocketBottomType.INVALID);
        Registry.register(ROCKET_UPGRADE_TYPE, Constant.Misc.INVALID, RocketUpgradeType.INVALID);
    }
}
