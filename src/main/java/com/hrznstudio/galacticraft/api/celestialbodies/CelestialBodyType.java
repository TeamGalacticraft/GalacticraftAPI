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

import java.util.Optional;

public class CelestialBodyType implements DynamicSerializable {
    public static final CelestialBodyType THE_SUN = register(
            new CelestialBodyType.Builder(new Identifier("galacticraft-api", "the_sun"))
                    .translationKey("ui.galacticraft-api.bodies.the_sun")
                    .display(
                            new CelestialBodyDisplayInfo.Builder()
                                .texture(new Identifier("galacticraft-api", "body_icons"))
                                .build()
                    )
                    .build()
    );

    public static final CelestialBodyType EARTH = register(
            new Builder(new Identifier("galacticraft-api", "earth"))
                    .translationKey("ui.galacticraft-api.bodies.earth")
                    .dimension(DimensionType.OVERWORLD)
                    .weight(0)
                    .display(
                            new CelestialBodyDisplayInfo.Builder()
                                    .texture(new Identifier("galacticraft-api", "body_icons"))
                                    .y(16)
                                    .build()
                    )
                    .atmosphere(
                            new AtmosphericInfo.Builder()
                                    .pressure(1.0f)
                                    .temperature(15.0f)
                                    .gas(AtmosphericGas.NITROGEN,       780840d     )
                                    .gas(AtmosphericGas.OXYGEN,         209500d     )
                                    .gas(AtmosphericGas.WATER_VAPOR,     25000d     )
                                    .gas(AtmosphericGas.ARGON,            9300d     )
                                    .gas(AtmosphericGas.CARBON_DIOXIDE,    399d     )
                                    .gas(AtmosphericGas.NEON,               18d     )
                                    .gas(AtmosphericGas.HELIUM,              5.42d  )
                                    .gas(AtmosphericGas.METHANE,             1.79d  )
                                    .gas(AtmosphericGas.KRYPTON,             1.14d  )
                                    .gas(AtmosphericGas.HYDROGEN,            0.55d  )
                                    .gas(AtmosphericGas.NITROUS_OXIDE,       0.325d )
                                    .gas(AtmosphericGas.CARBON_MONOXIDE,     0.1d   )
                                    .gas(AtmosphericGas.XENON,               0.09d  )
                                    .gas(AtmosphericGas.OZONE,               0.07d  )
                                    .gas(AtmosphericGas.IODINE,              0.01d  )
                                    .gas(AtmosphericGas.NITROUS_DIOXIDE,     0.02d  )
                                    .build()
                    )
                    .build()
    );
    private final Identifier id;
    private final String translationKey;
    private final DimensionType dimension;
    private final int accessWeight;
    private final CelestialBodyType parent;
    private final CelestialBodyDisplayInfo displayInfo;
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
     * @param displayInfo Information used to display the body
     * @param gravity The gravity applied to entities on the body (1.0f is the same as the overworld)
     * @param atmosphere The atmosphere of the body
     */
    public CelestialBodyType(Identifier id, String translationKey, DimensionType dimension, int accessWeight, CelestialBodyType parent, CelestialBodyDisplayInfo displayInfo, float gravity, AtmosphericInfo atmosphere) {
        this.id = id;
        this.translationKey = translationKey;
        this.dimension = dimension;
        this.accessWeight = accessWeight;
        this.parent = parent;
        this.displayInfo = displayInfo;
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

    public CelestialBodyDisplayInfo getDisplayInfo() {
        return displayInfo;
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

    public static class Builder {
        private final Identifier id;
        private String translationKey;
        private DimensionType dimension = null;
        private int accessWeight = -1;
        private CelestialBodyType parent = THE_SUN;
        private CelestialBodyDisplayInfo displayInfo = null;
        private float gravity = 1.0f;
        private AtmosphericInfo atmosphere = new AtmosphericInfo.Builder().build();

        public Builder(Identifier id) {
            this.id = id;
            this.translationKey = id.toString();
        }

        public Builder translationKey(String key) {
            this.translationKey = key;
            return this;
        }

        public Builder dimension(DimensionType dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder weight(int accessWeight) {
            this.accessWeight = accessWeight;
            return this;
        }

        public Builder parent(CelestialBodyType parent) {
            this.parent = parent;
            return this;
        }

        public Builder display(CelestialBodyDisplayInfo displayInfo) {
            this.displayInfo = displayInfo;
            return this;
        }

        public Builder gravity(float gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder atmosphere(AtmosphericInfo atmosphericInfo) {
            this.atmosphere = atmosphericInfo;
            return this;
        }

        public CelestialBodyType build() {
            return new CelestialBodyType(this.id, this.translationKey, this.dimension, this.accessWeight, this.parent, this.displayInfo, this.gravity, this.atmosphere);
        }
    }
}
