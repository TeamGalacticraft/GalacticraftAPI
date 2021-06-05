package dev.galacticraft.api.satellite;

import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import net.minecraft.text.Text;

public interface Satellite<C extends CelestialBodyConfig> {
    SatelliteOwnershipData ownershipData(C config);

    void setCustomName(Text text, C config);

    Text getCustomName(C config);
}
