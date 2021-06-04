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

package dev.galacticraft.api.satellite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class SatelliteRecipe implements Predicate<Inventory> {
    public static final Codec<SatelliteRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(SatelliteRecipe::getIngredients)
    ).apply(i, SatelliteRecipe::new));
    private final List<ItemStack> ingredients;

    public SatelliteRecipe(ItemStack... stacks) {
        this(Arrays.asList(stacks));
    }

    public SatelliteRecipe(List<ItemStack> list) {
        this.ingredients = list;
    }

    public static SatelliteRecipe deserialize(PacketByteBuf buf) {
        int length = buf.readInt();
        List<ItemStack> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(buf.readItemStack());
        }
        return new SatelliteRecipe(list);
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public PacketByteBuf serialize(PacketByteBuf buf) {
        buf.writeInt(this.getIngredients().size());
        for (ItemStack stack : this.getIngredients()) {
            buf.writeItemStack(stack);
        }
        return buf;
    }

    @Override
    public boolean test(Inventory inventory) {
        for (ItemStack stack : this.ingredients) {
            if (inventory.count(stack.getItem()) < stack.getCount()) return false;
        }
        return true;
    }
}
