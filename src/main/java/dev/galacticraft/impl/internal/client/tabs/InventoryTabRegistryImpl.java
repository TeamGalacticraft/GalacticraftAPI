package dev.galacticraft.impl.internal.client.tabs;

import dev.galacticraft.api.client.tabs.InventoryTabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class InventoryTabRegistryImpl implements InventoryTabRegistry {
    public static final InventoryTabRegistryImpl INSTANCE = new InventoryTabRegistryImpl();
    public final List<TabData> TABS = new ArrayList<>();

    static {
        INSTANCE.register(Items.CRAFTING_TABLE.getDefaultInstance(), () -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
        }, player -> true, InventoryMenu.class);
    }

    @Override
    public void register(ItemStack icon, Runnable onClick, Predicate<Player> visiablePredicate, Class<? extends AbstractContainerMenu> clazz) {
        TABS.add(new TabData(icon, onClick, visiablePredicate, clazz));
    }

    public record TabData(ItemStack icon, Runnable onClick, Predicate<Player> visiablePredicate, Class<? extends AbstractContainerMenu> clazz) { }
}
