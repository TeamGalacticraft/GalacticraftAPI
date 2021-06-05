/*
 * Copyright (c) 2019-2021 Team Galacticraft
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

package dev.galacticraft.api.atmosphere;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.impl.internal.fabric.GalacticraftAPI;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;

public record AtmosphericGas(TranslatableText name, String symbol) {
    public static final Codec<AtmosphericGas> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(i -> i.name),
                    Codec.STRING.fieldOf("symbol").forGetter(i -> i.symbol)
            ).apply(instance, AtmosphericGas::new)
    );

    public static final Identifier HYDROGEN_ID = new Identifier(GalacticraftAPI.MOD_ID, "hydrogen");
    public static final AtmosphericGas HYDROGEN = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.hydrogen"),
            "H"
    );
    public static final Identifier NITROGEN_ID = new Identifier(GalacticraftAPI.MOD_ID, "nitrogen");
    public static final AtmosphericGas NITROGEN = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.nitrogen"),
            "N"
    );
    public static final Identifier OXYGEN_ID = new Identifier(GalacticraftAPI.MOD_ID, "oxygen");
    public static final AtmosphericGas OXYGEN = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.oxygen"),
            "O2"
    );
    public static final Identifier CARBON_DIOXIDE_ID = new Identifier(GalacticraftAPI.MOD_ID, "carbon_dioxide");
    public static final AtmosphericGas CARBON_DIOXIDE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.carbon_dioxide"),
            "CO2"
    );
    public static final Identifier WATER_VAPOR_ID = new Identifier(GalacticraftAPI.MOD_ID, "water_vapor");
    public static final AtmosphericGas WATER_VAPOR = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.water_vapor"),
            "H2O"
    );
    public static final Identifier METHANE_ID = new Identifier(GalacticraftAPI.MOD_ID, "methane");
    public static final AtmosphericGas METHANE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.methane"),
            "CH4"
    );
    public static final Identifier HELIUM_ID = new Identifier(GalacticraftAPI.MOD_ID, "helium");
    public static final AtmosphericGas HELIUM = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.helium"),
            "He"
    );
    public static final Identifier ARGON_ID = new Identifier(GalacticraftAPI.MOD_ID, "argon");
    public static final AtmosphericGas ARGON = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.argon"),
            "Ar"
    );
    public static final Identifier NEON_ID = new Identifier(GalacticraftAPI.MOD_ID, "neon");
    public static final AtmosphericGas NEON = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.neon"),
            "Ne"
    );
    public static final Identifier KRYPTON_ID = new Identifier(GalacticraftAPI.MOD_ID, "krypton");
    public static final AtmosphericGas KRYPTON = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.krypton"),
            "Kr"
    );
    public static final Identifier NITROUS_OXIDE_ID = new Identifier(GalacticraftAPI.MOD_ID, "nitrous_oxide");
    public static final AtmosphericGas NITROUS_OXIDE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.nitrous_oxide"),
            "N2O"
    );
    public static final Identifier CARBON_MONOXIDE_ID = new Identifier(GalacticraftAPI.MOD_ID, "carbon_monoxide");
    public static final AtmosphericGas CARBON_MONOXIDE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.carbon_monoxide"),
            "CO"
    );
    public static final Identifier XENON_ID = new Identifier(GalacticraftAPI.MOD_ID, "xenon");
    public static final AtmosphericGas XENON = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.xenon"),
            "Xe"
    );
    public static final Identifier OZONE_ID = new Identifier(GalacticraftAPI.MOD_ID, "ozone");
    public static final AtmosphericGas OZONE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.ozone"),
            "O3"
    );
    public static final Identifier NITROUS_DIOXIDE_ID = new Identifier(GalacticraftAPI.MOD_ID, "nitrous_dioxide");
    public static final AtmosphericGas NITROUS_DIOXIDE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.nitrous_dioxide"),
            "NO2"
    );
    public static final Identifier IODINE_ID = new Identifier(GalacticraftAPI.MOD_ID, "iodine");
    public static final AtmosphericGas IODINE = new AtmosphericGas(
            new TranslatableText("ui.galacticraft-api.gases.iodine"),
            "I2"
    );
    
    public String getSymbolForDisplay() {
        return this.symbol()
                .replaceAll("0", "\u2080")
                .replaceAll("1", "\u2081")
                .replaceAll("2", "\u2082")
                .replaceAll("3", "\u2083")
                .replaceAll("4", "\u2084")
                .replaceAll("5", "\u2085")
                .replaceAll("6", "\u2086")
                .replaceAll("7", "\u2087")
                .replaceAll("8", "\u2088")
                .replaceAll("9", "\u2089");
    }

    public static AtmosphericGas deserialize(DynamicRegistryManager manager, Dynamic<?> dynamic) {
        return manager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY).get(new Identifier(dynamic.asString("")));
    }

    public static Registry<AtmosphericGas> get(DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY);
    }

    public static AtmosphericGas getById(DynamicRegistryManager manager, Identifier id) {
        return manager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY).get(id);
    }

    public static Identifier getId(DynamicRegistryManager manager, AtmosphericGas gas) {
        return manager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY).getId(gas);
    }

    public static boolean containsSymbol(DynamicRegistryManager manager, String symbol) {
        for (AtmosphericGas g : get(manager)) {
            if (g.symbol.equals(symbol)) return true;
        }
        return false;
    }

    public static AtmosphericGas getBySymbol(DynamicRegistryManager manager, String symbol) {
        for (AtmosphericGas g : get(manager)) {
            if (g.symbol.equals(symbol)) return g;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getSymbolForDisplay();
    }
}
