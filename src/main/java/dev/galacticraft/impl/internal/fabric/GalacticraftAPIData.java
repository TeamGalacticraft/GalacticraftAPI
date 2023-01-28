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

import dev.galacticraft.impl.data.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class GalacticraftAPIData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredCelestialBodyGenerator>) ConfiguredCelestialBodyGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<GalaxyDataGenerator>) GalaxyDataGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredRocketConeDataGenerator>) ConfiguredRocketConeDataGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredRocketBodyDataGenerator>) ConfiguredRocketBodyDataGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredRocketFinDataGenerator>) ConfiguredRocketFinDataGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredRocketBoosterDataGenerator>) ConfiguredRocketBoosterDataGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredRocketBottomDataGenerator>) ConfiguredRocketBottomDataGenerator::new);
        generator.createPack().addProvider((FabricDataGenerator.Pack.Factory<ConfiguredRocketUpgradeDataGenerator>) ConfiguredRocketUpgradeDataGenerator::new);
    }
}
