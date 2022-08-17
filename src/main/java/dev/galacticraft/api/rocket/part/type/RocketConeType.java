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
import dev.galacticraft.api.rocket.part.ConfiguredRocketCone;
import dev.galacticraft.api.rocket.part.config.RocketConeConfig;
import dev.galacticraft.api.rocket.recipe.RocketPartRecipe;
import dev.galacticraft.api.rocket.travelpredicate.ConfiguredTravelPredicate;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketConeConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The cone of a rocket. Controls how fast the rocket can accelerate.
 */
public non-sealed abstract class RocketConeType<C extends RocketConeConfig> implements RocketPartType<C> {
    public static final RocketConeType<DefaultRocketConeConfig> INVALID = new RocketConeType<>(DefaultRocketConeConfig.CODEC) {
        @Override
        public void tick(@NotNull Rocket rocket, @NotNull DefaultRocketConeConfig config) {

        }

        @Override
        public @Nullable RocketPartRecipe getRecipe(@NotNull DefaultRocketConeConfig config) {
            return null;
        }

        @Override
        public @NotNull ConfiguredTravelPredicate<?, ?> travelPredicate(@NotNull DefaultRocketConeConfig config) {
            return null;
        }
    };

    private final @NotNull Codec<ConfiguredRocketCone<C, RocketConeType<C>>> codec;

    public RocketConeType(@NotNull Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(this::configure, ConfiguredRocketCone::config).codec();
    }

    public @NotNull ConfiguredRocketCone<C, RocketConeType<C>> configure(@NotNull C config) {
        return new ConfiguredRocketCone<>(config, this);
    }

    @Override
    public @NotNull Codec<ConfiguredRocketCone<C, RocketConeType<C>>> codec() {
        return this.codec;
    }

    // todo: lander?
}
