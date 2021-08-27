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

package dev.galacticraft.impl.universe.position.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.SatelliteOwnershipData;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.Objects;

public final class SatelliteConfig implements CelestialBodyConfig {
    public static final Codec<SatelliteConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("parent").xmap(id -> RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, id), RegistryKey::getValue).forGetter(SatelliteConfig::parent),
            Identifier.CODEC.fieldOf("galaxy").xmap(id -> RegistryKey.of(AddonRegistry.GALAXY_KEY, id), RegistryKey::getValue).forGetter(SatelliteConfig::galaxy),
            CelestialPosition.CODEC.fieldOf("position").forGetter(SatelliteConfig::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(SatelliteConfig::display),
            SatelliteOwnershipData.CODEC.fieldOf("ownershipData").forGetter(SatelliteConfig::ownershipData),
            Identifier.CODEC.fieldOf("world").xmap(id -> RegistryKey.of(Registry.WORLD_KEY, id), RegistryKey::getValue).forGetter(SatelliteConfig::world),
            GasComposition.CODEC.fieldOf("atmosphere").forGetter(SatelliteConfig::atmosphere),
            Codec.FLOAT.fieldOf("gravity").forGetter(SatelliteConfig::gravity),
            Codec.INT.fieldOf("accessWeight").forGetter(SatelliteConfig::accessWeight),
            DimensionOptions.CODEC.fieldOf("dimension_options").forGetter(SatelliteConfig::dimensionOptions)
    ).apply(instance, SatelliteConfig::new));

    private final RegistryKey<CelestialBody<?, ?>> parent;
    private final RegistryKey<Galaxy> galaxy;
    private final CelestialPosition<?, ?> position;
    private final CelestialDisplay<?, ?> display;
    private final SatelliteOwnershipData ownershipData;
    private final RegistryKey<World> world;
    private final GasComposition atmosphere;
    private final float gravity;
    private final int accessWeight;
    private final DimensionOptions dimensionOptions;
    private Text customName = LiteralText.EMPTY;

    public SatelliteConfig(RegistryKey<CelestialBody<?, ?>> parent, RegistryKey<Galaxy> galaxy, CelestialPosition<?, ?> position, CelestialDisplay<?, ?> display, SatelliteOwnershipData ownershipData, RegistryKey<World> world, GasComposition atmosphere, float gravity, int accessWeight, DimensionOptions dimensionOptions) {
        this.parent = parent;
        this.galaxy = galaxy;
        this.position = position;
        this.display = display;
        this.ownershipData = ownershipData;
        this.world = world;
        this.atmosphere = atmosphere;
        this.gravity = gravity;
        this.accessWeight = accessWeight;
        this.dimensionOptions = dimensionOptions;
    }

    public RegistryKey<CelestialBody<?, ?>> parent() { return parent; }

    public RegistryKey<Galaxy> galaxy() { return galaxy; }

    public CelestialPosition<?, ?> position() { return position; }

    public CelestialDisplay<?, ?> display() { return display; }

    public SatelliteOwnershipData ownershipData() { return ownershipData; }

    public Text customName() { return customName; }

    public void customName(Text name) { this.customName = name; }

    public RegistryKey<World> world() { return world; }

    public GasComposition atmosphere() { return atmosphere; }

    public float gravity() { return gravity; }

    public int accessWeight() { return accessWeight; }

    public DimensionOptions dimensionOptions() {
        return this.dimensionOptions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        SatelliteConfig that = (SatelliteConfig) obj;
        return Objects.equals(this.parent, that.parent) &&
                Objects.equals(this.galaxy, that.galaxy) &&
                Objects.equals(this.position, that.position) &&
                Objects.equals(this.display, that.display) &&
                Objects.equals(this.ownershipData, that.ownershipData) &&
                Objects.equals(this.customName, that.customName) &&
                Objects.equals(this.world, that.world) &&
                Objects.equals(this.atmosphere, that.atmosphere) &&
                Float.floatToIntBits(this.gravity) == Float.floatToIntBits(that.gravity) &&
                this.accessWeight == that.accessWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, galaxy, position, display, ownershipData, customName, world, atmosphere, gravity, accessWeight);
    }

    @Override
    public String toString() {
        return "SatelliteConfig[" +
                "parent=" + parent + ", " +
                "galaxy=" + galaxy + ", " +
                "position=" + position + ", " +
                "display=" + display + ", " +
                "ownershipData=" + ownershipData + ", " +
                "customName=" + customName + ", " +
                "world=" + world + ", " +
                "atmosphere=" + atmosphere + ", " +
                "gravity=" + gravity + ", " +
                "accessWeight=" + accessWeight + ']';
    }
}
