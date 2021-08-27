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

package dev.galacticraft.api.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class DynamicallyRegisteredObject<T> {
    private final @NotNull Identifier id;
    private final @NotNull RegistryKey<Registry<T>> registryKey;
    private @Nullable T cachedValue = null;

    public DynamicallyRegisteredObject(@NotNull Identifier id, @NotNull RegistryKey<Registry<T>> registryKey) {
        this.id = id;
        this.registryKey = registryKey;
    }

    public @NotNull Identifier id() {
        return this.id;
    }

    public @NotNull RegistryKey<Registry<T>> registryKey() {
        return this.registryKey;
    }

    public @Nullable T cachedValue() {
        return this.cachedValue;
    }

    public void clearCachedValue() {
        this.cachedValue = null;
    }

    public @Nullable T get(@Nullable DynamicRegistryManager manager) {
        return this.get(manager, true);
    }

    public @Nullable T get(@Nullable Registry<T> registry) {
        return this.get(registry, true);
    }

    public @Nullable T get(@Nullable DynamicRegistryManager manager, boolean preferCached) {
        if (manager == null) return this.cachedValue();
        return this.get(manager.get(this.registryKey()), preferCached);
    }

    public @Nullable T get(@Nullable Registry<T> registry, boolean preferCached) {
        if (registry == null || (preferCached && this.cachedValue() != null)) return this.cachedValue;
        return this.cachedValue = registry.get(this.id());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        DynamicallyRegisteredObject that = (DynamicallyRegisteredObject) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.registryKey, that.registryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registryKey);
    }

    @Override
    public String toString() {
        return "DynamicallyRegisteredObject[" +
                "id=" + id + ", " +
                "registryKey=" + registryKey + ']';
    }

}
