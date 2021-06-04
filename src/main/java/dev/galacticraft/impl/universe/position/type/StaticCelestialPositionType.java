package dev.galacticraft.impl.universe.position.type;

import dev.galacticraft.api.universe.position.CelestialPositionType;
import dev.galacticraft.impl.universe.position.config.StaticCelestialPositionConfig;

public class StaticCelestialPositionType extends CelestialPositionType<StaticCelestialPositionConfig> {
    public static final StaticCelestialPositionType INSTANCE = new StaticCelestialPositionType();
    private StaticCelestialPositionType() {
        super(StaticCelestialPositionConfig.CODEC);
    }

    @Override
    public double x(StaticCelestialPositionConfig config, int worldTime, float delta) {
        return config.x();
    }

    @Override
    public double y(StaticCelestialPositionConfig config, int worldTime, float delta) {
        return config.y();
    }
}
