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

package dev.galacticraft.impl.internal.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

public record MappedInventory(Inventory inventory, int... slots) implements Inventory {
    // apparently mixin does not like IASTORE opcodes, so this is the alternative.
    public static MappedInventory create(Inventory inventory, int i1) {
        return new MappedInventory(inventory, i1);
    }

    public static MappedInventory create(Inventory inventory, int i1, int i2) {
        return new MappedInventory(inventory, i1, i2);
    }

    public static MappedInventory create(Inventory inventory, int i1, int i2, int i3) {
        return new MappedInventory(inventory, i1, i2, i3);
    }

    public static MappedInventory create(Inventory inventory, int i1, int i2, int i3, int i4) {
        return new MappedInventory(inventory, i1, i2, i3, i4);
    }

    public static MappedInventory create(Inventory inventory, int i1, int i2, int i3, int i4, int i5) {
        return new MappedInventory(inventory, i1, i2, i3, i4, i5);
    }

    public static MappedInventory create(Inventory inventory, int i1, int i2, int i3, int i4, int i5, int i6) {
        return new MappedInventory(inventory, i1, i2, i3, i4, i5, i6);
    }

    @Override
    public int size() {
        return this.slots.length;
    }

    @Override
    public boolean isEmpty() {
        for (int slot : slots) {
            if (!inventory.getStack(slot).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.getStack(this.slots[slot]);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.inventory.removeStack(this.slots[slot], amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.inventory.removeStack(this.slots[slot]);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.setStack(this.slots[slot], stack);
    }

    @Override
    public int getMaxCountPerStack() {
        return this.inventory.getMaxCountPerStack();
    }

    @Override
    public void markDirty() {
        this.inventory.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        this.inventory.onOpen(player);
    }

    @Override
    public void onClose(PlayerEntity player) {
        this.inventory.onClose(player);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return this.inventory.isValid(this.slots[slot], stack);
    }

    @Override
    public int count(Item item) {
        int count = 0;
        for (int slot : this.slots) {
            ItemStack stack = this.inventory.getStack(slot);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    @Override
    public boolean containsAny(Set<Item> items) {
        for (int slot : this.slots) {
            ItemStack stack = this.inventory.getStack(slot);
            if (items.contains(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int slot : this.slots) {
            this.inventory.setStack(slot, ItemStack.EMPTY);
        }
    }
}
