package dev.galacticraft.api.universe.position;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.registry.AddonRegistry;

public record CelestialPosition<C extends CelestialPositionConfig, T extends CelestialPositionType<C>>(T type, C config) {
    public static final Codec<CelestialPosition<?, ?>> CODEC = AddonRegistry.CELESTIAL_POSITION_TYPE.dispatch(CelestialPosition::type, CelestialPositionType::getCodec);

    public double x(int worldTime, float delta) {
        return this.type.x(this.config, worldTime, delta);
    }

    public double y(int worldTime, float delta) {
        return this.type.x(this.config, worldTime, delta);
    }
}
