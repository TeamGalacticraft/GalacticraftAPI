package com.hrznstudio.galacticraft.api.celestialbodies;

import com.google.common.annotations.Beta;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * basic satellite interface, subject to change
 */
@Beta
public interface Satellite {
    @Nullable RegistryKey<World> getWorld();

    @NotNull CelestialBodyType getParent();
}
