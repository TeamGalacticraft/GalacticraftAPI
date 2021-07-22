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

package dev.galacticraft.impl.universe.celestialbody.type;

import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import dev.galacticraft.api.universe.celestialbody.satellite.Orbitable;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.impl.universe.celestialbody.config.PlanetConfig;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlanetType extends CelestialBodyType<PlanetConfig> implements Landable<PlanetConfig>, Orbitable<PlanetConfig> {
    public static final PlanetType INSTANCE = new PlanetType();
    protected PlanetType() {
        super(PlanetConfig.CODEC);
    }

    @Override
    public @NotNull TranslatableText name(PlanetConfig config) {
        return config.name();
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(Registry<CelestialBody<?, ?>> registry, PlanetConfig config) {
        return registry.get(config.parent());
    }

    @Override
    public @NotNull RegistryKey<Galaxy> galaxy(PlanetConfig config) {
        return config.galaxy();
    }

    @Override
    public @NotNull TranslatableText description(PlanetConfig config) {
        return config.description();
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(PlanetConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(PlanetConfig config) {
        return config.display();
    }

    @Override
    public @NotNull RegistryKey<World> world(PlanetConfig config) {
        return config.world();
    }

    @Override
    public @NotNull GasComposition atmosphere(PlanetConfig config) {
        return config.atmosphere();
    }

    @Override
    public float gravity(PlanetConfig config) {
        return config.gravity();
    }

    @Override
    public int accessWeight(PlanetConfig config) {
        return config.accessWeight();
    }

    @Override
    public int dayTemperature(PlanetConfig config) {
        return config.dayTemperature();
    }

    @Override
    public int nightTemperature(PlanetConfig config) {
        return config.nightTemperature();
    }

    @Override
    public @Nullable SatelliteRecipe satelliteRecipe(PlanetConfig config) {
        return config.satelliteRecipe().orElse(null);
    }
}
