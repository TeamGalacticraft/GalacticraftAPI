package dev.galacticraft.impl.universe.position.type;

import dev.galacticraft.api.universe.position.CelestialPositionType;
import dev.galacticraft.impl.universe.position.config.OrbitalCelestialPositionConfig;

public class OrbitalCelestialPositionType extends CelestialPositionType<OrbitalCelestialPositionConfig> {
    public static final OrbitalCelestialPositionType INSTANCE = new OrbitalCelestialPositionType();
    private static final double OVERWORLD_DAY_LENGTH = 24000;

    protected OrbitalCelestialPositionType() {
        super(OrbitalCelestialPositionConfig.CODEC);
    }

    @Override
    public double x(OrbitalCelestialPositionConfig config, int worldTime, float delta) {
        double distanceFromCenter = 3.0 * config.distance() * (config.planet() ? 25.0 : (1.0 / 5.0));

        return Math.sin(((double)(worldTime + delta)) / ((config.planet() ? 200.0 : 2.0) * (config.orbitTime() / OVERWORLD_DAY_LENGTH)) + Math.PI) * distanceFromCenter;
    }

    @Override
    public double y(OrbitalCelestialPositionConfig config, int worldTime, float delta) {
        double distanceFromCenter = 3.0 * config.distance() * (config.planet() ? 25.0 : (1.0 / 5.0));
        return Math.cos(((double)(worldTime + delta)) / ((config.planet() ? 200.0 : 2.0) * (config.orbitTime() / OVERWORLD_DAY_LENGTH)) + Math.PI) * distanceFromCenter;
    }
}
