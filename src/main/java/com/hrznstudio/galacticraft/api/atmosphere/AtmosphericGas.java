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

package com.hrznstudio.galacticraft.api.atmosphere;

import com.hrznstudio.galacticraft.api.addon.AddonRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AtmosphericGas {

    public static final AtmosphericGas HYDROGEN = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "hydrogen"),
                    "ui.galacticraft-api.gases.hydrogen",
                    "H"
            )
    );
    public static final AtmosphericGas NITROGEN = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "nitrogen"),
                    "ui.galacticraft-api.gases.nitrogen",
                    "N"
            )
    );
    public static final AtmosphericGas OXYGEN = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "oxygen"),
                    "ui.galacticraft-api.gases.oxygen",
                    "O2"
            )
    );
    public static final AtmosphericGas CARBON_DIOXIDE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "carbon_dioxide"),
                    "ui.galacticraft-api.gases.carbon_dioxide",
                    "CO2"
            )
    );
    public static final AtmosphericGas WATER_VAPOR = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "water_vapor"),
                    "ui.galacticraft-api.gases.water_vapor",
                    "H2O"
            )
    );
    public static final AtmosphericGas METHANE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "methane"),
                    "ui.galacticraft-api.gases.methane",
                    "CH4"
            )
    );
    public static final AtmosphericGas HELIUM = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "helium"),
                    "ui.galacticraft-api.gases.helium",
                    "He"
            )
    );
    public static final AtmosphericGas ARGON = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "argon"),
                    "ui.galacticraft-api.gases.argon",
                    "Ar"
            )
    );
    public static final AtmosphericGas NEON = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "neon"),
                    "ui.galacticraft-api.gases.neon",
                    "Ne"
            )
    );
    public static final AtmosphericGas KRYPTON = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "krypton"),
                    "ui.galacticraft-api.gases.krypton",
                    "Kr"
            )
    );
    public static final AtmosphericGas NITROUS_OXIDE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "nitrous_oxide"),
                    "ui.galacticraft-api.gases.nitrous_oxide",
                    "N2O"
            )
    );
    public static final AtmosphericGas CARBON_MONOXIDE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "carbon_monoxide"),
                    "ui.galacticraft-api.gases.carbon_monoxide",
                    "CO"
            )
    );
    public static final AtmosphericGas XENON = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "xenon"),
                    "ui.galacticraft-api.gases.xenon",
                    "Xe"
            )
    );
    public static final AtmosphericGas OZONE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "ozone"),
                    "ui.galacticraft-api.gases.ozone",
                    "O3"
            )
    );
    public static final AtmosphericGas NITROUS_DIOXIDE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "nitrous_dioxide"),
                    "ui.galacticraft-api.gases.nitrous_dioxide",
                    "NO2"
            )
    );
    public static final AtmosphericGas IODINE = register(
            new AtmosphericGas(
                    new Identifier("galacticraft-api", "iodine"),
                    "ui.galacticraft-api.gases.iodine",
                    "I2"
            )
    );
    private static AtmosphericGas register(AtmosphericGas gas) {
        if (AtmosphericGas.containsSymbol(gas.symbol)) return AtmosphericGas.getBySymbol(gas.symbol);
        return Registry.register(AddonRegistry.ATMOSPHERIC_GASES, gas.getId(), gas);
    }

    private final Identifier id;
    private final String translationKey;
    private final String symbol;

    /**
     *
     * @param id Unique identifier
     * @param translationKey Key to translate the gas' name
     * @param symbol the symbol used to show the gas
     */
    public AtmosphericGas(Identifier id, String translationKey, String symbol) {
        this.id = id;
        this.translationKey = translationKey;
        this.symbol = symbol;
    }

    public Identifier getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSymbolForDisplay() {
        return this.symbol
                .replaceAll("0", "\u2080")
                .replaceAll("1", "\u2081")
                .replaceAll("2", "\u2082")
                .replaceAll("3", "\u2083")
                .replaceAll("4", "\u2084")
                .replaceAll("6", "\u2085")
                .replaceAll("7", "\u2086")
                .replaceAll("8", "\u2087")
                .replaceAll("9", "\u2088");
    }

    public static AtmosphericGas deserialize(Dynamic<?> dynamic) {
        return AddonRegistry.ATMOSPHERIC_GASES.get(new Identifier(dynamic.asString("")));
    }

    public static Iterable<AtmosphericGas> getAll() {
        return AddonRegistry.ATMOSPHERIC_GASES;
    }

    public static AtmosphericGas getById(Identifier id) {
        return AddonRegistry.ATMOSPHERIC_GASES.get(id);
    }

    public static Identifier getId(AtmosphericGas gas) {
        return AddonRegistry.ATMOSPHERIC_GASES.getId(gas);
    }

    public static boolean containsSymbol(String symbol) {
        for(AtmosphericGas g : AddonRegistry.ATMOSPHERIC_GASES) {
            if(g.symbol.equals(symbol)) return true;
        }
        return false;
    }

    public static AtmosphericGas getBySymbol(String symbol) {
        for(AtmosphericGas g : AddonRegistry.ATMOSPHERIC_GASES) {
            if(g.symbol.equals(symbol)) return g;
        }
        return null;
    }

    @Override
    public String toString() {
        return getId(this).toString();
    }
}
