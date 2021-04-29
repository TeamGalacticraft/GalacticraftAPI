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
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.part.travel.config.AccessTypeTravelPredicateConfig;
import net.fabricmc.fabric.api.util.BooleanFunction;
import net.minecraft.util.dynamic.RegistryElementCodec;

import java.util.List;
import java.util.function.Supplier;

public class ConfiguredTravelPredicate<C extends TravelPredicateConfig> {
    public static final Codec<ConfiguredTravelPredicate<?>> CODEC = AddonRegistry.TRAVEL_PREDICATE.dispatch(ConfiguredTravelPredicate::getType, TravelPredicateType::getCodec);
    public static final Codec<Supplier<ConfiguredTravelPredicate<?>>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.CONFIGURED_TRAVEL_PREDICATE_KEY, CODEC);
    public static final Codec<List<Supplier<ConfiguredTravelPredicate<?>>>> REGISTRY_LIST_CODEC = RegistryElementCodec.method_31194(AddonRegistry.CONFIGURED_TRAVEL_PREDICATE_KEY, CODEC);

    public static final ConfiguredTravelPredicate<AccessTypeTravelPredicateConfig> ALWAYS = ConstantTravelPredicateType.INSTANCE.configure(new AccessTypeTravelPredicateConfig(AccessType.ALLOW));
    public static final ConfiguredTravelPredicate<AccessTypeTravelPredicateConfig> PASS = ConstantTravelPredicateType.INSTANCE.configure(new AccessTypeTravelPredicateConfig(AccessType.PASS));
    public static final ConfiguredTravelPredicate<AccessTypeTravelPredicateConfig> NEVER = ConstantTravelPredicateType.INSTANCE.configure(new AccessTypeTravelPredicateConfig(AccessType.BLOCK));

    private final C config;
    private final TravelPredicateType<C> type;

    public ConfiguredTravelPredicate(C config, TravelPredicateType<C> type) {
        this.config = config;
        this.type = type;
    }

    public AccessType canTravelTo(CelestialBodyType type, BooleanFunction<RocketPart> parts) {
        return this.type.canTravelTo(type, parts, this.config);
    }

    public C getConfig() {
        return config;
    }

    public TravelPredicateType<C> getType() {
        return type;
    }
}
