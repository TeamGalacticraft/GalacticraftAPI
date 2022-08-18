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

package dev.galacticraft.impl.universe.celestialbody.type;

import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.SurfaceEnvironment;
import dev.galacticraft.api.universe.celestialbody.star.Star;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.impl.universe.celestialbody.config.DecorativeStarConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DecorativeStarType extends CelestialBodyType<DecorativeStarConfig> implements Star<DecorativeStarConfig>,
        SurfaceEnvironment<DecorativeStarConfig> {
    public static final DecorativeStarType INSTANCE = new DecorativeStarType();

    protected DecorativeStarType() {
        super(DecorativeStarConfig.CODEC);
    }

    @Override
    public @NotNull Component name(DecorativeStarConfig config) {
        return config.name();
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(Registry<CelestialBody<?, ?>> registry, DecorativeStarConfig config) {
        return registry.get(config.parent().orElse(null));
    }

    @Override
    public @Nullable ResourceKey<Galaxy> galaxy(DecorativeStarConfig config) {
        return config.galaxy().orElse(null);
    }

    @Override
    public @NotNull Component description(DecorativeStarConfig config) {
        return config.description();
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(DecorativeStarConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(DecorativeStarConfig config) {
        return config.display();
    }

    /**
     * {@inheritDoc}
     * Treat as this star's photospheric composition
     */
    @Override
    public @NotNull GasComposition atmosphere(DecorativeStarConfig config) {
        return config.photosphericComposition();
    }

    @Override
    public float gravity(DecorativeStarConfig config) {
        return config.gravity();
    }

    @Override
    public double luminance(DecorativeStarConfig config) {
        return config.luminance();
    }

    @Override
    public int temperature(RegistryAccess access, long time, DecorativeStarConfig config) {
        return config.surfaceTemperature(); //todo: temperature providers?
    }
}
