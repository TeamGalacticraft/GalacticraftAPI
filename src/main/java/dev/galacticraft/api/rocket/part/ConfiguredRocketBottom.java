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
import dev.galacticraft.api.rocket.part.config.RocketBottomConfig;
import dev.galacticraft.api.rocket.part.type.RocketBottomType;
import dev.galacticraft.impl.rocket.part.ConfiguredRocketBottomImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public non-sealed interface ConfiguredRocketBottom<C extends RocketBottomConfig, T extends RocketBottomType<C>> extends ConfiguredRocketPart<C, T> {
    Codec<ConfiguredRocketBottom<?, ?>> DIRECT_CODEC = RocketRegistry.ROCKET_BOTTOM_TYPE.byNameCodec().dispatch(ConfiguredRocketBottom::type, RocketBottomType::codec);
    Codec<Holder<ConfiguredRocketBottom<?, ?>>> CODEC = RegistryFileCodec.create(RocketRegistry.CONFIGURED_ROCKET_BOTTOM_KEY, DIRECT_CODEC);
    Codec<HolderSet<ConfiguredRocketBottom<?, ?>>> LIST_CODEC = RegistryCodecs.homogeneousList(RocketRegistry.CONFIGURED_ROCKET_BOTTOM_KEY, DIRECT_CODEC);

    @Contract(pure = true, value = "_, _ -> new")
    static @NotNull <C extends RocketBottomConfig, T extends RocketBottomType<C>> ConfiguredRocketBottom<C, T> create(@NotNull C config, @NotNull T type) {
        return new ConfiguredRocketBottomImpl<>(config, type);
    }

    /**
     * Returns the fuel capacity of this rocket.
     *
     * @return the fuel capacity of this rocket. 1 bucket = 81000.
     * @see net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
     */
    default long getFuelCapacity() {
        return this.type().getFuelCapacity(this.config());
    }
}
