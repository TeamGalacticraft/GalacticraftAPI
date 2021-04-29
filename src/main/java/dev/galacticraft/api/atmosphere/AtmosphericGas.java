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

import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.registry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryElementCodec;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;

import java.util.function.Supplier;

public class AtmosphericGas {
    public static final Codec<AtmosphericGas> CODEC = RecordCodecBuilder.create(atmosphericGasInstance ->
        atmosphericGasInstance.group(
                Identifier.CODEC.fieldOf("id").forGetter(i -> i.id),
                Codec.STRING.fieldOf("translation_key").forGetter(i -> i.translationKey),
                Codec.STRING.fieldOf("symbol").forGetter(i -> i.symbol)
        ).apply(atmosphericGasInstance, AtmosphericGas::new)
    );

    public static final Codec<Supplier<AtmosphericGas>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.ATMOSPHERIC_GAS_KEY, CODEC);

    public static final AtmosphericGas HYDROGEN = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "hydrogen"),
            "ui.galacticraft-api.gases.hydrogen",
            "H"
    );
    public static final AtmosphericGas NITROGEN = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "nitrogen"),
            "ui.galacticraft-api.gases.nitrogen",
            "N"
    );
    public static final AtmosphericGas OXYGEN = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "oxygen"),
            "ui.galacticraft-api.gases.oxygen",
            "O2"
    );
    public static final AtmosphericGas CARBON_DIOXIDE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "carbon_dioxide"),
            "ui.galacticraft-api.gases.carbon_dioxide",
            "CO2"
    );
    public static final AtmosphericGas WATER_VAPOR = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "water_vapor"),
            "ui.galacticraft-api.gases.water_vapor",
            "H2O"
    );
    public static final AtmosphericGas METHANE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "methane"),
            "ui.galacticraft-api.gases.methane",
            "CH4"
    );
    public static final AtmosphericGas HELIUM = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "helium"),
            "ui.galacticraft-api.gases.helium",
            "He"
    );
    public static final AtmosphericGas ARGON = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "argon"),
            "ui.galacticraft-api.gases.argon",
            "Ar"
    );
    public static final AtmosphericGas NEON = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "neon"),
            "ui.galacticraft-api.gases.neon",
            "Ne"
    );
    public static final AtmosphericGas KRYPTON = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "krypton"),
            "ui.galacticraft-api.gases.krypton",
            "Kr"
    );
    public static final AtmosphericGas NITROUS_OXIDE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "nitrous_oxide"),
            "ui.galacticraft-api.gases.nitrous_oxide",
            "N2O"
    );
    public static final AtmosphericGas CARBON_MONOXIDE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "carbon_monoxide"),
            "ui.galacticraft-api.gases.carbon_monoxide",
            "CO"
    );
    public static final AtmosphericGas XENON = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "xenon"),
            "ui.galacticraft-api.gases.xenon",
            "Xe"
    );
    public static final AtmosphericGas OZONE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "ozone"),
            "ui.galacticraft-api.gases.ozone",
            "O3"
    );
    public static final AtmosphericGas NITROUS_DIOXIDE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "nitrous_dioxide"),
            "ui.galacticraft-api.gases.nitrous_dioxide",
            "NO2"
    );
    public static final AtmosphericGas IODINE = new AtmosphericGas(
            new Identifier(GalacticraftAPI.MOD_ID, "iodine"),
            "ui.galacticraft-api.gases.iodine",
            "I2"
    );

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
                .replaceAll("5", "\u2085")
                .replaceAll("6", "\u2086")
                .replaceAll("7", "\u2087")
                .replaceAll("8", "\u2088")
                .replaceAll("9", "\u2089");
    }

    public static AtmosphericGas deserialize(DynamicRegistryManager registryManager, Dynamic<?> dynamic) {
        return registryManager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY).get(new Identifier(dynamic.asString("")));
    }

    public static MutableRegistry<AtmosphericGas> getAll(DynamicRegistryManager registryManager) {
        return registryManager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY);
    }

    public static AtmosphericGas getById(DynamicRegistryManager registryManager, Identifier id) {
        return registryManager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY).get(id);
    }

    public static Identifier getId(DynamicRegistryManager registryManager, AtmosphericGas gas) {
        return registryManager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY).getId(gas);
    }

    public static boolean containsSymbol(DynamicRegistryManager registryManager, String symbol) {
        for(AtmosphericGas g : getAll(registryManager)) {
            if(g.symbol.equals(symbol)) return true;
        }
        return false;
    }

    public static AtmosphericGas getBySymbol(DynamicRegistryManager registryManager, String symbol) {
        for(AtmosphericGas g : getAll(registryManager)) {
            if(g.symbol.equals(symbol)) return g;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getSymbolForDisplay();
    }
}
