package dev.galacticraft.api.universe.celestialbody.star;

import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;

public interface Star<C extends CelestialBodyConfig> {
    double luminance(C config);

    int surfaceTemperature(C config);
}
