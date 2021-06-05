/*
 * Copyright (c) 2019-2021 Team Galacticraft
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

package dev.galacticraft.api.universe.celestialbody;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CelestialBodyType<C extends CelestialBodyConfig> {
    private final Codec<CelestialBody<C, CelestialBodyType<C>>> codec;

    public CelestialBodyType(Codec<C> codec) {
        this.codec = codec.fieldOf("config").xmap((config) -> new CelestialBody<>(this, config), CelestialBody::config).codec();
    }

    public abstract @NotNull TranslatableText name(C config);

    public abstract @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager, C config);

    public abstract @NotNull RegistryKey<Galaxy> galaxy(C config);

    public abstract @NotNull TranslatableText description(C config);

    public abstract @NotNull CelestialPosition<?, ?> position(C config);

    public abstract @NotNull CelestialDisplay<?, ?> display(C config);

    public Codec<CelestialBody<C, CelestialBodyType<C>>> getCodec() {
        return this.codec;
    }

    public CelestialBody<C, ? extends CelestialBodyType<C>> configure(C config) {
        return new CelestialBody<>(this, config);
    }
}
