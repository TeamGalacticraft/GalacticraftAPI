package com.hrznstudio.galacticraft.testmod;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GCApiTestMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void onInitialize() {
        LOGGER.info("Loaded test mod!");
    }
}
