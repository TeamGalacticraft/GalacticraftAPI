package com.hrznstudio.galacticraft.api.addon;

import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.internal.mixin.RegistryAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.function.Supplier;

public abstract class AddonRegistry<T> extends Registry<T> {
    public static final Registry<CelestialBodyType> CELESTIAL_BODIES = create(new Identifier("galacticraft-api", "celestial_bodies"), () -> {
        return CelestialBodyType.THE_SUN;
    });

    private static <T> Registry<T> create(Identifier identifier, Supplier<T> defaultObject) {
        return putDefaultEntry(identifier, new SimpleRegistry<>(), defaultObject);
    }

    private static <T, R extends MutableRegistry<T>> R putDefaultEntry(Identifier identifier, R mutableRegistry_1, Supplier<T> supplier_1) {
        RegistryAccessor.getDefaultEntries().put(identifier, supplier_1);
        return (R) REGISTRIES.add(identifier, mutableRegistry_1);
    }
}
