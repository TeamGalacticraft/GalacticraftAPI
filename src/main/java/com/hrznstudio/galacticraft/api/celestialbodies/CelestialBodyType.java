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

package com.hrznstudio.galacticraft.api.celestialbodies;

import com.hrznstudio.galacticraft.api.addon.AddonRegistry;
import com.mojang.datafixers.types.DynamicOps;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.util.DynamicSerializable;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.Optional;

public class CelestialBodyType implements DynamicSerializable {

    public static final CelestialBodyType THE_SUN;
    public static final CelestialBodyType EARTH;

    private final Identifier id;
    private final String translationKey;
    @Nullable private final DimensionType dimension;
    private final int accessWeight;
    @Nullable private final CelestialBodyType parent;
    private final double orbit;
    private final float gravity;
    private final boolean oxygen;

    private static CelestialBodyType register(CelestialBodyType type) {
        return Registry.register(AddonRegistry.CELESTIAL_BODIES, type.getId(), type);
    }

    /**
     * Used to register a new Celestial Body. {@link AddonRegistry#CELESTIAL_BODIES}
     * @param id Unique identifier
     * @param translationKey Key used to translate the body's name
     * @param dimension The dimension the body type is for
     * @param accessWeight The rocket tier/access weight for the body. (-1 for inaccessible)
     * @param parent Parent body.
     * @param orbit The orbit distance around the parent body
     * @param gravity The gravity applied to entities on the body (1.0f is the same as the overworld)
     * @param oxygen Whether or not the body has oxygen.
     */
    public CelestialBodyType(Identifier id, String translationKey, @Nullable DimensionType dimension, int accessWeight, @Nullable CelestialBodyType parent, double orbit, float gravity, boolean oxygen) {
        this.id = id;
        this.translationKey = translationKey;
        this.dimension = dimension;
        this.accessWeight = accessWeight;
        this.parent = parent;
        this.orbit = orbit;
        this.gravity = gravity;
        this.oxygen = oxygen;
    }

    /**
     *
     * @return all registered Celestial Bodies
     */
    public static Iterable<CelestialBodyType> getAll() {
        return AddonRegistry.CELESTIAL_BODIES;
    }

    /**
     *
     * @param id The identifier of the body
     * @return the celestial body or null
     */
    @Nullable
    public static CelestialBodyType getById(Identifier id) {
        return AddonRegistry.CELESTIAL_BODIES.get(id);
    }

    public static Optional<CelestialBodyType> getByDimType(DimensionType dimensionType) {
        return AddonRegistry.CELESTIAL_BODIES.stream().filter(celestialBodyType -> celestialBodyType.getDimension() == dimensionType).findFirst();
    }

    public Identifier getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public DimensionType getDimension() {
        return dimension;
    }

    public int getAccessWeight() {
        return accessWeight;
    }

    public CelestialBodyType getParent() {
        return parent;
    }

    public double getOrbit() {
        return orbit;
    }

    public float getGravity() {
        return gravity;
    }

    public boolean hasOxygen() {
        return oxygen;
    }

    @Override
    public <T> T serialize(DynamicOps<T> var1) {
        return null;
    }

    static {
        THE_SUN = register(new CelestialBodyType(new Identifier("galacticraft-api", "the_sun"), "ui.galacticraft-api.bodies.the_sun", null, -1, null, 0d, 0f, false));
        EARTH = register(new CelestialBodyType(new Identifier("galacticraft-api", "earth"), "ui.galacticraft-api.bodies.earth", DimensionType.OVERWORLD, 0, THE_SUN, 10d, 1.0f, true));
    }
}
