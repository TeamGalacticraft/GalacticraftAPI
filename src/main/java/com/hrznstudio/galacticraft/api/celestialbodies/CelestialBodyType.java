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
import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericInfo;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.DynamicSerializable;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class CelestialBodyType implements DynamicSerializable {

    public static final CelestialBodyType THE_SUN = register(
            new CelestialBodyType(
                    new Identifier("galacticraft-api", "the_sun"),
                    "ui.galacticraft-api.bodies.the_sun",
                    null,
                    -1,
                    null,
                    0d,
                    0f,
                    new AtmosphericInfo( // blank atmosphere because you cannot visit the sun
                            new HashMap<>(),
                            0d,
                            0f
                    )
            )
    );
    public static final CelestialBodyType EARTH = register(
            new CelestialBodyType(
                    new Identifier("galacticraft-api", "earth"),
                    "ui.galacticraft-api.bodies.earth",
                    DimensionType.OVERWORLD,
                    0, THE_SUN,
                    10d,
                    1.0f,
                    new AtmosphericInfo(
                            new HashMap<AtmosphericGas, Double>() {{
                               put(AtmosphericGas.NITROGEN,       780840d    );
                               put(AtmosphericGas.OXYGEN,         209500d    );
                               put(AtmosphericGas.WATER_VAPOR,     25000d    );
                               put(AtmosphericGas.ARGON,            9300d    );
                               put(AtmosphericGas.CARBON_DIOXIDE,    399d    );
                               put(AtmosphericGas.NEON,               18d    );
                               put(AtmosphericGas.HELIUM,              5.42d );
                               put(AtmosphericGas.METHANE,             1.79d );
                               put(AtmosphericGas.KRYPTON,             1.14d );
                               put(AtmosphericGas.HYDROGEN,            0.55d );
                               put(AtmosphericGas.NITROUS_OXIDE,       0.325d);
                               put(AtmosphericGas.CARBON_MONOXIDE,     0.1d  );
                               put(AtmosphericGas.XENON,               0.09d );
                               put(AtmosphericGas.OZONE,               0.07d );
                               put(AtmosphericGas.NITROUS_DIOXIDE,     0.02d );
                               put(AtmosphericGas.IODINE,              0.01d );
                            }},
                            15d,
                            1.0f
                    )
            )
    );

    private final Identifier id;
    private final String translationKey;
    private final DimensionType dimension;
    private final int accessWeight;
    private final CelestialBodyType parent;
    private final double orbit;
    private final float gravity;
    private final AtmosphericInfo atmosphere;

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
     * @param atmosphere The atmosphere of the body
     */
    public CelestialBodyType(Identifier id, String translationKey, DimensionType dimension, int accessWeight, CelestialBodyType parent, double orbit, float gravity, AtmosphericInfo atmosphere) {
        this.id = id;
        this.translationKey = translationKey;
        this.dimension = dimension;
        this.accessWeight = accessWeight;
        this.parent = parent;
        this.orbit = orbit;
        this.gravity = gravity;
        this.atmosphere = atmosphere;
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
    public static CelestialBodyType getById(Identifier id) {
        return AddonRegistry.CELESTIAL_BODIES.get(id);
    }

    public static Optional<CelestialBodyType> getByDimType(DimensionType dimensionType) {
        return AddonRegistry.CELESTIAL_BODIES.stream().filter(celestialBodyType -> celestialBodyType.getDimension() == dimensionType).findFirst();
    }

    public Identifier getId() {
        return id;
    }

    public static Identifier getId(CelestialBodyType type) {
        return AddonRegistry.CELESTIAL_BODIES.getId(type);
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

    public AtmosphericInfo getAtmosphere() {
        return atmosphere;
    }

    @Override
    public <T> T serialize(DynamicOps<T> ops) {
        return ops.createString(AddonRegistry.CELESTIAL_BODIES.getId(this).toString());
    }

    public static CelestialBodyType deserialize(Dynamic<?> dynamic) {
        return AddonRegistry.CELESTIAL_BODIES.get(new Identifier(dynamic.asString("")));
    }

    @Override
    public String toString() {
        return getId(this).toString();
    }
}
