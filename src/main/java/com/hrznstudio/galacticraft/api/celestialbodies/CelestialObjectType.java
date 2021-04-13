/*
 * Copyright (c) 2019-2021 HRZN LTD
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

package com.hrznstudio.galacticraft.api.celestialbodies;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.Locale;

public enum CelestialObjectType {
    STAR,
    PLANET,
    MOON,
    SATELLITE;

    public static final Codec<CelestialObjectType> CODEC = Codec.of(new Encoder<CelestialObjectType>() {
        @Override
        public <T> DataResult<T> encode(CelestialObjectType input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(ops.createString(input.name().toLowerCase(Locale.ROOT)));
        }
    }, new Decoder<CelestialObjectType>() {
        @Override
        public <T> DataResult<Pair<CelestialObjectType, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(new Pair<>(valueOf(ops.getStringValue(input).get().orThrow().toUpperCase(Locale.ROOT)), input));
        }
    });
}
