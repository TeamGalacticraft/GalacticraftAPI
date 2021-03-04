package com.hrznstudio.galacticraft.api.celestialbodies;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.Locale;

public enum CelestialObjectType {
    STAR,
    PLANET,
    MOON;

    public static final Codec<CelestialObjectType> CODEC = Codec.of(new Encoder<CelestialObjectType>() {
        @Override
        public <T> DataResult<T> encode(CelestialObjectType input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(ops.createString(input.name().toLowerCase(Locale.ROOT)));
        }
    }, new Decoder<CelestialObjectType>() {
        @Override
        public <T> DataResult<Pair<CelestialObjectType, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(new Pair<>(valueOf(ops.getStringValue(input).getOrThrow(false, s -> {throw new RuntimeException(s);}).toUpperCase(Locale.ROOT)), input));
        }
    });
}
