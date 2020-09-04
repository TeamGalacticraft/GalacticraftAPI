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
import com.hrznstudio.galacticraft.api.event.AtmosphericGasRegistryCallback;
import com.hrznstudio.galacticraft.api.event.CelestialBodyRegistryCallback;
import com.hrznstudio.galacticraft.api.event.SpaceRaceTeamPermissionRegistryCallback;
import com.hrznstudio.galacticraft.api.internal.fabric.GalacticraftAPI;
import com.hrznstudio.galacticraft.api.internal.mixin.RegistryAccessor;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

public abstract class AddonRegistry<T> extends Registry<T> {
    public static final Registry<CelestialBodyType> CELESTIAL_BODIES = RegistryAccessor.callCreate(
            RegistryKey.ofRegistry(new Identifier("galacticraft-api", "celestial_bodies")),
            Lifecycle.stable(),
            () -> CelestialBodyType.THE_SUN);

    public static final Registry<AtmosphericGas> ATMOSPHERIC_GASES = RegistryAccessor.callCreate(
            RegistryKey.ofRegistry(new Identifier("galacticraft-api", "atmospheric_gases")),
            Lifecycle.stable(),
            () -> AtmosphericGas.OXYGEN);

    public static final Registry<Permission> PERMISSIONS = RegistryAccessor.callCreate(
            RegistryKey.ofRegistry(new Identifier("galacticraft-api", "permissions")),
            Lifecycle.stable(),
            () -> Permission.INVITE_PLAYER);

    protected AddonRegistry(RegistryKey<Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    public static void runRegistryCallbacks() {
        SpaceRaceTeamPermissionRegistryCallback.EVENT.invoker().register(AddonRegistry.PERMISSIONS);
        AtmosphericGasRegistryCallback.EVENT.invoker().register(AddonRegistry.ATMOSPHERIC_GASES);
        CelestialBodyRegistryCallback.EVENT.invoker().register(AddonRegistry.CELESTIAL_BODIES);

        GalacticraftAPI.LOGGER.info("[GC-API] Loaded {} Celestial Bodies", AddonRegistry.CELESTIAL_BODIES.getIds().size());
        GalacticraftAPI.LOGGER.info("[GC-API] Loaded {} Atmospheric Gases", AddonRegistry.ATMOSPHERIC_GASES.getIds().size());
        GalacticraftAPI.LOGGER.info("[GC-API] Loaded {} Team Permissions", AddonRegistry.PERMISSIONS.getIds().size());
    }
}
