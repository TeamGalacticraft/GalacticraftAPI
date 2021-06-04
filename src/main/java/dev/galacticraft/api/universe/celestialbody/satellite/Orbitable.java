package dev.galacticraft.api.universe.celestialbody.satellite;

import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import org.jetbrains.annotations.Nullable;

public interface Orbitable<C extends CelestialBodyConfig> {
    @Nullable SatelliteRecipe satelliteRecipe(C config);
}
