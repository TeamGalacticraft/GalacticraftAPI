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

package dev.galacticraft.api.rocket.recipe.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.rocket.recipe.QuantifiedIngredient;
import dev.galacticraft.impl.rocket.recipe.slot.RocketRecipeSlotImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface RocketRecipeSlot {
    Codec<RocketRecipeSlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(RocketRecipeSlot::x),
            Codec.INT.fieldOf("y").forGetter(RocketRecipeSlot::y),
            SlotAlignment.CODEC.optionalFieldOf("alignment", SlotAlignment.LEFT).forGetter(RocketRecipeSlot::alignment),
            QuantifiedIngredient.CODEC.fieldOf("ingredient").forGetter(RocketRecipeSlot::ingredient)
    ).apply(instance, RocketRecipeSlot::create));
    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull RocketRecipeSlot create(/*positive*/ int x, /*positive*/ int y, @NotNull QuantifiedIngredient ingredient) {
        return create(x, y, SlotAlignment.LEFT, ingredient);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull RocketRecipeSlot create(/*positive*/ int x, /*positive*/ int y, @NotNull SlotAlignment alignment, @NotNull QuantifiedIngredient ingredient) {
        assert x >= 0;
        assert y >= 0;
        return new RocketRecipeSlotImpl(x, y, alignment, ingredient);
    }

    /*positive*/ int x();

    /*positive*/ int y();

    SlotAlignment alignment();

    QuantifiedIngredient ingredient();
}
