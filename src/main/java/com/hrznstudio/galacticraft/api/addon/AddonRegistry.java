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

package com.hrznstudio.galacticraft.api.addon;

import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.internal.mixin.RegistryAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.function.Supplier;

public abstract class AddonRegistry<T> extends Registry<T> {
    public static final Registry<CelestialBodyType> CELESTIAL_BODIES = create(new Identifier("galacticraft-api", "celestial_bodies"), () -> CelestialBodyType.THE_SUN);

    public static final Registry<AtmosphericGas> ATMOSPHERIC_GASES = create(new Identifier("galacticraft-api", "atmospheric_gases"), () -> AtmosphericGas.OXYGEN);

    private static <T> Registry<T> create(Identifier identifier, Supplier<T> defaultObject) {
        return putDefaultEntry(identifier, new SimpleRegistry<>(), defaultObject);
    }

    private static <T, R extends MutableRegistry<T>> R putDefaultEntry(Identifier identifier, R mutableRegistry_1, Supplier<T> supplier_1) {
        RegistryAccessor.getDefaultEntries().put(identifier, supplier_1);
        return (R) REGISTRIES.add(identifier, mutableRegistry_1);
    }
}
