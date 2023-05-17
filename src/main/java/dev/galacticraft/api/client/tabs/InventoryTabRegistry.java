package dev.galacticraft.api.client.tabs;

import dev.galacticraft.impl.internal.client.tabs.InventoryTabRegistryImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface InventoryTabRegistry {
    InventoryTabRegistry INSTANCE = InventoryTabRegistryImpl.INSTANCE;
    void register(ItemStack icon, Runnable onClick, Predicate<Player> visiablePredicate, Class<? extends AbstractContainerMenu> clazz);

    default void register(ItemStack icon, Runnable onClick, Class<? extends AbstractContainerMenu> clazz) {
        register(icon, onClick, player -> true, clazz);
    }
}
