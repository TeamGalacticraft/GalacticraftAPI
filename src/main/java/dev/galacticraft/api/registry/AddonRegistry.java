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
import dev.galacticraft.api.rocket.travelpredicate.TravelPredicateType;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.display.CelestialDisplayType;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPositionType;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.rocket.travelpredicate.type.AccessWeightPredicateType;
import dev.galacticraft.impl.rocket.travelpredicate.type.ConstantTravelPredicateType;
import dev.galacticraft.impl.universe.celestialbody.type.DecorativePlanet;
import dev.galacticraft.impl.universe.celestialbody.type.PlanetType;
import dev.galacticraft.impl.universe.celestialbody.type.StarType;
import dev.galacticraft.impl.universe.display.type.EmptyCelestialDisplayType;
import dev.galacticraft.impl.universe.display.type.IconCelestialDisplayType;
import dev.galacticraft.impl.universe.position.type.OrbitalCelestialPositionType;
import dev.galacticraft.impl.universe.position.type.StaticCelestialPositionType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class AddonRegistry {
    private AddonRegistry() {}

    public static final ResourceKey<Registry<TravelPredicateType<?>>> TRAVEL_PREDICATE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "travel_predicate"));
    public static final WritableRegistry<TravelPredicateType<?>> TRAVEL_PREDICATE = FabricRegistryBuilder.from(
            new MappedRegistry<>(TRAVEL_PREDICATE_KEY, Lifecycle.experimental(), TravelPredicateType::getReference)).buildAndRegister();

    public static final ResourceKey<Registry<CelestialPositionType<?>>> CELESTIAL_POSITION_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "celestial_position_type"));
    public static final WritableRegistry<CelestialPositionType<?>> CELESTIAL_POSITION_TYPE = FabricRegistryBuilder.from(
            new MappedRegistry<>(CELESTIAL_POSITION_TYPE_KEY, Lifecycle.experimental(), null)).buildAndRegister();

    public static final ResourceKey<Registry<CelestialDisplayType<?>>> CELESTIAL_DISPLAY_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "celestial_display_type"));
    public static final WritableRegistry<CelestialDisplayType<?>> CELESTIAL_DISPLAY_TYPE = FabricRegistryBuilder.from(
            new MappedRegistry<>(CELESTIAL_DISPLAY_TYPE_KEY, Lifecycle.experimental(), null)).buildAndRegister();

    public static final ResourceKey<Registry<Galaxy>> GALAXY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "galaxy"));
    public static final WritableRegistry<Galaxy> GALAXY = FabricRegistryBuilder.from(
            new MappedRegistry<>(GALAXY_KEY, Lifecycle.experimental(), null)).buildAndRegister();

    public static final ResourceKey<Registry<CelestialBodyType<?>>> CELESTIAL_BODY_TYPE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "celestial_body_type"));
    public static final WritableRegistry<CelestialBodyType<?>> CELESTIAL_BODY_TYPE = FabricRegistryBuilder.from(
            new MappedRegistry<>(CELESTIAL_BODY_TYPE_KEY, Lifecycle.experimental(), null)).buildAndRegister();

    public static final ResourceKey<Registry<CelestialBody<?, ?>>> CELESTIAL_BODY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Constant.MOD_ID, "celestial_body"));
    public static final WritableRegistry<CelestialBody<?, ?>> CELESTIAL_BODY = FabricRegistryBuilder.from(
            new MappedRegistry<>(CELESTIAL_BODY_KEY, Lifecycle.experimental(), null)).buildAndRegister();

    static {
        Registry.register(CELESTIAL_POSITION_TYPE, new ResourceLocation(Constant.MOD_ID, "static"), StaticCelestialPositionType.INSTANCE);
        Registry.register(CELESTIAL_POSITION_TYPE, new ResourceLocation(Constant.MOD_ID, "orbital"), OrbitalCelestialPositionType.INSTANCE);

        Registry.register(CELESTIAL_DISPLAY_TYPE, new ResourceLocation(Constant.MOD_ID, "empty"), EmptyCelestialDisplayType.INSTANCE);
        Registry.register(CELESTIAL_DISPLAY_TYPE, new ResourceLocation(Constant.MOD_ID, "icon"), IconCelestialDisplayType.INSTANCE);

        Registry.register(CELESTIAL_BODY_TYPE, new ResourceLocation(Constant.MOD_ID, "star"), StarType.INSTANCE);
        Registry.register(CELESTIAL_BODY_TYPE, new ResourceLocation(Constant.MOD_ID, "planet"), PlanetType.INSTANCE);
        Registry.register(CELESTIAL_BODY_TYPE, new ResourceLocation(Constant.MOD_ID, "decorative_planet"), DecorativePlanet.INSTANCE);

        Registry.register(TRAVEL_PREDICATE, new ResourceLocation(Constant.MOD_ID, "access_weight"), AccessWeightPredicateType.INSTANCE);
        Registry.register(TRAVEL_PREDICATE, new ResourceLocation(Constant.MOD_ID, "constant"), ConstantTravelPredicateType.INSTANCE);
    }
}
