/*
 * Copyright (c) 2019-2023 Team Galacticraft
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

package dev.galacticraft.api.rocket.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.impl.rocket.recipe.EmptyQuantifiedIngredient;
import dev.galacticraft.impl.rocket.recipe.QuantifiedIngredientImpl;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface QuantifiedIngredient {
    Codec<QuantifiedIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SatelliteRecipe.INGREDIENT_CODEC.fieldOf("ingredient").forGetter(QuantifiedIngredient::ingredient),
            Codec.LONG.fieldOf("amount").forGetter(QuantifiedIngredient::amount)
    ).apply(instance, QuantifiedIngredient::create));

    @Contract("_, _ -> new")
    static @NotNull QuantifiedIngredient create(@NotNull Ingredient ingredient, long amount) {
        if (amount < 1 || ingredient == Ingredient.EMPTY) throw new AssertionError();
        return new QuantifiedIngredientImpl(ingredient, amount);
    }

    @Contract(pure = true)
    static @NotNull QuantifiedIngredient empty() {
        return EmptyQuantifiedIngredient.INSTANCE;
    }

    @NotNull Ingredient ingredient();

    long amount();

    boolean isEmpty();
}
