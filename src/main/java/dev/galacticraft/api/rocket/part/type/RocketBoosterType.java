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
import dev.galacticraft.api.rocket.part.ConfiguredRocketBooster;
import dev.galacticraft.api.rocket.part.config.RocketBoosterConfig;
import dev.galacticraft.api.rocket.recipe.RocketPartRecipe;
import dev.galacticraft.api.rocket.travelpredicate.ConfiguredTravelPredicate;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketBoosterConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The booster of a rocket. Controls how fast the rocket accelerates, where the rocket can travel to, and how much fuel is consumed.
 */
public non-sealed abstract class RocketBoosterType<C extends RocketBoosterConfig> implements RocketPartType<C> {
    public static final RocketBoosterType<DefaultRocketBoosterConfig> INVALID = new RocketBoosterType<>(DefaultRocketBoosterConfig.CODEC) {
        @Override
        public void tick(@NotNull Rocket rocket, @NotNull DefaultRocketBoosterConfig config) {

        }

        @Override
        public @Nullable RocketPartRecipe getRecipe(@NotNull DefaultRocketBoosterConfig config) {
            return null;
        }

        @Override
        public @NotNull ConfiguredTravelPredicate<?, ?> travelPredicate(@NotNull DefaultRocketBoosterConfig config) {
            return null;
        }

        @Override
        public double getMaximumVelocity(@NotNull DefaultRocketBoosterConfig config) {
            return 0;
        }

        @Override
        public double getAccelerationPerTick(@NotNull DefaultRocketBoosterConfig config) {
            return 0;
        }

        @Override
        public long getFuelUsagePerTick(@NotNull DefaultRocketBoosterConfig config) {
            return 0;
        }
    };

    private final @NotNull Codec<ConfiguredRocketBooster<C, RocketBoosterType<C>>> codec;

    protected RocketBoosterType(@NotNull Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(this::configure, ConfiguredRocketBooster::config).codec();
    }

    public @NotNull ConfiguredRocketBooster<C, RocketBoosterType<C>> configure(@NotNull C config) {
        return new ConfiguredRocketBooster<>(config, this);
    }

    @Override
    public @NotNull Codec<ConfiguredRocketBooster<C, RocketBoosterType<C>>> codec() {
        return this.codec;
    }

    /**
     * Returns the maximum velocity of this booster in blocks/tick.
     * @return the maximum velocity of this booster blocks/tick.
     */
    @Contract(pure = true)
    public abstract double getMaximumVelocity(@NotNull C config);

    /**
     * Returns the acceleration of this booster in blocks/tick^2.
     * @return the acceleration of this booster blocks/tick^2.
     */
    @Contract(pure = true)
    public abstract double getAccelerationPerTick(@NotNull C config);

    /**
     * Returns the amount of fuel consumed by this booster per tick.
     * @return the amount of fuel consumed by this booster per tick.
     */
    @Contract(pure = true)
    public abstract long getFuelUsagePerTick(@NotNull C config);
}
