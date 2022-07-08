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

package dev.galacticraft.impl.universe.celestialbody.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record StarConfig(@NotNull MutableComponent name, @NotNull MutableComponent description,
                         @NotNull Optional<ResourceKey<Galaxy>> galaxy, Optional<ResourceKey<CelestialBody<?, ?>>> parent,
                         @NotNull CelestialPosition<?, ?> position, @NotNull CelestialDisplay<?, ?> display,
                         @NotNull Optional<ResourceKey<Level>> world, GasComposition photosphericComposition, float gravity,
                         double luminance, @NotNull Optional<Integer> accessWeight, int surfaceTemperature,
                         @NotNull Optional<SatelliteRecipe> satelliteRecipe) implements CelestialBodyConfig {
    public static final Codec<StarConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(Component::translatable, Component::getString).forGetter(StarConfig::name),
            Codec.STRING.fieldOf("description").xmap(Component::translatable, Component::getString).forGetter(StarConfig::description),
            ResourceLocation.CODEC.optionalFieldOf("galaxy").xmap(id -> id.map(resourceLocation -> ResourceKey.create(AddonRegistry.GALAXY_KEY, resourceLocation)), key -> key.map(ResourceKey::location)).forGetter(StarConfig::galaxy),
            ResourceLocation.CODEC.optionalFieldOf("parent").xmap(id -> id.map(resourceLocation -> ResourceKey.create(AddonRegistry.CELESTIAL_BODY_KEY, resourceLocation)), key -> key.map(ResourceKey::location)).forGetter(StarConfig::parent),
            CelestialPosition.CODEC.fieldOf("position").forGetter(StarConfig::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(StarConfig::display),
            Level.RESOURCE_KEY_CODEC.optionalFieldOf("world").forGetter(StarConfig::world),
            GasComposition.CODEC.fieldOf("photospheric_composition").forGetter(StarConfig::photosphericComposition),
            Codec.FLOAT.fieldOf("gravity").forGetter(StarConfig::gravity),
            Codec.DOUBLE.fieldOf("luminance").forGetter(StarConfig::luminance),
            Codec.INT.optionalFieldOf("access_weight").forGetter(StarConfig::accessWeight),
            Codec.INT.fieldOf("surface_temperature").forGetter(StarConfig::surfaceTemperature),
            SatelliteRecipe.CODEC.optionalFieldOf("satellite_recipe").forGetter(StarConfig::satelliteRecipe)
    ).apply(instance, StarConfig::new));
}
