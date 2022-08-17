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

package dev.galacticraft.api.rocket.part.type;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.rocket.entity.Rocket;
import dev.galacticraft.api.rocket.part.ConfiguredRocketBottom;
import dev.galacticraft.api.rocket.part.config.RocketBottomConfig;
import dev.galacticraft.api.rocket.recipe.RocketPartRecipe;
import dev.galacticraft.api.rocket.travelpredicate.ConfiguredTravelPredicate;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketBottomConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The bottom of a rocket. Controls how much fuel the rocket can hold.
 */
public non-sealed abstract class RocketBottomType<C extends RocketBottomConfig> implements RocketPartType<C> {
    public static final RocketBottomType<DefaultRocketBottomConfig> INVALID = new RocketBottomType<>(DefaultRocketBottomConfig.CODEC) {
        @Override
        public void tick(@NotNull Rocket rocket, @NotNull DefaultRocketBottomConfig config) {

        }

        @Override
        public @Nullable RocketPartRecipe getRecipe(@NotNull DefaultRocketBottomConfig config) {
            return null;
        }

        @Override
        public @NotNull ConfiguredTravelPredicate<?, ?> travelPredicate(@NotNull DefaultRocketBottomConfig config) {
            return null;
        }

        @Override
        public long getFuelCapacity(@NotNull DefaultRocketBottomConfig config) {
            return 0;
        }
    };

    private final @NotNull Codec<ConfiguredRocketBottom<C, RocketBottomType<C>>> codec;

    protected RocketBottomType(@NotNull Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(this::configure, ConfiguredRocketBottom::config).codec();
    }

    public @NotNull ConfiguredRocketBottom<C, RocketBottomType<C>> configure(@NotNull C config) {
        return new ConfiguredRocketBottom<>(config, this);
    }

    @Override
    public @NotNull Codec<ConfiguredRocketBottom<C, RocketBottomType<C>>> codec() {
        return this.codec;
    }

    /**
     * Returns the fuel capacity of this rocket.
     * @return the fuel capacity of this rocket. 1 bucket = 81000.
     * @see net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
     */
    public abstract long getFuelCapacity(@NotNull C config);
}
