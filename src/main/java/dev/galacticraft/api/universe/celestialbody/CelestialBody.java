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

package dev.galacticraft.api.universe.celestialbody;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.gas.GasComposition;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CelestialBody<C extends CelestialBodyConfig, T extends CelestialBodyType<C>>(T type, C config) {
    public static final Codec<CelestialBody<?, ?>> CODEC = AddonRegistry.CELESTIAL_BODY_TYPE.getCodec().dispatch(CelestialBody::type, CelestialBodyType::codec);

    public static Registry<CelestialBody<?, ?>> getRegistry(DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.CELESTIAL_BODY_KEY);
    }

    public static CelestialBody<?, ?> getById(DynamicRegistryManager manager, Identifier id) {
        return getById(getRegistry(manager), id);
    }

    public static Identifier getId(DynamicRegistryManager manager, CelestialBody<?, ?> celestialBody) {
        return getId(getRegistry(manager), celestialBody);
    }

    public static <C extends CelestialBodyConfig, T extends CelestialBodyType<C> & Landable<C>> Optional<CelestialBody<C, T>> getByDimension(DynamicRegistryManager manager, RegistryKey<World> key) {
        return getByDimension(getRegistry(manager), key);
    }

    public static <C extends CelestialBodyConfig, T extends CelestialBodyType<C> & Landable<C>> Optional<CelestialBody<C, T>> getByDimension(World world) {
        return getByDimension(world.getRegistryManager(), world.getRegistryKey());
    }

    public static CelestialBody<?, ?> getById(Registry<CelestialBody<?, ?>> registry, Identifier id) {
        return registry.get(id);
    }

    public static Identifier getId(Registry<CelestialBody<?, ?>> registry, CelestialBody<?, ?> celestialBody) {
        return registry.getId(celestialBody);
    }

    public static <C extends CelestialBodyConfig, T extends CelestialBodyType<C> & Landable<C>> Optional<CelestialBody<C, T>> getByDimension(Registry<CelestialBody<?, ?>> registry, RegistryKey<World> key) {
        for (CelestialBody<?, ?> body : registry) {
            if (body.type() instanceof Landable landable && landable.world(body.config()).equals(key))
                return Optional.of((CelestialBody<C, T>) body);
        }
        return Optional.empty();
    }

    /**
     * Returns the name of this celestial body
     * Be sure to {@link Text#copy() copy} the returned text if you intend on stylizing it.
     *
     * @return the name of this celestial body
     */
    public @NotNull Text name() {
        return this.type().name(this.config());
    }

    /**
     * Returns the description of this celestial body
     * Be sure to {@link Text#copy() copy} the returned text if you intend on stylizing it.
     *
     * @return the description of this celestial body
     */
    public @NotNull Text description() {
        return this.type().description(this.config());
    }

    /**
     * Returns this celestial body's parent, or {@code null} if it does not have one
     *
     * @param registry the registry to query for the parent
     * @return this celestial body's parent
     */
    public @Nullable CelestialBody<?, ?> parent(Registry<CelestialBody<?, ?>> registry) {
        return this.type().parent(registry, this.config());
    }

    /**
     * Returns this celestial body's parent, or {@code null} if it does not have one
     *
     * @param manager the dynamic registry manager to supply the registry
     * @return this celestial body's parent
     * @see #parent(Registry)
     */
    public @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager) {
        return this.type().parent(manager, this.config());
    }

    /**
     * Returns this celestial body's parent galaxy's id
     *
     * @return this celestial body's parent galaxy's id
     */
    public @NotNull RegistryKey<Galaxy> galaxy() {
        return this.type().galaxy(this.config());
    }

    /**
     * Returns this celestial body's position provider
     *
     * @return this celestial body's position provider
     * @see CelestialPosition
     */
    public @NotNull CelestialPosition<?, ?> position() {
        return this.type().position(this.config());
    }

    /**
     * Returns this celestial body's display provider
     *
     * @return this celestial body's display provider
     * @see CelestialDisplay
     */
    public @NotNull CelestialDisplay<?, ?> display() {
        return this.type().display(this.config());
    }

    /**
     * Returns this celestial body's atmospheric composition
     *
     * @return this celestial body's atmospheric composition
     */
    public @NotNull GasComposition atmosphere() {
        return this.type().atmosphere(this.config());
    }

    /**
     * Returns this celestial body's gravity
     *
     * @return this celestial body's gravity
     */
    public float gravity() {
        return this.type().gravity(this.config());
    }

}
