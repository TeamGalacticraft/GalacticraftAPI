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

import com.hrznstudio.galacticraft.api.internal.dynamic.RegistryElementCodec;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class CelestialBodyType {
    public static final Codec<Supplier<CelestialBodyType>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, () -> CelestialBodyType.CODEC);
    public static final Codec<CelestialBodyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(i -> i.id),
            Codec.STRING.fieldOf("translation_key").forGetter(i -> i.translationKey),
            World.CODEC.optionalFieldOf("dimension").forGetter(i -> Optional.ofNullable(i.worldKey)),
            SolarSystemType.REGISTRY_CODEC.fieldOf("solar_system").forGetter(i -> () -> i.parentSystem),
            Codec.INT.fieldOf("access_weight").forGetter(i -> i.accessWeight),
            CelestialBodyType.REGISTRY_CODEC.optionalFieldOf("parent").forGetter(i -> i.parent == null ? Optional.empty() : Optional.of(() -> i.parent)),
            CelestialBodyDisplayInfo.CODEC.fieldOf("display").forGetter(i -> i.displayInfo),
            Codec.FLOAT.fieldOf("gravity").forGetter(i -> i.gravity),
            AtmosphericInfo.CODEC.fieldOf("atmosphere").forGetter(i -> i.atmosphere)
    ).apply(instance, (identifier, s, worldRegistryKey, solarSystem, integer, celestialBodyTypeSupplier, celestialBodyDisplayInfo, aFloat, atmosphericInfo) ->
            new CelestialBodyType(identifier, s, worldRegistryKey.orElse(null), solarSystem.get(), integer, celestialBodyTypeSupplier.orElse(() -> null).get(), celestialBodyDisplayInfo, aFloat, atmosphericInfo)));

    public static final CelestialBodyType THE_SUN = new Builder(new Identifier("galacticraft-api", "the_sun"))
            .translationKey("ui.galacticraft-api.bodies.the_sun")
            .parent(null)
            .display(
                    new CelestialBodyDisplayInfo.Builder()
                            .texture(new Identifier("galacticraft-api", "body_icons"))
                            .build()
            ).build();

    public static final CelestialBodyType EARTH = new Builder(new Identifier("galacticraft-api", "earth"))
            .translationKey("ui.galacticraft-api.bodies.earth")
            .world(World.OVERWORLD)
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
            ).build();
    private final Identifier id;
    private final String translationKey;
    private final @Nullable RegistryKey<World> worldKey;
    private final SolarSystemType parentSystem;
    private final int accessWeight;
    private final CelestialBodyType parent;
    private final CelestialBodyDisplayInfo displayInfo;
    private final float gravity;
    private final AtmosphericInfo atmosphere;

    /**
     * Used to register a new Celestial Body. {@link AddonRegistry#CELESTIAL_BODIES}
     * @param id Unique identifier
     * @param translationKey Key used to translate the body's name
     * @param worldKey The world the body type is for
     * @param parentSystem The celestial body's parent solar system
     * @param accessWeight The rocket tier/access weight for the body. (-1 for inaccessible)
     * @param parent Parent body.
     * @param displayInfo Information used to display the body
     * @param gravity The gravity applied to entities on the body (1.0f is the same as the overworld)
     * @param atmosphere The atmosphere of the body
     */
    public CelestialBodyType(Identifier id, String translationKey, @Nullable RegistryKey<World> worldKey, SolarSystemType parentSystem, int accessWeight, CelestialBodyType parent, CelestialBodyDisplayInfo displayInfo, float gravity, AtmosphericInfo atmosphere) {
        this.id = id;
        this.translationKey = translationKey;
        this.worldKey = worldKey;
        this.parentSystem = parentSystem;
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

    public static Optional<CelestialBodyType> getByDimType(RegistryKey<World> world) {
        return AddonRegistry.CELESTIAL_BODIES.stream().filter(celestialBodyType -> celestialBodyType.getWorld() == world).findFirst();
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

    public @Nullable RegistryKey<World> getWorld() {
        return worldKey;
    }

    public SolarSystemType getParentSystem() {
        return parentSystem;
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
        private RegistryKey<World> worldKey = null;
        private SolarSystemType parentSystem = SolarSystemType.SOL;
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

        public Builder world(RegistryKey<World> worldKey) {
            this.worldKey = worldKey;
            return this;
        }

        public Builder system(SolarSystemType system) {
            this.parentSystem = system;
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
            assert this.id != null;
            return new CelestialBodyType(this.id, this.translationKey, this.worldKey, this.parentSystem, this.accessWeight, this.parent, this.displayInfo, this.gravity, this.atmosphere);
        }
    }
}
