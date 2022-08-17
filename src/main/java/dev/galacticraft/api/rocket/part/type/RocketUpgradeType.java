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
import dev.galacticraft.api.rocket.part.ConfiguredRocketUpgrade;
import dev.galacticraft.api.rocket.part.config.RocketUpgradeConfig;
import dev.galacticraft.api.rocket.recipe.RocketPartRecipe;
import dev.galacticraft.api.rocket.travelpredicate.ConfiguredTravelPredicate;
import dev.galacticraft.impl.rocket.part.config.DefaultRocketUpgradeConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An upgrade for a rocket.
 */
public non-sealed abstract class RocketUpgradeType<C extends RocketUpgradeConfig> implements RocketPartType<C> {
    public static final RocketUpgradeType<DefaultRocketUpgradeConfig> INVALID = new RocketUpgradeType<>(DefaultRocketUpgradeConfig.CODEC) {
        @Override
        public void tick(@NotNull Rocket rocket, @NotNull DefaultRocketUpgradeConfig config) {

        }

        @Override
        public @Nullable RocketPartRecipe getRecipe(@NotNull DefaultRocketUpgradeConfig config) {
            return null;
        }

        @Override
        public @NotNull ConfiguredTravelPredicate<?, ?> travelPredicate(@NotNull DefaultRocketUpgradeConfig config) {
            return null;
        }
    };

    public static final RocketUpgradeType<DefaultRocketUpgradeConfig> NO_UPGRADE = new RocketUpgradeType<>(DefaultRocketUpgradeConfig.CODEC) {
        @Override
        public void tick(@NotNull Rocket rocket, @NotNull DefaultRocketUpgradeConfig config) {

        }

        @Override
        public @Nullable RocketPartRecipe getRecipe(@NotNull DefaultRocketUpgradeConfig config) {
            return null;
        }

        @Override
        public @NotNull ConfiguredTravelPredicate<?, ?> travelPredicate(@NotNull DefaultRocketUpgradeConfig config) {
            return null;
        }
    };

    private final @NotNull Codec<ConfiguredRocketUpgrade<C, RocketUpgradeType<C>>> codec;

    protected RocketUpgradeType(@NotNull Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(this::configure, ConfiguredRocketUpgrade::config).codec();
    }

    public @NotNull ConfiguredRocketUpgrade<C, RocketUpgradeType<C>> configure(@NotNull C config) {
        return new ConfiguredRocketUpgrade<>(config, this);
    }

    @Override
    public @NotNull Codec<ConfiguredRocketUpgrade<C, RocketUpgradeType<C>>> codec() {
        return this.codec;
    }

}
