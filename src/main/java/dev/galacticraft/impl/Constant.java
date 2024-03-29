/*
 * Copyright (c) 2019-2023 Team Galacticraft
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

package dev.galacticraft.impl;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface Constant {
    String MOD_ID = "galacticraft-api";
    Logger LOGGER = LogManager.getLogger("GalacticraftAPI");

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ResourceLocation id(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    interface Nbt {
        String GC_API = "GCApi";
        String CHANGE_COUNT = "Modified";
        String OXYGEN = "Inversion";
        String GEAR_INV = "GearInv";
    }

    interface Chunk {
        int WIDTH = 16;
        int SECTION_HEIGHT = 16;
        int CHUNK_SECTION_AREA = WIDTH * WIDTH * SECTION_HEIGHT;
    }

    interface TranslationKey {
        String HYDROGEN = "gas.galacticraft-api.hydrogen";
        String NITROGEN = "gas.galacticraft-api.nitrogen";
        String OXYGEN = "gas.galacticraft-api.oxygen";
        String CARBON_DIOXIDE = "gas.galacticraft-api.carbon_dioxide";
        String CARBON_MONOXIDE = "gas.galacticraft-api.carbon_monoxide";
        String WATER_VAPOR = "gas.galacticraft-api.water_vapor";
        String METHANE = "gas.galacticraft-api.methane";
        String HELIUM = "gas.galacticraft-api.helium";
        String ARGON = "gas.galacticraft-api.argon";
        String NITROUS_OXIDE = "gas.galacticraft-api.nitrous_oxide";
        String NEON = "gas.galacticraft-api.neon";
        String KRYPTON = "gas.galacticraft-api.krypton";
        String XENON = "gas.galacticraft-api.xenon";
        String OZONE = "gas.galacticraft-api.ozone";
        String NITROUS_DIOXIDE = "gas.galacticraft-api.nitrous_dioxide";
        String IODINE = "gas.galacticraft-api.iodine";
    }
    
    interface Misc {
        ResourceLocation INVALID = new ResourceLocation(Constant.MOD_ID, "invalid");
    }
}
