package com.hrznstudio.galacticraft.api.internal.fabric;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class GalacticraftAPIServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        AddonRegistry.runRegistryCallbacks();
    }
}
