package dev.galacticraft.api.universe.position;

import com.mojang.serialization.Codec;

public abstract class CelestialPositionType<C extends CelestialPositionConfig> {
    private final Codec<CelestialPosition<C, CelestialPositionType<C>>> codec;
    public CelestialPositionType(Codec<C> codec) {
        this.codec = codec.fieldOf("config").xmap((config) -> new CelestialPosition<>(this, config), CelestialPosition::config).codec();
    }

    public abstract double x(C config, int worldTime, float delta);

    public abstract double y(C config, int worldTime, float delta);

    public Codec<CelestialPosition<C, CelestialPositionType<C>>> getCodec() {
        return this.codec;
    }

    public CelestialPosition<C, CelestialPositionType<C>> configure(C config) {
        return new CelestialPosition<>(this, config);
    }
}
