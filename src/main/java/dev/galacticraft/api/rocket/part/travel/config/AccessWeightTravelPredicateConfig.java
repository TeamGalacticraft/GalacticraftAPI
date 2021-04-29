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

package dev.galacticraft.api.rocket.part.travel.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.rocket.part.travel.AccessType;

public class AccessWeightTravelPredicateConfig implements TravelPredicateConfig {
    public static final Codec<AccessWeightTravelPredicateConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("weight").forGetter(AccessWeightTravelPredicateConfig::getWeight),
            AccessType.CODEC.optionalFieldOf("default", AccessType.PASS).forGetter(AccessWeightTravelPredicateConfig::getDefault)
    ).apply(instance, AccessWeightTravelPredicateConfig::new));

    private final int weight;
    private final AccessType defaultType;

    public AccessWeightTravelPredicateConfig(int weight, AccessType defaultType) {
        this.weight = weight;
        this.defaultType = defaultType;
    }

    public int getWeight() {
        return this.weight;
    }

    public AccessType getDefault() {
        return this.defaultType;
    }
}
