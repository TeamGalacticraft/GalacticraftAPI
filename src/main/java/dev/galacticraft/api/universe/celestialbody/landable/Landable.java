package dev.galacticraft.api.universe.celestialbody.landable;

import dev.galacticraft.api.atmosphere.AtmosphericInfo;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface Landable<C extends CelestialBodyConfig> {
    @NotNull RegistryKey<World> world(C config);

    @NotNull AtmosphericInfo atmosphere(C config);

    float gravity(C config);

    int accessWeight(C config);
}
