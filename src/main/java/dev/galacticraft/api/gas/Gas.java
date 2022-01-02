/*
 * Copyright (c) 2019-2022 Team Galacticraft
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

package dev.galacticraft.api.gas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.impl.Constant;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public record Gas(TranslatableText name, String symbol) {
    public static final Codec<Gas> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(i -> i.name),
                    Codec.STRING.fieldOf("symbol").forGetter(i -> i.symbol)
            ).apply(instance, Gas::new)
    );

    public static final Identifier HYDROGEN_ID = new Identifier(Constant.MOD_ID, "hydrogen");
    public static final Gas HYDROGEN = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.hydrogen"),
            "H"
    );
    public static final Identifier NITROGEN_ID = new Identifier(Constant.MOD_ID, "nitrogen");
    public static final Gas NITROGEN = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.nitrogen"),
            "N"
    );
    public static final Identifier OXYGEN_ID = new Identifier(Constant.MOD_ID, "oxygen");
    public static final RegistryKey<Gas> OXYGEN_KEY = RegistryKey.of(AddonRegistry.GAS_KEY, OXYGEN_ID);
    public static final Gas OXYGEN = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.oxygen"),
            "O2"
    );
    public static final Identifier CARBON_DIOXIDE_ID = new Identifier(Constant.MOD_ID, "carbon_dioxide");
    public static final Gas CARBON_DIOXIDE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.carbon_dioxide"),
            "CO2"
    );
    public static final Identifier WATER_VAPOR_ID = new Identifier(Constant.MOD_ID, "water_vapor");
    public static final Gas WATER_VAPOR = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.water_vapor"),
            "H2O"
    );
    public static final Identifier METHANE_ID = new Identifier(Constant.MOD_ID, "methane");
    public static final Gas METHANE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.methane"),
            "CH4"
    );
    public static final Identifier HELIUM_ID = new Identifier(Constant.MOD_ID, "helium");
    public static final Gas HELIUM = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.helium"),
            "He"
    );
    public static final Identifier ARGON_ID = new Identifier(Constant.MOD_ID, "argon");
    public static final Gas ARGON = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.argon"),
            "Ar"
    );
    public static final Identifier NEON_ID = new Identifier(Constant.MOD_ID, "neon");
    public static final Gas NEON = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.neon"),
            "Ne"
    );
    public static final Identifier KRYPTON_ID = new Identifier(Constant.MOD_ID, "krypton");
    public static final Gas KRYPTON = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.krypton"),
            "Kr"
    );
    public static final Identifier NITROUS_OXIDE_ID = new Identifier(Constant.MOD_ID, "nitrous_oxide");
    public static final Gas NITROUS_OXIDE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.nitrous_oxide"),
            "N2O"
    );
    public static final Identifier CARBON_MONOXIDE_ID = new Identifier(Constant.MOD_ID, "carbon_monoxide");
    public static final Gas CARBON_MONOXIDE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.carbon_monoxide"),
            "CO"
    );
    public static final Identifier XENON_ID = new Identifier(Constant.MOD_ID, "xenon");
    public static final Gas XENON = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.xenon"),
            "Xe"
    );
    public static final Identifier OZONE_ID = new Identifier(Constant.MOD_ID, "ozone");
    public static final Gas OZONE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.ozone"),
            "O3"
    );
    public static final Identifier NITROUS_DIOXIDE_ID = new Identifier(Constant.MOD_ID, "nitrous_dioxide");
    public static final Gas NITROUS_DIOXIDE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.nitrous_dioxide"),
            "NO2"
    );
    public static final Identifier IODINE_ID = new Identifier(Constant.MOD_ID, "iodine");
    public static final Gas IODINE = new Gas(
            new TranslatableText("ui.galacticraft-api.gases.iodine"),
            "I2"
    );

    public static Registry<Gas> getRegistry(DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.GAS_KEY);
    }

    public static Gas getById(DynamicRegistryManager manager, Identifier id) {
        return getById(getRegistry(manager), id);
    }

    public static Identifier getId(DynamicRegistryManager manager, Gas gas) {
        return getId(getRegistry(manager), gas);
    }

    public static boolean containsSymbol(DynamicRegistryManager manager, String symbol) {
        return containsSymbol(getRegistry(manager), symbol);
    }

    public static Gas getBySymbol(DynamicRegistryManager manager, String symbol) {
        return getBySymbol(getRegistry(manager), symbol);
    }

    public static Gas getById(Registry<Gas> registry, Identifier id) {
        return registry.get(id);
    }

    public static Identifier getId(Registry<Gas> registry, Gas gas) {
        return registry.getId(gas);
    }

    public static boolean containsSymbol(Registry<Gas> registry, String symbol) {
        for (Gas g : registry) {
            if (g.symbol().equals(symbol)) return true;
        }
        return false;
    }

    public static Gas getBySymbol(Registry<Gas> registry, String symbol) {
        for (Gas g : registry) {
            if (g.symbol().equals(symbol)) return g;
        }
        return null;
    }

    public String symbolForDisplay() {
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

    @Override
    public String toString() {
        return this.symbolForDisplay();
    }
}
