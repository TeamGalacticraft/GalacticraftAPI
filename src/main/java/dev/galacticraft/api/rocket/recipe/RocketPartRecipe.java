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

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.impl.rocket.recipe.EmptyRocketPartRecipeImpl;
import dev.galacticraft.impl.rocket.recipe.RocketPartRecipeImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface RocketPartRecipe {
    Codec<RocketPartRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            QuantifiedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(RocketPartRecipe::ingredients)
    ).apply(instance, RocketPartRecipe::create));

    @Contract(pure = true)
    static @NotNull RocketPartRecipe create(@NotNull Collection<QuantifiedIngredient> ingredients) {
        if (ingredients.size() == 0) return empty();
        return new RocketPartRecipeImpl(ImmutableList.copyOf(ingredients));
    }

    @Contract(pure = true)
    static @NotNull RocketPartRecipe empty() {
        return EmptyRocketPartRecipeImpl.INSTANCE;
    }

    @NotNull List<QuantifiedIngredient> ingredients();
}
