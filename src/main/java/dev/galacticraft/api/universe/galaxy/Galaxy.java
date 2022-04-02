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

package dev.galacticraft.api.universe.galaxy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Galaxy {
    public static final Codec<Galaxy> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(Galaxy::name),
            Codec.STRING.fieldOf("description").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(Galaxy::description),
            CelestialPosition.CODEC.fieldOf("position").forGetter(Galaxy::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(Galaxy::display)
    ).apply(instance, Galaxy::new));

    private final @NotNull TranslatableText name;
    private final @NotNull TranslatableText description;
    private final CelestialPosition<?, ?> position;
    private final CelestialDisplay<?, ?> display;

    public Galaxy(@NotNull TranslatableText name, @NotNull TranslatableText description,
                  CelestialPosition<?, ?> position, CelestialDisplay<?, ?> display) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.display = display;
    }

    public static Registry<Galaxy> getRegistry(@NotNull DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.GALAXY_KEY);
    }

    public static Galaxy getById(DynamicRegistryManager manager, Identifier id) {
        return getById(getRegistry(manager), id);
    }

    public static Identifier getId(DynamicRegistryManager manager, Galaxy galaxy) {
        return getId(getRegistry(manager), galaxy);
    }

    public static Galaxy getById(@NotNull Registry<Galaxy> registry, Identifier id) {
        return registry.get(id);
    }

    public static Identifier getId(@NotNull Registry<Galaxy> registry, Galaxy galaxy) {
        return registry.getId(galaxy);
    }

    public @NotNull TranslatableText name() {
        return this.name;
    }

    public @NotNull TranslatableText description() {
        return this.description;
    }

    public CelestialPosition<?, ?> position() {
        return this.position;
    }

    public CelestialDisplay<?, ?> display() {
        return this.display;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Galaxy) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.position, that.position) &&
                Objects.equals(this.display, that.display);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, position, display);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Galaxy[" +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "position=" + position + ", " +
                "display=" + display + ']';
    }

}
