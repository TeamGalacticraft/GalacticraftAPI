package dev.galacticraft.api.internal.server.fabric;

import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.registry.AddonRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;

public class DedicatedServerGalacticraftAPI implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        long startInitTime = System.currentTimeMillis();
        GalacticraftAPI.LOGGER.info("Registering entries...");
        AddonRegistry.invokeEvents();
        GalacticraftAPI.LOGGER.info("All registered. (Took {}ms)", System.currentTimeMillis() - startInitTime);
    }
}
