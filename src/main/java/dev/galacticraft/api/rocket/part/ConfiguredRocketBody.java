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

package dev.galacticraft.api.rocket.part;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.registry.RocketRegistry;
import dev.galacticraft.api.rocket.part.config.RocketBodyConfig;
import dev.galacticraft.api.rocket.part.type.RocketBodyType;
import dev.galacticraft.impl.rocket.part.ConfiguredRocketBodyImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public non-sealed interface ConfiguredRocketBody<C extends RocketBodyConfig, T extends RocketBodyType<C>> extends ConfiguredRocketPart<C, T> {
    Codec<ConfiguredRocketBody<?, ?>> DIRECT_CODEC = RocketRegistry.ROCKET_BODY_TYPE.byNameCodec().dispatch(ConfiguredRocketBody::type, RocketBodyType::codec);
    Codec<Holder<ConfiguredRocketBody<?, ?>>> CODEC = RegistryFileCodec.create(RocketRegistry.CONFIGURED_ROCKET_BODY_KEY, DIRECT_CODEC);
    Codec<HolderSet<ConfiguredRocketBody<?, ?>>> LIST_CODEC = RegistryCodecs.homogeneousList(RocketRegistry.CONFIGURED_ROCKET_BODY_KEY, DIRECT_CODEC);

    @Contract(pure = true, value = "_, _ -> new")
    static @NotNull <C extends RocketBodyConfig, T extends RocketBodyType<C>> ConfiguredRocketBody<C, T> create(@NotNull C config, @NotNull T type) {
        return new ConfiguredRocketBodyImpl<>(config, type);
    }

    /**
     * Returns the maximum number of passengers allowed on this rocket.
     * @return the maximum number of passengers allowed on this rocket.
     */
    @Contract(pure = true)
    default int getMaxPassengers() {
        return this.type().getMaxPassengers(this.config());
    }

    /**
     * Returns the maximum number of upgrades that can be installed on this rocket.
     *
     * @return the maximum number of upgrades that can be installed on this rocket.
     */
    @Contract(pure = true)
    default int getUpgradeCapacity() {
        return this.type().getUpgradeCapacity(this.config());
    }
}
