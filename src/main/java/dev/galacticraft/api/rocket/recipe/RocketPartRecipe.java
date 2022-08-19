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

package dev.galacticraft.api.rocket.recipe;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.rocket.recipe.slot.RocketRecipeSlot;
import dev.galacticraft.api.rocket.recipe.slot.SlotAlignment;
import dev.galacticraft.impl.rocket.recipe.EmptyRocketPartRecipeImpl;
import dev.galacticraft.impl.rocket.recipe.RocketPartRecipeImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface RocketPartRecipe {
    Codec<RocketPartRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RocketRecipeSlot.CODEC.listOf().fieldOf("slots").forGetter(s -> new ArrayList<>(s.slots())),
            Codec.BOOL.optionalFieldOf("mirrored", false).forGetter(a -> false)
    ).apply(instance, (slots, mirrored) -> mirrored ? RocketPartRecipe.mirrored(slots) : RocketPartRecipe.create(slots)));

    Codec<RocketPartRecipe> SINGLE_ALIGNMENT_CODEC = RocketRecipeSlot.CODEC.listOf().xmap(slots -> {
        for (RocketRecipeSlot rocketRecipeSlot : slots) {
            if (rocketRecipeSlot.alignment() != SlotAlignment.LEFT) throw new RuntimeException("Slot rocket part recipe has invalid alignment!");
        }
        return RocketPartRecipe.create(slots);
    }, r -> new ArrayList<>(r.slots()));

    @Contract(pure = true)
    static @NotNull RocketPartRecipe create(@NotNull Collection<RocketRecipeSlot> slots) {
        if (slots.size() == 0) return empty();
        int maxX = 0;
        int maxY = 0;
        for (RocketRecipeSlot slot : slots) {
            maxX = Math.max(maxX, slot.x() + 18);
            maxY = Math.max(maxY, slot.y() + 18);
        }
        return new RocketPartRecipeImpl(ImmutableSet.copyOf(slots), maxX, maxY);
    }

    @Contract(pure = true)
    static @NotNull RocketPartRecipe mirrored(@NotNull Collection<RocketRecipeSlot> slots) {
        if (slots.size() == 0) return empty();
        int maxX = 0;
        int maxY = 0;
        for (RocketRecipeSlot slot : slots) {
            maxX = Math.max(maxX, slot.x() + 18);
            maxY = Math.max(maxY, slot.y() + 18);
        }
        ImmutableSet.Builder<RocketRecipeSlot> builder = ImmutableSet.builderWithExpectedSize(slots.size() * 2);
        builder.addAll(slots);
        for (RocketRecipeSlot slot : slots) {
            builder.add(RocketRecipeSlot.create(slot.x(), slot.y(), slot.alignment().inverse(), slot.ingredient()));
        }
        return new RocketPartRecipeImpl(builder.build(), maxX, maxY);
    }

    @Contract(pure = true)
    static @NotNull RocketPartRecipe empty() {
        return EmptyRocketPartRecipeImpl.INSTANCE;
    }

    int width();

    int height();

    @NotNull @Unmodifiable Set<RocketRecipeSlot> slots();
}
