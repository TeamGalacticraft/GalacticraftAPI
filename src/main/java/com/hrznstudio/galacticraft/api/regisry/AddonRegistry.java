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

package com.hrznstudio.galacticraft.api.regisry;

import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.celestialbodies.SolarSystemType;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

public abstract class AddonRegistry<T> extends Registry<T> {
    public static final RegistryKey<Registry<AtmosphericGas>> ATMOSPHERIC_GAS_KEY = RegistryKey.ofRegistry(new Identifier("galacticraft-api", "atmospheric_gases"));
    public static final MutableRegistry<AtmosphericGas> ATMOSPHERIC_GASES = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(AtmosphericGas.OXYGEN.getId().toString(),
                    ATMOSPHERIC_GAS_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<SolarSystemType>> SOLAR_SYSTEM_TYPE_KEY = RegistryKey.ofRegistry(new Identifier("galacticraft-api", "solar_systems"));
    public static final MutableRegistry<SolarSystemType> SOLAR_SYSTEMS = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(SolarSystemType.SOL.getId().toString(),
                    SOLAR_SYSTEM_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<CelestialBodyType>> CELESTIAL_BODY_TYPE_KEY = RegistryKey.ofRegistry(new Identifier("galacticraft-api", "celestial_bodies"));
    public static final MutableRegistry<CelestialBodyType> CELESTIAL_BODIES = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(CelestialBodyType.THE_SUN.getId().toString(),
                    CELESTIAL_BODY_TYPE_KEY, Lifecycle.experimental())).buildAndRegister();

    public static final RegistryKey<Registry<Permission>> PERMISSIONS_KEY = RegistryKey.ofRegistry(new Identifier("galacticraft-api", "permissions"));
    public static final MutableRegistry<Permission> PERMISSIONS = FabricRegistryBuilder.from(
            new DefaultedRegistry<>(Permission.INVITE_PLAYER.getId().toString(),
                    PERMISSIONS_KEY, Lifecycle.experimental())).buildAndRegister();

    protected AddonRegistry(RegistryKey<Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    static {
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.HYDROGEN.getId(), AtmosphericGas.HYDROGEN);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.NITROGEN.getId(), AtmosphericGas.NITROGEN);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.OXYGEN.getId(), AtmosphericGas.OXYGEN);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.CARBON_DIOXIDE.getId(), AtmosphericGas.CARBON_DIOXIDE);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.WATER_VAPOR.getId(), AtmosphericGas.WATER_VAPOR);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.METHANE.getId(), AtmosphericGas.METHANE);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.HELIUM.getId(), AtmosphericGas.HELIUM);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.ARGON.getId(), AtmosphericGas.ARGON);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.NEON.getId(), AtmosphericGas.NEON);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.KRYPTON.getId(), AtmosphericGas.KRYPTON);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.NITROUS_OXIDE.getId(), AtmosphericGas.NITROUS_OXIDE);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.CARBON_MONOXIDE.getId(), AtmosphericGas.CARBON_MONOXIDE);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.XENON.getId(), AtmosphericGas.XENON);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.OZONE.getId(), AtmosphericGas.OZONE);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.NITROUS_DIOXIDE.getId(), AtmosphericGas.NITROUS_DIOXIDE);
        Registry.register(AddonRegistry.ATMOSPHERIC_GASES, AtmosphericGas.IODINE.getId(), AtmosphericGas.IODINE);

        Registry.register(AddonRegistry.SOLAR_SYSTEMS, SolarSystemType.SOL.getId(), SolarSystemType.SOL);

        Registry.register(AddonRegistry.CELESTIAL_BODIES, CelestialBodyType.THE_SUN.getId(), CelestialBodyType.THE_SUN);
        Registry.register(AddonRegistry.CELESTIAL_BODIES, CelestialBodyType.EARTH.getId(), CelestialBodyType.EARTH);

        Registry.register(AddonRegistry.PERMISSIONS, Permission.INVITE_PLAYER.getId(), Permission.INVITE_PLAYER);
        Registry.register(AddonRegistry.PERMISSIONS, Permission.MODIFY_COLOR.getId(), Permission.MODIFY_COLOR);
        Registry.register(AddonRegistry.PERMISSIONS, Permission.MODIFY_FLAG.getId(), Permission.MODIFY_FLAG);
        Registry.register(AddonRegistry.PERMISSIONS, Permission.MODIFY_NAME.getId(), Permission.MODIFY_NAME);
        Registry.register(AddonRegistry.PERMISSIONS, Permission.MODIFY_ROLES.getId(), Permission.MODIFY_ROLES);

    }
}
