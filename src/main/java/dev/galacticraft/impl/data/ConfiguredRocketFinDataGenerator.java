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

import dev.galacticraft.api.data.ConfiguredRocketFinDataProvider;
import dev.galacticraft.api.rocket.part.RocketFin;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketFinConfig;
import dev.galacticraft.impl.rocket.part.type.InvalidRocketFinType;
import dev.galacticraft.impl.universe.BuiltinObjects;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public class ConfiguredRocketFinDataGenerator extends ConfiguredRocketFinDataProvider {
    public ConfiguredRocketFinDataGenerator(PackOutput output) {
        super(output);
    }

    @Override
    public void generateGalaxies() {
        this.add(BuiltinObjects.INVALID_ROCKET_FIN, RocketFin.create(DefaultRocketFinConfig.INSTANCE, InvalidRocketFinType.INSTANCE));
    }

    @Override
    public @NotNull String getName() {
        return "Galacticraft API Rocket Fins";
    }
}
