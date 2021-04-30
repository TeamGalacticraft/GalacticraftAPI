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

package dev.galacticraft.api.registry;

import dev.galacticraft.api.atmosphere.AtmosphericGas;
import dev.galacticraft.api.celestialbody.CelestialBodyType;
import dev.galacticraft.api.celestialbody.SolarSystemType;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.rocket.part.travel.AccessWeightPredicateType;
import dev.galacticraft.api.rocket.part.travel.ConfiguredTravelPredicate;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.part.travel.ConstantTravelPredicateType;
import dev.galacticraft.api.rocket.part.travel.TravelPredicateType;
import dev.galacticraft.api.teams.data.Permission;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.ApiStatus;

public class AddonRegistry {
    public static final RegistryKey<Registry<AtmosphericGas>> ATMOSPHERIC_GAS_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "atmospheric_gas"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    @ApiStatus.Internal
    public static final MutableRegistry<AtmosphericGas> ATMOSPHERIC_GAS = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(AtmosphericGas.OXYGEN.getId().toString(),
                    ATMOSPHERIC_GAS_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<SolarSystemType>> SOLAR_SYSTEM_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "solar_system"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    @ApiStatus.Internal
    public static final MutableRegistry<SolarSystemType> SOLAR_SYSTEM_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(SolarSystemType.SOL.getId().toString(),
                    SOLAR_SYSTEM_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "celestial_body"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    @ApiStatus.Internal
    public static final MutableRegistry<CelestialBodyType> CELESTIAL_BODY_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(CelestialBodyType.THE_SUN.getId().toString(),
                    CELESTIAL_BODY_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<Permission>> PERMISSION_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "permission"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    public static final MutableRegistry<Permission> PERMISSION = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Permission.INVITE_PLAYER.getId().toString(),
                    PERMISSION_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<TravelPredicateType<?>>> TRAVEL_PREDICATE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "travel_predicate"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    public static final MutableRegistry<TravelPredicateType<?>> TRAVEL_PREDICATE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(GalacticraftAPI.MOD_ID, "constant").toString(),
                    TRAVEL_PREDICATE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<ConfiguredTravelPredicate<?>>> CONFIGURED_TRAVEL_PREDICATE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "configured_travel_predicate"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    public static final MutableRegistry<ConfiguredTravelPredicate<?>> CONFIGURED_TRAVEL_PREDICATE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(GalacticraftAPI.MOD_ID, "never").toString(),
                    CONFIGURED_TRAVEL_PREDICATE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<RocketPart>> ROCKET_PART_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "rocket_part"));

    /**
     * When accessing values of the registry in-world use the {@link net.minecraft.util.registry.DynamicRegistryManager dynamic registry manager}.
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     */
    public static final MutableRegistry<RocketPart> ROCKET_PART = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(GalacticraftAPI.MOD_ID, "invalid").toString(),
                    ROCKET_PART_KEY, Lifecycle.experimental())).buildAndRegister();

    static {
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.HYDROGEN.getId(), AtmosphericGas.HYDROGEN);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.NITROGEN.getId(), AtmosphericGas.NITROGEN);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.OXYGEN.getId(), AtmosphericGas.OXYGEN);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.CARBON_DIOXIDE.getId(), AtmosphericGas.CARBON_DIOXIDE);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.WATER_VAPOR.getId(), AtmosphericGas.WATER_VAPOR);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.METHANE.getId(), AtmosphericGas.METHANE);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.HELIUM.getId(), AtmosphericGas.HELIUM);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.ARGON.getId(), AtmosphericGas.ARGON);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.NEON.getId(), AtmosphericGas.NEON);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.KRYPTON.getId(), AtmosphericGas.KRYPTON);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.NITROUS_OXIDE.getId(), AtmosphericGas.NITROUS_OXIDE);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.CARBON_MONOXIDE.getId(), AtmosphericGas.CARBON_MONOXIDE);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.XENON.getId(), AtmosphericGas.XENON);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.OZONE.getId(), AtmosphericGas.OZONE);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.NITROUS_DIOXIDE.getId(), AtmosphericGas.NITROUS_DIOXIDE);
        Registry.register(ATMOSPHERIC_GAS, AtmosphericGas.IODINE.getId(), AtmosphericGas.IODINE);

        Registry.register(SOLAR_SYSTEM_TYPE, SolarSystemType.SOL.getId(), SolarSystemType.SOL);

        Registry.register(CELESTIAL_BODY_TYPE, CelestialBodyType.THE_SUN.getId(), CelestialBodyType.THE_SUN);
        Registry.register(CELESTIAL_BODY_TYPE, CelestialBodyType.EARTH.getId(), CelestialBodyType.EARTH);

        Registry.register(PERMISSION, Permission.INVITE_PLAYER.getId(), Permission.INVITE_PLAYER);
        Registry.register(PERMISSION, Permission.MODIFY_COLOR.getId(), Permission.MODIFY_COLOR);
        Registry.register(PERMISSION, Permission.MODIFY_FLAG.getId(), Permission.MODIFY_FLAG);
        Registry.register(PERMISSION, Permission.MODIFY_NAME.getId(), Permission.MODIFY_NAME);
        Registry.register(PERMISSION, Permission.MODIFY_ROLES.getId(), Permission.MODIFY_ROLES);
        Registry.register(PERMISSION, Permission.ACCESS_SPACE_STATION.getId(), Permission.ACCESS_SPACE_STATION);

        Registry.register(TRAVEL_PREDICATE, new Identifier(GalacticraftAPI.MOD_ID, "access_weight"), AccessWeightPredicateType.INSTANCE);
        Registry.register(TRAVEL_PREDICATE, new Identifier(GalacticraftAPI.MOD_ID, "constant"), ConstantTravelPredicateType.INSTANCE);

        Registry.register(CONFIGURED_TRAVEL_PREDICATE, new Identifier(GalacticraftAPI.MOD_ID, "always"), ConfiguredTravelPredicate.ALWAYS);
        Registry.register(CONFIGURED_TRAVEL_PREDICATE, new Identifier(GalacticraftAPI.MOD_ID, "pass"), ConfiguredTravelPredicate.PASS);
        Registry.register(CONFIGURED_TRAVEL_PREDICATE, new Identifier(GalacticraftAPI.MOD_ID, "never"), ConfiguredTravelPredicate.NEVER);

        Registry.register(ROCKET_PART, RocketPart.INVALID.getId(), RocketPart.INVALID);
    }
}
