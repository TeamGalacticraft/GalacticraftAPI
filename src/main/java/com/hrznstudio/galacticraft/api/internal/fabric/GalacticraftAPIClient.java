package com.hrznstudio.galacticraft.api.internal.fabric;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GalacticraftAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AddonRegistry.runRegistryCallbacks();
    }
}
