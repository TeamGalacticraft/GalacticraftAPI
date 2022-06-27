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

package dev.galacticraft.impl.satellite;

import dev.galacticraft.api.satellite.SpaceStationRecipe;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public class SpaceStationRecipeImpl implements SpaceStationRecipe {
    private final Object2IntMap<Ingredient> ingredients;

    public SpaceStationRecipeImpl(Object2IntMap<Ingredient> list) {
        this.ingredients = list;
    }

    @Override
    public Object2IntMap<Ingredient> ingredients() {
        return ingredients;
    }

    @Override
    public boolean test(@NotNull Inventory inventory) {
        IntList slotModifiers = new IntArrayList(inventory.size());

        for (Object2IntMap.Entry<Ingredient> ingredient : this.ingredients.object2IntEntrySet()) {
            int amount = ingredient.getIntValue();
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if (ingredient.getKey().test(stack)) {
                    amount -= (stack.getCount() - slotModifiers.getInt(i));
                    slotModifiers.set(i, slotModifiers.getInt(i) + (stack.getCount() - slotModifiers.getInt(i)) - Math.min(amount, 0));
                    if (amount <= 0) break;
                }
            }
            if (amount > 0) return false;
        }
        return true;
    }
}