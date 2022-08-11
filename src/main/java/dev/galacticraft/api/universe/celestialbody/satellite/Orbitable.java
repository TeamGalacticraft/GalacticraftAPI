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

package dev.galacticraft.api.universe.celestialbody.satellite;

import dev.galacticraft.api.satellite.SpaceStationRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.impl.universe.position.config.SpaceStationConfig;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link dev.galacticraft.api.universe.celestialbody.CelestialBodyType<C> celestial body type} that can potentially allow player-made objects to orbit itself.
 * Moons and other natural satellites are not effected by this.
 *
 * @param <C> the type of configuration
 */
public interface Orbitable<C extends CelestialBodyConfig> {
    /**
     * Returns the {@link SpaceStationRecipe stellite recipe} of this celestial body, or {@code null} if satellites should not be allowed to be created
     *
     * @param config the celestial body configuration to be queried
     * @return the {@link SpaceStationRecipe satellite recipe} of this celestial body
     */
    @Nullable SpaceStationRecipe getSpaceStationRecipe(C config);

    /**
     * Callback to register client listeners (for example, {@link net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer sky renderers}) for this celestial body.
     * Should only be called on the client.
     *
     * @param world
     * @param key             the {@link ResourceKey} of the satellite's dimension
     * @param config          the celestial body configuration
     * @param spaceStationConfig
     */
    void registerClientWorldHooks(RegistryAccess manager, /*Client*/Level world, ResourceKey<Level> key, C config, SpaceStationConfig spaceStationConfig);
}
