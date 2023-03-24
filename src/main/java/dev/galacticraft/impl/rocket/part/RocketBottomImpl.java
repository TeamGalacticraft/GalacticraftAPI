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

package dev.galacticraft.impl.rocket.part;

import dev.galacticraft.api.rocket.part.RocketBottom;
import dev.galacticraft.api.rocket.part.config.RocketBottomConfig;
import dev.galacticraft.api.rocket.part.type.RocketBottomType;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketBottomConfig;
import dev.galacticraft.impl.rocket.part.type.InvalidRocketBottomType;
import dev.galacticraft.impl.universe.BuiltinObjects;
import net.minecraft.data.worldgen.BootstapContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public record RocketBottomImpl<C extends RocketBottomConfig, T extends RocketBottomType<C>>(@NotNull C config, @NotNull T type) implements RocketBottom<C, T> {
    @ApiStatus.Internal
    public static void bootstrapRegistries(BootstapContext<RocketBottom<?, ?>> context) {
        context.register(BuiltinObjects.INVALID_ROCKET_BOTTOM, RocketBottom.create(DefaultRocketBottomConfig.INSTANCE, InvalidRocketBottomType.INSTANCE));
    }
}