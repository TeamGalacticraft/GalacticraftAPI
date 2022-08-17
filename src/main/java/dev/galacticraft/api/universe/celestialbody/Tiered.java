package dev.galacticraft.api.universe.celestialbody;

import org.jetbrains.annotations.NotNull;

public interface Tiered<C extends CelestialBodyConfig> {
    int accessWeight(@NotNull C config);
}
