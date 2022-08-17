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
import dev.galacticraft.api.rocket.part.ConfiguredRocketBody;
import dev.galacticraft.api.rocket.part.config.RocketBodyConfig;
import dev.galacticraft.api.rocket.recipe.RocketPartRecipe;
import dev.galacticraft.api.rocket.travelpredicate.ConfiguredTravelPredicate;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketBodyConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The body of a rocket. Controls the number of installable rocket upgrades and the maximum passenger count.
 */
public non-sealed abstract class RocketBodyType<C extends RocketBodyConfig> implements RocketPartType<C> {
    public static final RocketBodyType<DefaultRocketBodyConfig> INVALID = new RocketBodyType<>(DefaultRocketBodyConfig.CODEC) {
        @Override
        public void tick(@NotNull Rocket rocket, @NotNull DefaultRocketBodyConfig config) {

        }

        @Override
        public @Nullable RocketPartRecipe getRecipe(@NotNull DefaultRocketBodyConfig config) {
            return null;
        }

        @Override
        public @NotNull ConfiguredTravelPredicate<?, ?> travelPredicate(@NotNull DefaultRocketBodyConfig config) {
            return null;
        }

        @Override
        public int getMaxPassengers(@NotNull DefaultRocketBodyConfig config) {
            return 0;
        }

        @Override
        public int getUpgradeCapacity(@NotNull DefaultRocketBodyConfig config) {
            return 0;
        }
    };

    private final @NotNull Codec<ConfiguredRocketBody<C, RocketBodyType<C>>> codec;

    protected RocketBodyType(@NotNull Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(this::configure, ConfiguredRocketBody::config).codec();
    }

    public @NotNull ConfiguredRocketBody<C, RocketBodyType<C>> configure(@NotNull C config) {
        return ConfiguredRocketBody.create(config, this);
    }

    @Override
    public @NotNull Codec<ConfiguredRocketBody<C, RocketBodyType<C>>> codec() {
        return this.codec;
    }

    /**
     * Returns the maximum number of passengers allowed on this rocket.
     * @return the maximum number of passengers allowed on this rocket.
     */
    @Contract(pure = true)
    public abstract int getMaxPassengers(@NotNull C config);

    /**
     * Returns the maximum number of upgrades that can be installed on this rocket.
     * @return the maximum number of upgrades that can be installed on this rocket.
     */
    @Contract(pure = true)
    public abstract int getUpgradeCapacity(@NotNull C config);
}
