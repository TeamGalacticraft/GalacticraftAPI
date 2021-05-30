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

package dev.galacticraft.api.celestialbody;

import dev.galacticraft.api.atmosphere.AtmosphericGas;
import dev.galacticraft.api.atmosphere.AtmosphericInfo;
import dev.galacticraft.api.celestialbody.satellite.SatelliteRecipe;
import dev.galacticraft.api.internal.codec.LazyRegistryElementCodec;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.internal.util.NetworkUtil;
import dev.galacticraft.api.registry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class CelestialBodyType {
    public static final Codec<Supplier<CelestialBodyType>> REGISTRY_CODEC = LazyRegistryElementCodec.of(AddonRegistry.CELESTIAL_BODY_TYPE_KEY, new Lazy<>(() -> CelestialBodyType.CODEC));
    public static final Codec<CelestialBodyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(CelestialBodyType::getId),
            Codec.STRING.fieldOf("translation_key").forGetter(CelestialBodyType::getTranslationKey),
            CelestialObjectType.CODEC.fieldOf("type").forGetter(CelestialBodyType::getType),
            World.CODEC.optionalFieldOf("dimension").xmap(optional -> optional.orElse(null), Optional::ofNullable).forGetter(CelestialBodyType::getWorld),
            SolarSystemType.REGISTRY_CODEC.fieldOf("solar_system").xmap(Supplier::get, system -> () -> system).forGetter(CelestialBodyType::getParentSystem),
            Codec.INT.fieldOf("access_weight").forGetter(CelestialBodyType::getAccessWeight),
            CelestialBodyType.REGISTRY_CODEC.optionalFieldOf("parent").xmap(optional -> optional.orElse(() -> null).get(), parent -> parent == null ? Optional.empty() : Optional.of(() -> parent)).forGetter(CelestialBodyType::getParent),
            CelestialBodyDisplayInfo.CODEC.fieldOf("display").forGetter(CelestialBodyType::getDisplayInfo),
            Codec.FLOAT.fieldOf("gravity").forGetter(CelestialBodyType::getGravity),
            AtmosphericInfo.CODEC.fieldOf("atmosphere").forGetter(CelestialBodyType::getAtmosphere),
            SatelliteRecipe.CODEC.optionalFieldOf("satellite_recipe").xmap(optional -> optional.orElse(null), Optional::ofNullable).forGetter(CelestialBodyType::getSatelliteRecipe)
    ).apply(instance, CelestialBodyType::new));

    public static final CelestialBodyType THE_SUN = new Builder(new Identifier(GalacticraftAPI.MOD_ID, "the_sun"))
            .translationKey("ui.galacticraft-api.bodies.the_sun")
            .type(CelestialObjectType.STAR)
            .parent(null)
            .display(
                    new CelestialBodyDisplayInfo.Builder()
                            .texture(new Identifier(GalacticraftAPI.MOD_ID, "body_icons"))
                            .build()
            ).build();

    public static final CelestialBodyType EARTH = new Builder(new Identifier(GalacticraftAPI.MOD_ID, "earth"))
            .translationKey("ui.galacticraft-api.bodies.earth")
            .world(World.OVERWORLD)
            .weight(0)
            .display(
                    new CelestialBodyDisplayInfo.Builder()
                            .texture(new Identifier(GalacticraftAPI.MOD_ID, "body_icons"))
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
    private final CelestialObjectType type;
    private final @Nullable RegistryKey<World> worldKey;
    private final SolarSystemType parentSystem;
    private final int accessWeight;
    private final @Nullable CelestialBodyType parent;
    private final CelestialBodyDisplayInfo displayInfo;
    private final float gravity;
    private final AtmosphericInfo atmosphere;
    private final @Nullable SatelliteRecipe satelliteRecipe;

    /**
     * @param id Unique identifier
     * @param translationKey Key used to translate the body's name
     * @param type The type of celestial body this is
     * @param worldKey The world the body type is for
     * @param parentSystem The celestial body's parent solar system
     * @param accessWeight The rocket tier/access weight for the body. (-1 for inaccessible)
     * @param parent Parent body.
     * @param displayInfo Information used to display the body
     * @param gravity The gravity applied to entities on the body (1.0f is the same as the overworld)
     * @param atmosphere The atmosphere of the body
     * @param satelliteRecipe The resources required create a space station
     */
    protected CelestialBodyType(@NotNull Identifier id, String translationKey, @NotNull CelestialObjectType type, @Nullable RegistryKey<World> worldKey, @NotNull SolarSystemType parentSystem, int accessWeight, @Nullable CelestialBodyType parent, @NotNull CelestialBodyDisplayInfo displayInfo, float gravity, @NotNull AtmosphericInfo atmosphere, @Nullable SatelliteRecipe satelliteRecipe) {
        this.id = id;
        this.translationKey = translationKey;
        this.type = type;
        this.worldKey = worldKey;
        this.parentSystem = parentSystem;
        this.accessWeight = accessWeight;
        this.parent = parent;
        this.displayInfo = displayInfo;
        this.gravity = gravity;
        this.atmosphere = atmosphere;
        this.satelliteRecipe = satelliteRecipe;
    }

    public @NotNull Identifier getId() {
        return id;
    }

    public @NotNull String getTranslationKey() {
        return translationKey;
    }

    public @NotNull CelestialObjectType getType() {
        return type;
    }

    public @Nullable RegistryKey<World> getWorld() {
        return worldKey;
    }

    public @NotNull SolarSystemType getParentSystem() {
        return parentSystem;
    }

    public int getAccessWeight() {
        return accessWeight;
    }

    public @Nullable CelestialBodyType getParent() {
        return parent;
    }

    public @NotNull CelestialBodyDisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public float getGravity() {
        return gravity;
    }

    public @NotNull AtmosphericInfo getAtmosphere() {
        return atmosphere;
    }

    public @Nullable SatelliteRecipe getSatelliteRecipe() {
        return satelliteRecipe;
    }

    public static CelestialBodyType deserialize(DynamicRegistryManager registryManager, Dynamic<?> dynamic) {
        return registryManager.get(AddonRegistry.CELESTIAL_BODY_TYPE_KEY).get(new Identifier(dynamic.asString("")));
    }

    /**
     * @return all registered Celestial Bodies
     */
    public static Registry<CelestialBodyType> getAll(DynamicRegistryManager registryManager) {
        return registryManager.get(AddonRegistry.CELESTIAL_BODY_TYPE_KEY);
    }

    /**
     * @param id The identifier of the body
     * @return the celestial body or null
     */
    public static CelestialBodyType getById(DynamicRegistryManager registryManager, Identifier id) {
        return registryManager.get(AddonRegistry.CELESTIAL_BODY_TYPE_KEY).get(id);
    }

    public static Identifier getId(DynamicRegistryManager registryManager, CelestialBodyType type) {
        return registryManager.get(AddonRegistry.CELESTIAL_BODY_TYPE_KEY).getId(type);
    }

    public static Optional<CelestialBodyType> getByDimType(DynamicRegistryManager registryManager, RegistryKey<World> world) {
        return registryManager.get(AddonRegistry.CELESTIAL_BODY_TYPE_KEY).stream().filter(celestialBodyType -> celestialBodyType.getWorld() == world).findFirst();
    }

    public PacketByteBuf serialize(PacketByteBuf buf, DynamicRegistryManager registryManager, boolean registeredToClient) {
        buf.writeIdentifier(this.getId());
        if (!registeredToClient) {
            buf.writeByte(NetworkUtil.packBooleans(false, this.getWorld() != null, this.getParent() != null, this.getSatelliteRecipe() != null));
            buf.writeString(this.getTranslationKey());
            buf.writeByte(this.getType().ordinal());
            if (this.getWorld() != null) buf.writeIdentifier(this.getWorld().getValue());
            buf.writeIdentifier(SolarSystemType.getId(registryManager, this.getParentSystem()));
            buf.writeInt(this.getAccessWeight());
            if (this.getParent() != null) buf.writeIdentifier(CelestialBodyType.getId(registryManager, this.getParent()));
            this.getDisplayInfo().writePacket(buf);
            buf.writeFloat(this.getGravity());
            this.getAtmosphere().writePacket(buf);
            if (this.getSatelliteRecipe() != null) this.getSatelliteRecipe().serialize(buf);
        } else {
            buf.writeBoolean(true);
        }
        return buf;
    }

    public static CelestialBodyType deserialize(PacketByteBuf buf, DynamicRegistryManager registryManager) {
        Identifier id = buf.readIdentifier();
        byte packed = buf.readByte();
        if (!NetworkUtil.unpackBoolean(packed, 0)) {
            return new CelestialBodyType(id,
                    buf.readString(),
                    CelestialObjectType.values()[buf.readByte()],
                    NetworkUtil.unpackBoolean(packed, 1) ? RegistryKey.of(Registry.WORLD_KEY, buf.readIdentifier()) : null,
                    SolarSystemType.getById(registryManager, buf.readIdentifier()),
                    buf.readInt(),
                    NetworkUtil.unpackBoolean(packed, 2) ? CelestialBodyType.getById(registryManager, buf.readIdentifier()) : null,
                    CelestialBodyDisplayInfo.fromPacket(buf),
                    buf.readFloat(),
                    AtmosphericInfo.readPacket(registryManager, buf),
                    NetworkUtil.unpackBoolean(packed, 3) ? SatelliteRecipe.deserialize(buf) : null
            );
        } else {
            return CelestialBodyType.getById(registryManager, id);
        }
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    public static class Builder {
        private final Identifier id;
        private String translationKey;
        private CelestialObjectType type = CelestialObjectType.PLANET;
        private RegistryKey<World> worldKey = null;
        private SolarSystemType parentSystem = SolarSystemType.SOL;
        private int accessWeight = -1;
        private CelestialBodyType parent = THE_SUN;
        private CelestialBodyDisplayInfo displayInfo = null;
        private float gravity = 1.0f;
        private AtmosphericInfo atmosphere = new AtmosphericInfo.Builder().build();
        private SatelliteRecipe satelliteRecipe = null;

        public Builder(Identifier id) {
            this.id = id;
            this.translationKey = id.toString();
        }

        public Builder translationKey(String key) {
            this.translationKey = key;
            return this;
        }

        public Builder type(CelestialObjectType type) {
            this.type = type;
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

        public Builder parent(@Nullable CelestialBodyType parent) {
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
        public Builder recipe(SatelliteRecipe recipe) {
            this.satelliteRecipe = recipe;
            return this;
        }

        public CelestialBodyType build() {
            assert this.id != null;
            return new CelestialBodyType(this.id, this.translationKey, this.type, this.worldKey, this.parentSystem, this.accessWeight, this.parent, this.displayInfo, this.gravity, this.atmosphere, this.satelliteRecipe);
        }
    }
}
