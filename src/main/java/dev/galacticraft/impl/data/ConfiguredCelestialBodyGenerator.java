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

package dev.galacticraft.impl.data;

import dev.galacticraft.api.data.ConfiguredCelestialBodyDataProvider;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.universe.BuiltinObjects;
import dev.galacticraft.impl.universe.celestialbody.config.StarConfig;
import dev.galacticraft.impl.universe.celestialbody.type.StarType;
import dev.galacticraft.impl.universe.display.config.IconCelestialDisplayConfig;
import dev.galacticraft.impl.universe.display.type.IconCelestialDisplayType;
import dev.galacticraft.impl.universe.position.config.StaticCelestialPositionConfig;
import dev.galacticraft.impl.universe.position.type.StaticCelestialPositionType;
import dev.galacticraft.machinelib.api.gas.Gases;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ConfiguredCelestialBodyGenerator extends ConfiguredCelestialBodyDataProvider {
    public ConfiguredCelestialBodyGenerator(PackOutput output) {
        super(output);
    }

    @Override
    public void generateCelestialBodies() {
        this.add(BuiltinObjects.SOL_KEY, StarType.INSTANCE.configure(
                        new StarConfig(
                                Component.translatable("star.galacticraft-api.sol.name"),
                                Component.translatable("star.galacticraft-api.sol.description"),
                                BuiltinObjects.MILKY_WAY_KEY,
                                StaticCelestialPositionType.INSTANCE.configure(new StaticCelestialPositionConfig(0, 0)),
                                IconCelestialDisplayType.INSTANCE.configure(new IconCelestialDisplayConfig(new ResourceLocation(Constant.MOD_ID, "textures/body_icons.png"), 0, 0, 16, 16, 1.5f)),
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
                                5772
                        )
                ));
    }

    @Override
    public @NotNull String getName() {
        return "Galacticraft API Celestial Bodies";
    }
}
