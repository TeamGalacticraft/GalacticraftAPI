/*
 * Copyright (c) 2019-2023 Team Galacticraft
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

package dev.galacticraft.impl.internal.fabric;

import dev.galacticraft.api.registry.AddonRegistries;
import dev.galacticraft.api.registry.RocketRegistries;
import dev.galacticraft.api.rocket.part.*;
import dev.galacticraft.api.rocket.recipe.RocketPartRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.landable.teleporter.CelestialTeleporter;
import dev.galacticraft.impl.data.BootstrapDataProvider;
import dev.galacticraft.impl.internal.world.gen.biome.GcApiBiomes;
import dev.galacticraft.impl.rocket.part.*;
import dev.galacticraft.impl.universe.galaxy.GalaxyImpl;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class GalacticraftAPIData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.createPack().addProvider(BootstrapDataProvider.create("Biomes", GcApiBiomes::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<CelestialBody<?, ?>>create("Celestial Bodies", CelestialBody::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.create("Galaxies", GalaxyImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<RocketCone<?, ?>>create("Rocket Cones", RocketConeImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<RocketBody<?, ?>>create("Rocket Bodies", RocketBodyImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<RocketFin<?, ?>>create("Rocket Fins", RocketFinImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<RocketBooster<?, ?>>create("Rocket Boosters", RocketBoosterImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<RocketBottom<?, ?>>create("Rocket Bottoms", RocketBottomImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<RocketUpgrade<?, ?>>create("Rocket Upgrades", RocketUpgradeImpl::bootstrapRegistries));
        generator.createPack().addProvider(BootstrapDataProvider.<CelestialTeleporter<?, ?>>create("Celestial Teleporters", CelestialTeleporter::bootstrapRegistries));
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
        registryBuilder.add(Registries.BIOME, GcApiBiomes::bootstrapRegistries);
        registryBuilder.add(AddonRegistries.GALAXY, GalaxyImpl::bootstrapRegistries);
        registryBuilder.add(AddonRegistries.CELESTIAL_BODY, CelestialBody::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_CONE, RocketConeImpl::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_BODY, RocketBodyImpl::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_FIN, RocketFinImpl::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_BOOSTER, RocketBoosterImpl::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_BOTTOM, RocketBottomImpl::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_UPGRADE, RocketUpgradeImpl::bootstrapRegistries);
        registryBuilder.add(RocketRegistries.ROCKET_PART_RECIPE, RocketPartRecipe::bootstrapRegistries);
        registryBuilder.add(AddonRegistries.CELESTIAL_TELEPORTER, CelestialTeleporter::bootstrapRegistries);
    }
}
