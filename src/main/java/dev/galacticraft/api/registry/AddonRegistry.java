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
import dev.galacticraft.api.event.RegistrationEvent;
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
    public static final RegistryKey<Registry<AtmosphericGas>> ATMOSPHERIC_GAS_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "atmospheric_gases"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#ATMOSPHERIC_GAS
     */
    @ApiStatus.Internal
    public static final MutableRegistry<AtmosphericGas> ATMOSPHERIC_GAS = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(AtmosphericGas.OXYGEN.getId().toString(),
                    ATMOSPHERIC_GAS_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<SolarSystemType>> SOLAR_SYSTEM_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "solar_systems"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#SOLAR_SYSTEM_TYPE
     */
    @ApiStatus.Internal
    public static final MutableRegistry<SolarSystemType> SOLAR_SYSTEM_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(SolarSystemType.SOL.getId().toString(),
                    SOLAR_SYSTEM_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "celestial_bodies"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#CELESTIAL_BODY_TYPE
     */
    @ApiStatus.Internal
    public static final MutableRegistry<CelestialBodyType> CELESTIAL_BODY_TYPE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(CelestialBodyType.THE_SUN.getId().toString(),
                    CELESTIAL_BODY_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<Permission>> PERMISSION_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "permissions"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#PERMISSION
     */
    @ApiStatus.Internal
    public static final MutableRegistry<Permission> PERMISSION = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Permission.INVITE_PLAYER.getId().toString(),
                    PERMISSION_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<TravelPredicateType<?>>> TRAVEL_PREDICATE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "travel_predicate"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#TRAVEL_PREDICATE
     */
    @ApiStatus.Internal
    public static final MutableRegistry<TravelPredicateType<?>> TRAVEL_PREDICATE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(GalacticraftAPI.MOD_ID, "constant").toString(),
                    TRAVEL_PREDICATE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<ConfiguredTravelPredicate<?>>> CONFIGURED_TRAVEL_PREDICATE_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "configured_travel_predicate"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#CONFIGURED_TRAVEL_PREDICATE
     */
    @ApiStatus.Internal
    public static final MutableRegistry<ConfiguredTravelPredicate<?>> CONFIGURED_TRAVEL_PREDICATE = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(GalacticraftAPI.MOD_ID, "never").toString(),
                    CONFIGURED_TRAVEL_PREDICATE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<RocketPart>> ROCKET_PART_KEY = RegistryKey.ofRegistry(new Identifier(GalacticraftAPI.MOD_ID, "rocket_parts"));

    /**
     * You should use the dynamic registry manager to get an instance of the registry
     * To register entries use the proper {@link RegistrationEvent event}
     *
     * @see net.minecraft.util.registry.DynamicRegistryManager
     * @see net.minecraft.world.World#getRegistryManager()
     * @see RegistrationEvent#ROCKET_PART
     */
    @ApiStatus.Internal
    public static final MutableRegistry<RocketPart> ROCKET_PART = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(new Identifier(GalacticraftAPI.MOD_ID, "invalid").toString(),
                    ROCKET_PART_KEY, Lifecycle.experimental())).buildAndRegister();

    static {
        RegistrationEvent.ATMOSPHERIC_GAS.register(registry -> {
            Registry.register(registry, AtmosphericGas.HYDROGEN.getId(), AtmosphericGas.HYDROGEN);
            Registry.register(registry, AtmosphericGas.NITROGEN.getId(), AtmosphericGas.NITROGEN);
            Registry.register(registry, AtmosphericGas.OXYGEN.getId(), AtmosphericGas.OXYGEN);
            Registry.register(registry, AtmosphericGas.CARBON_DIOXIDE.getId(), AtmosphericGas.CARBON_DIOXIDE);
            Registry.register(registry, AtmosphericGas.WATER_VAPOR.getId(), AtmosphericGas.WATER_VAPOR);
            Registry.register(registry, AtmosphericGas.METHANE.getId(), AtmosphericGas.METHANE);
            Registry.register(registry, AtmosphericGas.HELIUM.getId(), AtmosphericGas.HELIUM);
            Registry.register(registry, AtmosphericGas.ARGON.getId(), AtmosphericGas.ARGON);
            Registry.register(registry, AtmosphericGas.NEON.getId(), AtmosphericGas.NEON);
            Registry.register(registry, AtmosphericGas.KRYPTON.getId(), AtmosphericGas.KRYPTON);
            Registry.register(registry, AtmosphericGas.NITROUS_OXIDE.getId(), AtmosphericGas.NITROUS_OXIDE);
            Registry.register(registry, AtmosphericGas.CARBON_MONOXIDE.getId(), AtmosphericGas.CARBON_MONOXIDE);
            Registry.register(registry, AtmosphericGas.XENON.getId(), AtmosphericGas.XENON);
            Registry.register(registry, AtmosphericGas.OZONE.getId(), AtmosphericGas.OZONE);
            Registry.register(registry, AtmosphericGas.NITROUS_DIOXIDE.getId(), AtmosphericGas.NITROUS_DIOXIDE);
            Registry.register(registry, AtmosphericGas.IODINE.getId(), AtmosphericGas.IODINE);
        });

        RegistrationEvent.SOLAR_SYSTEM_TYPE.register(registry -> Registry.register(registry, SolarSystemType.SOL.getId(), SolarSystemType.SOL));

        RegistrationEvent.CELESTIAL_BODY_TYPE.register(registry -> {
            Registry.register(registry, CelestialBodyType.THE_SUN.getId(), CelestialBodyType.THE_SUN);
            Registry.register(registry, CelestialBodyType.EARTH.getId(), CelestialBodyType.EARTH);
        });

        RegistrationEvent.PERMISSION.register(registry -> {
            Registry.register(registry, Permission.INVITE_PLAYER.getId(), Permission.INVITE_PLAYER);
            Registry.register(registry, Permission.MODIFY_COLOR.getId(), Permission.MODIFY_COLOR);
            Registry.register(registry, Permission.MODIFY_FLAG.getId(), Permission.MODIFY_FLAG);
            Registry.register(registry, Permission.MODIFY_NAME.getId(), Permission.MODIFY_NAME);
            Registry.register(registry, Permission.MODIFY_ROLES.getId(), Permission.MODIFY_ROLES);
            Registry.register(registry, Permission.ACCESS_SPACE_STATION.getId(), Permission.ACCESS_SPACE_STATION);
        });

        RegistrationEvent.TRAVEL_PREDICATE.register(registry -> {
            Registry.register(registry, new Identifier(GalacticraftAPI.MOD_ID, "access_weight"), AccessWeightPredicateType.INSTANCE);
            Registry.register(registry, new Identifier(GalacticraftAPI.MOD_ID, "constant"), ConstantTravelPredicateType.INSTANCE);
        });

        RegistrationEvent.CONFIGURED_TRAVEL_PREDICATE.register(registry -> {
            Registry.register(registry, new Identifier(GalacticraftAPI.MOD_ID, "always"), ConfiguredTravelPredicate.ALWAYS);
            Registry.register(registry, new Identifier(GalacticraftAPI.MOD_ID, "pass"), ConfiguredTravelPredicate.PASS);
            Registry.register(registry, new Identifier(GalacticraftAPI.MOD_ID, "never"), ConfiguredTravelPredicate.NEVER);
        });

        RegistrationEvent.ROCKET_PART.register(registry -> Registry.register(registry, RocketPart.INVALID.getId(), RocketPart.INVALID));
    }

    public static void invokeEvents() {
        RegistrationEvent.ATMOSPHERIC_GAS.invoker().register(ATMOSPHERIC_GAS);
        RegistrationEvent.SOLAR_SYSTEM_TYPE.invoker().register(SOLAR_SYSTEM_TYPE);
        RegistrationEvent.CELESTIAL_BODY_TYPE.invoker().register(CELESTIAL_BODY_TYPE);
        RegistrationEvent.PERMISSION.invoker().register(PERMISSION);
        RegistrationEvent.ROCKET_PART.invoker().register(ROCKET_PART);
    }
}
