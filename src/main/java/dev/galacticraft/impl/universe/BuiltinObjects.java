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

package dev.galacticraft.impl.universe;

import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.gas.Gases;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.universe.celestialbody.config.PlanetConfig;
import dev.galacticraft.impl.universe.celestialbody.config.StarConfig;
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
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BuiltinObjects {
    public static final ResourceKey<Galaxy> MILKY_WAY_KEY = ResourceKey.create(AddonRegistry.GALAXY_KEY, new ResourceLocation(Constant.MOD_ID, "milky_way"));
    public static final Galaxy MILKY_WAY = Galaxy.create(
            Component.translatable("galaxy.galacticraft-api.milky_way.name"),
            Component.translatable("galaxy.galacticraft-api.milky_way.description"),
            StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
            EmptyCelestialDisplayType.INSTANCE.configure(EmptyCelestialDisplayConfig.INSTANCE)
    );

    public static final ResourceKey<CelestialBody<?, ?>> SOL_KEY = ResourceKey.create(AddonRegistry.CELESTIAL_BODY_KEY, new ResourceLocation(Constant.MOD_ID, "sol"));
    public static final CelestialBody<StarConfig, ? extends CelestialBodyType<StarConfig>> SOL = StarType.INSTANCE.configure(
            new StarConfig(
                    Component.translatable("star.galacticraft-api.sol.name"),
                    Component.translatable("star.galacticraft-api.sol.description"),
                    Optional.of(MILKY_WAY_KEY),
                    Optional.empty(),
                    StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
                    IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new ResourceLocation(Constant.MOD_ID, "textures/body_icons.png"), 0, 0, 16, 16, 1.5f)),
                    Optional.empty(),
                    new GasComposition.Builder()
                            .pressure(28)
                            .gas(Gases.HYDROGEN_ID, 734600.000)
                            .gas(Gases.HELIUM_ID, 248500.000)
                            .gas(Gases.OXYGEN_ID, 7700.000)
                            .gas(Gases.NEON_ID, 1200.000)
                            .gas(Gases.NITROGEN_ID, 900.000)
                            .build(),
                    28.0f,
                    1,
                    Optional.empty(),
                    5772,
                    Optional.empty()
            )
    );

    public static final ResourceKey<CelestialBody<?, ?>> EARTH_KEY = ResourceKey.create(AddonRegistry.CELESTIAL_BODY_KEY, new ResourceLocation(Constant.MOD_ID, "earth"));
    public static final CelestialBody<PlanetConfig, ? extends CelestialBodyType<PlanetConfig>> EARTH = PlanetType.INSTANCE.configure(
            new PlanetConfig(
                    Component.translatable("planet.galacticraft-api.earth.name"),
                    Component.translatable("planet.galacticraft-api.earth.description"),
                    Optional.of(MILKY_WAY_KEY),
                    Optional.of(SOL_KEY),
                    OrbitalCelestialPositionType.INSTANCE.configure(new OrbitalCelestialPositionConfig(1536000.0, 1.0, 0.0F, true)),
                    IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new ResourceLocation(Constant.MOD_ID, "textures/body_icons.png"), 0, 16, 16, 16, 1)),
                    Optional.of(Level.OVERWORLD),
                    new GasComposition.Builder()
                            .pressure(1.0f)
                            .temperature(15.0f)
                            .gas(Gases.NITROGEN_ID, 780840.000)
                            .gas(Gases.OXYGEN_ID, 209500.000)
                            .gas(Gases.WATER_VAPOR_ID, 25000.000)
                            .gas(Gases.ARGON_ID, 9300.000)
                            .gas(Gases.CARBON_DIOXIDE_ID, 399.000)
                            .gas(Gases.NEON_ID, 18.000)
                            .gas(Gases.HELIUM_ID, 5.420)
                            .gas(Gases.METHANE_ID, 1.790)
                            .gas(Gases.KRYPTON_ID, 1.140)
                            .gas(Gases.HYDROGEN_ID, 0.550)
                            .gas(Gases.NITROUS_OXIDE_ID, 0.325)
                            .gas(Gases.CARBON_MONOXIDE_ID, 0.100)
                            .gas(Gases.XENON_ID, 0.090)
                            .gas(Gases.OZONE_ID, 0.070)
                            .gas(Gases.NITROUS_DIOXIDE_ID, 0.020)
                            .gas(Gases.IODINE_ID, 0.010)
                            .build(),
                    1.0f,
                    Optional.of(0),
                    21, //todo
                    15, //todo
                    Optional.empty() //todo
            )
    );

    public static void register() {
        Registry.register(AddonRegistry.CELESTIAL_POSITION_TYPE, new ResourceLocation(Constant.MOD_ID, "static"), StaticCelestialPositionType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_POSITION_TYPE, new ResourceLocation(Constant.MOD_ID, "orbital"), OrbitalCelestialPositionType.INSTANCE);

        Registry.register(AddonRegistry.CELESTIAL_DISPLAY_TYPE, new ResourceLocation(Constant.MOD_ID, "empty"), EmptyCelestialDisplayType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_DISPLAY_TYPE, new ResourceLocation(Constant.MOD_ID, "icon"), IconCelestialDisplayType.INSTANCE);

        Registry.register(AddonRegistry.GALAXY, MILKY_WAY_KEY.location(), MILKY_WAY);

        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new ResourceLocation(Constant.MOD_ID, "star"), StarType.INSTANCE);
        Registry.register(AddonRegistry.CELESTIAL_BODY_TYPE, new ResourceLocation(Constant.MOD_ID, "planet"), PlanetType.INSTANCE);

        Registry.register(AddonRegistry.CELESTIAL_BODY, SOL_KEY.location(), SOL);
        Registry.register(AddonRegistry.CELESTIAL_BODY, EARTH_KEY.location(), EARTH);
    }
}
