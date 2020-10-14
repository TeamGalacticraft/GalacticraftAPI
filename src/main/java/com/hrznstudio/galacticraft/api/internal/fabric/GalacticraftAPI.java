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

package com.hrznstudio.galacticraft.api.internal.fabric;

import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.event.AtmosphericGasRegistryCallback;
import com.hrznstudio.galacticraft.api.event.CelestialBodyRegistryCallback;
import com.hrznstudio.galacticraft.api.event.SpaceRaceTeamPermissionRegistryCallback;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GalacticraftAPI implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        long startInitTime = System.currentTimeMillis();
        LOGGER.info("Initializing...");
        // register our things
        AtmosphericGasRegistryCallback.EVENT.register(registry -> {
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
        CelestialBodyRegistryCallback.EVENT.register(registry -> {
            Registry.register(registry, CelestialBodyType.THE_SUN.getId(), CelestialBodyType.THE_SUN);
            Registry.register(registry, CelestialBodyType.EARTH.getId(), CelestialBodyType.EARTH);
        });
        SpaceRaceTeamPermissionRegistryCallback.EVENT.register(registry -> {
            Registry.register(registry, Permission.MODIFY_COLOR.getIdentifier(), Permission.MODIFY_COLOR);
            Registry.register(registry, Permission.MODIFY_FLAG.getIdentifier(), Permission.MODIFY_FLAG);
            Registry.register(registry, Permission.MODIFY_NAME.getIdentifier(), Permission.MODIFY_NAME);
            Registry.register(registry, Permission.MODIFY_ROLES.getIdentifier(), Permission.MODIFY_ROLES);
        });
        LOGGER.info("[GC-API] Initialization Complete. (Took {}ms).", System.currentTimeMillis()-startInitTime);
    }
}
