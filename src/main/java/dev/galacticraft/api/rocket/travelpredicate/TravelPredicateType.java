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

package dev.galacticraft.api.rocket.travelpredicate;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public abstract class TravelPredicateType<C extends TravelPredicateConfig> {
    private final Codec<ConfiguredTravelPredicate<C>> codec;

    public TravelPredicateType(Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(this::configure, ConfiguredTravelPredicate::config).codec();
    }

    public ConfiguredTravelPredicate<C> configure(C config) {
        return new ConfiguredTravelPredicate<>(config, this);
    }

    public abstract AccessType canTravelTo(CelestialBody<?, ?> type, Object2BooleanFunction<RocketPart> parts, C config);

    public Codec<ConfiguredTravelPredicate<C>> getCodec() {
        return this.codec;
    }

    public enum AccessType implements StringIdentifiable {
        /**
         * Allow other rocket parts to decide whether or not the player may visit the celestial body
         */
        PASS,
        /**
         * Forcefully block access to celestial body - overrides {@link AccessType#ALLOW}
         */
        BLOCK,
        /**
         * Allow access to celestial body.
         * Can be overridden by {@link AccessType#BLOCK}
         */
        ALLOW;

        public static final Codec<AccessType> CODEC = StringIdentifiable.createCodec(Enum::ordinal, i -> AccessType.values()[i], s -> AccessType.valueOf(s.toUpperCase(Locale.ROOT)));

        public AccessType merge(AccessType other) {
            if (other == PASS) return this;
            if (other == BLOCK || this == BLOCK) return BLOCK;
            return ALLOW;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
