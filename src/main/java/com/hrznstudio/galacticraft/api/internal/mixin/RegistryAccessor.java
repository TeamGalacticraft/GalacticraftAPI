package com.hrznstudio.galacticraft.api.internal.mixin;

import net.minecraft.util.Identifier;
import net.minecraft.util.IndexedIterable;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Supplier;

@Mixin({Registry.class})
public interface RegistryAccessor {
    @Accessor("DEFAULT_ENTRIES")
    static Map<Identifier, Supplier<?>> getDefaultEntries() {
        return null; // ignored
    }
}
