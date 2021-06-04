package dev.galacticraft.api.universe.celestialbody;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.registry.AddonRegistry;

public record CelestialBody<C extends CelestialBodyConfig, T extends CelestialBodyType<C>>(T type, C config) {
    public static final Codec<CelestialBody<?, ?>> CODEC = AddonRegistry.CELESTIAL_BODY_TYPE.dispatch(CelestialBody::type, CelestialBodyType::getCodec);
}
