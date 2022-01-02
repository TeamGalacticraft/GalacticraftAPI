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
import dev.galacticraft.api.gas.Gas;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.travelpredicate.TravelPredicateType;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.display.CelestialDisplayType;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPositionType;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.rocket.travelpredicate.type.AccessWeightPredicateType;
import dev.galacticraft.impl.rocket.travelpredicate.type.ConstantTravelPredicateType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AddonRegistry {
    public static final RegistryKey<Registry<Gas>> GAS_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "gas"));
    /**
     * <b>Warning</b>: Do not use this registry to obtain instances or ids of objects when in-world; use the {@link net.minecraft.util.registry.DynamicRegistryManager Dynamic Registry Manager} instead.
     * <i>This registry will not contain entries obtained via datapack</i>.
     */
    public static final MutableRegistry<Gas> GAS = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "oxygen").toString(),
                    GAS_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<TravelPredicateType<?>>> TRAVEL_PREDICATE_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "travel_predicate"));
    public static final MutableRegistry<TravelPredicateType<?>> TRAVEL_PREDICATE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "constant").toString(),
                    TRAVEL_PREDICATE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialPositionType<?>>> CELESTIAL_POSITION_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "celestial_position_type"));
    public static final MutableRegistry<CelestialPositionType<?>> CELESTIAL_POSITION_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "static").toString(),
                    CELESTIAL_POSITION_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialDisplayType<?>>> CELESTIAL_DISPLAY_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "celestial_display_type"));
    public static final MutableRegistry<CelestialDisplayType<?>> CELESTIAL_DISPLAY_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "empty").toString(),
                    CELESTIAL_DISPLAY_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<Galaxy>> GALAXY_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "galaxy"));
    /**
     * <b>Warning</b>: Do not use this registry to obtain instances or ids of objects when in-world; use the {@link net.minecraft.util.registry.DynamicRegistryManager Dynamic Registry Manager} instead.
     * <i>This registry will not contain entries obtained via datapack</i>.
     */
    public static final MutableRegistry<Galaxy> GALAXY = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "milky_way").toString(),
                    GALAXY_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialBodyType<?>>> CELESTIAL_BODY_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "celestial_body_type"));
    public static final MutableRegistry<CelestialBodyType<?>> CELESTIAL_BODY_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "star").toString(),
                    CELESTIAL_BODY_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialBody<?, ?>>> CELESTIAL_BODY_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "celestial_body"));
    /**
     * <b>Warning</b>: Do not use this registry to obtain instances or ids of objects when in-world; use the {@link net.minecraft.util.registry.DynamicRegistryManager Dynamic Registry Manager} instead.
     * <i>This registry will not contain entries obtained via datapack</i>.
     */
    public static final MutableRegistry<CelestialBody<?, ?>> CELESTIAL_BODY = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "sol").toString(),
                    CELESTIAL_BODY_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<RocketPart>> ROCKET_PART_KEY = RegistryKey.ofRegistry(new Identifier(Constant.MOD_ID, "rocket_part"));
    /**
     * <b>Warning</b>: Do not use this registry to obtain instances or ids of objects when in-world; use the {@link net.minecraft.util.registry.DynamicRegistryManager Dynamic Registry Manager} instead.
     * <i>This registry will not contain entries obtained via datapack</i>.
     */
    public static final MutableRegistry<RocketPart> ROCKET_PART = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(Constant.MOD_ID, "invalid").toString(),
                    ROCKET_PART_KEY, Lifecycle.experimental())).buildAndRegister();

    static {
        Registry.register(GAS, Gas.HYDROGEN_ID, Gas.HYDROGEN);
        Registry.register(GAS, Gas.NITROGEN_ID, Gas.NITROGEN);
        Registry.register(GAS, Gas.OXYGEN_ID, Gas.OXYGEN);
        Registry.register(GAS, Gas.CARBON_DIOXIDE_ID, Gas.CARBON_DIOXIDE);
        Registry.register(GAS, Gas.WATER_VAPOR_ID, Gas.WATER_VAPOR);
        Registry.register(GAS, Gas.METHANE_ID, Gas.METHANE);
        Registry.register(GAS, Gas.HELIUM_ID, Gas.HELIUM);
        Registry.register(GAS, Gas.ARGON_ID, Gas.ARGON);
        Registry.register(GAS, Gas.NEON_ID, Gas.NEON);
        Registry.register(GAS, Gas.KRYPTON_ID, Gas.KRYPTON);
        Registry.register(GAS, Gas.NITROUS_OXIDE_ID, Gas.NITROUS_OXIDE);
        Registry.register(GAS, Gas.CARBON_MONOXIDE_ID, Gas.CARBON_MONOXIDE);
        Registry.register(GAS, Gas.XENON_ID, Gas.XENON);
        Registry.register(GAS, Gas.OZONE_ID, Gas.OZONE);
        Registry.register(GAS, Gas.NITROUS_DIOXIDE_ID, Gas.NITROUS_DIOXIDE);
        Registry.register(GAS, Gas.IODINE_ID, Gas.IODINE);

        Registry.register(TRAVEL_PREDICATE, new Identifier(Constant.MOD_ID, "access_weight"), AccessWeightPredicateType.INSTANCE);
        Registry.register(TRAVEL_PREDICATE, new Identifier(Constant.MOD_ID, "constant"), ConstantTravelPredicateType.INSTANCE);

        Registry.register(ROCKET_PART, new Identifier(Constant.MOD_ID, "invalid"), RocketPart.INVALID);
    }
}
