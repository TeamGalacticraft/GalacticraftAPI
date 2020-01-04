/*
 * Copyright (c) 2020 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.api.internal.fabric;

import com.hrznstudio.galacticraft.api.addon.GCAddonInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GalacticraftAPIMain implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        long startInitTime = System.currentTimeMillis();
        LOGGER.info("[GC-API] Starting Initialization.");

        // GC detection.
        // only initialize if gc is present.
        LOGGER.info("[GC-API] Searching for Galacticraft.");
        if(FabricLoader.getInstance().isModLoaded("galacticraft-rewoven")) {
            LOGGER.info("[GC-API] Found Galacticraft, continuing initialization.");
        } else {
            LOGGER.info("[GC-API] Galacticraft not found, stopping addon initialization.");
            if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
                LOGGER.warn("[GC-API] If you are developing an addon, you may have forgotten to add Galacticraft to your build.gradle.");
            }
        }

        // Addon detection
        LOGGER.info("[GC-API] Scanning for Addons...");
        long startAddonInitTime = System.currentTimeMillis();
        List<GCAddonInitializer> addonInitializers = FabricLoader.getInstance().getEntrypoints("gc_addons", com.hrznstudio.galacticraft.api.addon.GCAddonInitializer.class);
        LOGGER.info("[GC-API] Addon scan complete, found {} addons.", addonInitializers.size());
        int addonSuccess = 0;
        int addonFailure = 0;
        for (int i = 0; i < addonInitializers.size(); i++) {
            ModContainer container = FabricLoader.getInstance().getModContainer(addonInitializers.get(i).getModId()).get();
            LOGGER.info("[GC-API] Initializing Addon entry point for {} (v{}).", container.getMetadata().getName(), container.getMetadata().getVersion().getFriendlyString());

            if(addonInitializers.get(i).onInitialize()) addonSuccess++;
            else addonFailure++;
        }
        LOGGER.info("[GC-API] Addon initialization complete. Loaded {} successfully and {} failed to load, {} total. (Took {}ms).", addonSuccess, addonFailure, addonSuccess + addonFailure, System.currentTimeMillis()-startAddonInitTime);



        LOGGER.info("[GC-API] Initialization Complete. (Took {}ms).", System.currentTimeMillis()-startInitTime);
    }
}
