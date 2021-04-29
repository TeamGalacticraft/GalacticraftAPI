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

package dev.galacticraft.api.rocket.part.travel;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.celestialbody.CelestialBodyType;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.part.travel.config.IntTravelPredicateConfig;
import net.fabricmc.fabric.api.util.BooleanFunction;

public class AccessWeightPredicateType extends TravelPredicateType<IntTravelPredicateConfig> {
    public static final AccessWeightPredicateType INSTANCE = new AccessWeightPredicateType(IntTravelPredicateConfig.CODEC);

    protected AccessWeightPredicateType(Codec<IntTravelPredicateConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean canTravelTo(CelestialBodyType type, BooleanFunction<RocketPart> parts, IntTravelPredicateConfig config) {
        return type.getAccessWeight() <= config.getWeight();
    }
}
