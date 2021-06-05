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

package dev.galacticraft.impl.universe;

import dev.galacticraft.api.atmosphere.AtmosphericGas;
import dev.galacticraft.api.atmosphere.AtmosphericInfo;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.impl.internal.fabric.GalacticraftAPI;
import dev.galacticraft.impl.universe.celestialbody.config.PlanetConfig;
import dev.galacticraft.impl.universe.celestialbody.config.StarConfig;
import dev.galacticraft.impl.universe.celestialbody.type.DecorativePlanet;
import dev.galacticraft.impl.universe.celestialbody.type.PlanetType;
import dev.galacticraft.impl.universe.celestialbody.type.StarType;
import dev.galacticraft.impl.universe.display.config.EmptyCelestialDisplayConfig;
import dev.galacticraft.impl.universe.display.config.IconCelestialDisplayConfig;
import dev.galacticraft.impl.universe.display.type.EmptyCelestialDisplayType;
import dev.galacticraft.impl.universe.display.type.IconCelestialDisplayType;
import dev.galacticraft.impl.universe.position.config.OrbitalCelestialPositionConfig;
import dev.galacticraft.impl.universe.position.config.StaticCelestialPositionConfig;
import dev.galacticraft.impl.universe.position.type.OrbitalCelestialPositionType;
import dev.galacticraft.impl.universe.position.type.StaticCelestialPositionType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Optional;

public class BuiltinObjects {
    public static final RegistryKey<Galaxy> MILKY_WAY_KEY = RegistryKey.of(AddonRegistry.GALAXY_KEY, new Identifier(GalacticraftAPI.MOD_ID, "milky_way"));
    public static final Galaxy MILKY_WAY = new Galaxy(
            new TranslatableText("galaxy.galacticraft-api.milky_way.name"),
            new TranslatableText("galaxy.galacticraft-api.milky_way.description"),
            StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
            EmptyCelestialDisplayType.INSTANCE.configure(EmptyCelestialDisplayConfig.INSTANCE)
    );

    public static final RegistryKey<CelestialBody<?, ?>> SOL_KEY = RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, new Identifier(GalacticraftAPI.MOD_ID, "sol"));
    public static final CelestialBody<StarConfig, ? extends CelestialBodyType<StarConfig>> SOL = StarType.INSTANCE.configure(
            new StarConfig(
                    new TranslatableText("star.galacticraft-api.sol.name"),
                    new TranslatableText("star.galacticraft-api.sol.description"),
                    MILKY_WAY_KEY,
                    StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
                    IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new Identifier(GalacticraftAPI.MOD_ID, "body_icons"), 0, 0, 16, 16, 1)),
                    1,
                    5500
            )
    );

    public static final RegistryKey<CelestialBody<?, ?>> EARTH_KEY = RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, new Identifier(GalacticraftAPI.MOD_ID, "earth"));
    public static final CelestialBody<PlanetConfig, ? extends CelestialBodyType<PlanetConfig>> EARTH = PlanetType.INSTANCE.configure(
            new PlanetConfig(
                    new TranslatableText("planet.galacticraft-api.earth.name"),
                    new TranslatableText("planet.galacticraft-api.earth.description"),
                    MILKY_WAY_KEY,
                    SOL_KEY,
                    OrbitalCelestialPositionType.INSTANCE.configure(new OrbitalCelestialPositionConfig(1536000.0, 1.0, true)),
                    IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new Identifier(GalacticraftAPI.MOD_ID, "body_icons"), 0, 16, 16, 16, 1)),
                    World.OVERWORLD,
                    new AtmosphericInfo.Builder()
                            .pressure(1.0f)
                            .temperature(15.0f)
                            .gas(AtmosphericGas.NITROGEN,       780840d     )
                            .gas(AtmosphericGas.OXYGEN,         209500d     )
                            .gas(AtmosphericGas.WATER_VAPOR,     25000d     )
                            .gas(AtmosphericGas.ARGON,            9300d     )
                            .gas(AtmosphericGas.CARBON_DIOXIDE,    399d     )
                            .gas(AtmosphericGas.NEON,               18d     )
                            .gas(AtmosphericGas.HELIUM,              5.42d  )
                            .gas(AtmosphericGas.METHANE,             1.79d  )
                            .gas(AtmosphericGas.KRYPTON,             1.14d  )
                            .gas(AtmosphericGas.HYDROGEN,            0.55d  )
                            .gas(AtmosphericGas.NITROUS_OXIDE,       0.325d )
                            .gas(AtmosphericGas.CARBON_MONOXIDE,     0.1d   )
                            .gas(AtmosphericGas.XENON,               0.09d  )
                            .gas(AtmosphericGas.OZONE,               0.07d  )
                            .gas(AtmosphericGas.IODINE,              0.01d  )
                            .gas(AtmosphericGas.NITROUS_DIOXIDE,     0.02d  )
                            .build(),
                    1.0f,
                    0,
                    Optional.empty() //todo
            )
    );

    public static void register() {
        Registry.register(AddonRegistry.CELESTIAL_POSITION_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "static"), StaticCelestialPositionType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_POSITION_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "orbital"), OrbitalCelestialPositionType.INSTANCE);

        Registry.register(AddonRegistry.CELESTIAL_DISPLAY_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "empty"), EmptyCelestialDisplayType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_DISPLAY_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "icon"), IconCelestialDisplayType.INSTANCE);

        Registry.register(AddonRegistry.GALAXY, MILKY_WAY_KEY.getValue(), MILKY_WAY);

        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "star"), StarType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "planet"), PlanetType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new Identifier(GalacticraftAPI.MOD_ID, "decorative_planet"), DecorativePlanet.INSTANCE);

        Registry.register(AddonRegistry.CELESTIAL_BODY, SOL_KEY.getValue(), SOL);
        Registry.register(AddonRegistry.CELESTIAL_BODY, EARTH_KEY.getValue(), EARTH);
    }
}
