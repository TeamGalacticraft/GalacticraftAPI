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

import com.hrznstudio.galacticraft.api.addon.AddonInitializer;
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
        List<AddonInitializer> addonInitializers = FabricLoader.getInstance().getEntrypoints("gc_addons", AddonInitializer.class);

        // compat init
        LOGGER.info("[GC-API] Initializing compatibility method.");
        for (AddonInitializer addonInitializer : addonInitializers) {
            if(FabricLoader.getInstance().getModContainer(addonInitializer.getModId()).isPresent()) {
                ModContainer container = FabricLoader.getInstance().getModContainer(addonInitializer.getModId()).get();
                LOGGER.info("[GC-API] Initializing Compat entry point for {} (v{}).", container.getMetadata().getName(), container.getMetadata().getVersion().getFriendlyString());
                addonInitializer.onCompatInitialize();
            } else {
                LOGGER.warn("[GC-API] Mod not found with ID \"{}\".", addonInitializer.getModId());
            }
        }

        // GC detection.
        // only initialize if gc is present.
        LOGGER.info("[GC-API] Searching for Galacticraft.");
        if(FabricLoader.getInstance().isModLoaded("galacticraft-rewoven")) {
            // Addon detection
            LOGGER.info("[GC-API] Scanning for Addons...");
            long startAddonInitTime = System.currentTimeMillis();
            LOGGER.info("[GC-API] Addon scan complete, found {} addons.", addonInitializers.size());
            for (AddonInitializer addonInitializer : addonInitializers) {
                if(FabricLoader.getInstance().getModContainer(addonInitializer.getModId()).isPresent()) {
                    ModContainer container = FabricLoader.getInstance().getModContainer(addonInitializer.getModId()).get();
                    LOGGER.info("[GC-API] Initializing Addon entry point for {} (v{}).", container.getMetadata().getName(), container.getMetadata().getVersion().getFriendlyString());

                    addonInitializer.onAddonInitialize();
                } else {
                    LOGGER.warn("[GC-API] Mod not found with ID \"{}\".", addonInitializer.getModId());
                }
            }
            LOGGER.info("[GC-API] Addon initialization complete. (Took {}ms)", System.currentTimeMillis()-startAddonInitTime);
        } else {
            LOGGER.info("[GC-API] Galacticraft not found, stopping addon initialization.");
            if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
                LOGGER.warn("[GC-API] If you are developing an addon, you may have forgotten to add Galacticraft to your build.gradle.");
            }
        }

        LOGGER.info("[GC-API] Initialization Complete. (Took {}ms).", System.currentTimeMillis()-startInitTime);
    }
}
