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

import dev.galacticraft.api.gas.Gas;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.impl.Constant;
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
    public static final RegistryKey<Galaxy> MILKY_WAY_KEY = RegistryKey.of(AddonRegistry.GALAXY_KEY, new Identifier(Constant.MOD_ID, "milky_way"));
    public static final Galaxy MILKY_WAY = new Galaxy(
            new TranslatableText("galaxy.galacticraft-api.milky_way.name"),
            new TranslatableText("galaxy.galacticraft-api.milky_way.description"),
            StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
            EmptyCelestialDisplayType.INSTANCE.configure(EmptyCelestialDisplayConfig.INSTANCE)
    );

    public static final RegistryKey<CelestialBody<?, ?>> SOL_KEY = RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, new Identifier(Constant.MOD_ID, "sol"));
    public static final CelestialBody<StarConfig, ? extends CelestialBodyType<StarConfig>> SOL = StarType.INSTANCE.configure(
            new StarConfig(
                    new TranslatableText("star.galacticraft-api.sol.name"),
                    new TranslatableText("star.galacticraft-api.sol.description"),
                    MILKY_WAY_KEY,
                    StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
                    IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new Identifier(Constant.MOD_ID, "textures/body_icons.png"), 0, 0, 16, 16, 1.5f)),
                    new GasComposition.Builder()
                            .pressure(28)
                            .gas(Gas.HYDROGEN_ID,     734600.000)
                            .gas(Gas.HELIUM_ID,       248500.000)
                            .gas(Gas.OXYGEN_ID,         7700.000)
                            .gas(Gas.NEON_ID,           1200.000)
                            .gas(Gas.NITROGEN_ID,        900.000)
                            .build(),
                    28.0f,
                    1,
                    5772
            )
    );

    public static final RegistryKey<CelestialBody<?, ?>> EARTH_KEY = RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, new Identifier(Constant.MOD_ID, "earth"));
    public static final CelestialBody<PlanetConfig, ? extends CelestialBodyType<PlanetConfig>> EARTH = PlanetType.INSTANCE.configure(
            new PlanetConfig(
                    new TranslatableText("planet.galacticraft-api.earth.name"),
                    new TranslatableText("planet.galacticraft-api.earth.description"),
                    MILKY_WAY_KEY,
                    SOL_KEY,
                    OrbitalCelestialPositionType.INSTANCE.configure(new OrbitalCelestialPositionConfig(1536000.0, 1.0, 0.0F, true)),
                    IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new Identifier(Constant.MOD_ID, "textures/body_icons.png"), 0, 16, 16, 16, 1)),
                    World.OVERWORLD,
                    new GasComposition.Builder()
                            .pressure(1.0f)
                            .temperature(15.0f)
                            .gas(Gas.NITROGEN_ID,       780840.000  )
                            .gas(Gas.OXYGEN_ID,         209500.000  )
                            .gas(Gas.WATER_VAPOR_ID,     25000.000  )
                            .gas(Gas.ARGON_ID,            9300.000  )
                            .gas(Gas.CARBON_DIOXIDE_ID,    399.000  )
                            .gas(Gas.NEON_ID,               18.000  )
                            .gas(Gas.HELIUM_ID,              5.420  )
                            .gas(Gas.METHANE_ID,             1.790  )
                            .gas(Gas.KRYPTON_ID,             1.140  )
                            .gas(Gas.HYDROGEN_ID,            0.550  )
                            .gas(Gas.NITROUS_OXIDE_ID,       0.325  )
                            .gas(Gas.CARBON_MONOXIDE_ID,     0.100  )
                            .gas(Gas.XENON_ID,               0.090  )
                            .gas(Gas.OZONE_ID,               0.070  )
                            .gas(Gas.NITROUS_DIOXIDE_ID,     0.020  )
                            .gas(Gas.IODINE_ID,              0.010  )
                            .build(),
                    1.0f,
                    0,
                    21, //todo
                    15, //todo
                    Optional.empty() //todo
            )
    );

    public static void register() {
        Registry.register(AddonRegistry.CELESTIAL_POSITION_TYPE, new Identifier(Constant.MOD_ID, "static"), StaticCelestialPositionType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_POSITION_TYPE, new Identifier(Constant.MOD_ID, "orbital"), OrbitalCelestialPositionType.INSTANCE);

        Registry.register(AddonRegistry.CELESTIAL_DISPLAY_TYPE, new Identifier(Constant.MOD_ID, "empty"), EmptyCelestialDisplayType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_DISPLAY_TYPE, new Identifier(Constant.MOD_ID, "icon"), IconCelestialDisplayType.INSTANCE);

        Registry.register(AddonRegistry.GALAXY, MILKY_WAY_KEY.getValue(), MILKY_WAY);

        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new Identifier(Constant.MOD_ID, "star"), StarType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new Identifier(Constant.MOD_ID, "planet"), PlanetType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new Identifier(Constant.MOD_ID, "decorative_planet"), DecorativePlanet.INSTANCE);

        Registry.register(AddonRegistry.CELESTIAL_BODY, SOL_KEY.getValue(), SOL);
        Registry.register(AddonRegistry.CELESTIAL_BODY, EARTH_KEY.getValue(), EARTH);
    }
}
