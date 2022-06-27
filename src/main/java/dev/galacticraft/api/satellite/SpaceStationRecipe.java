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

package dev.galacticraft.api.satellite;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.impl.satellite.SpaceStationRecipeImpl;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface SpaceStationRecipe extends Predicate<Inventory> {
    Codec<Ingredient> INGREDIENT_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(Ingredient input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(JsonOps.INSTANCE.convertTo(ops, input.toJson()));
        }

        @Override
        public <T> DataResult<Pair<Ingredient, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(new Pair<>(Ingredient.fromJson(ops.convertTo(JsonOps.INSTANCE, input)), input));
        }
    };

    Codec<SpaceStationRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
            new Codec<Object2IntMap<Ingredient>>() {
                @Override
                public <T> DataResult<T> encode(Object2IntMap<Ingredient> input, DynamicOps<T> ops, T prefix) {
                    RecordBuilder<T> mapBuilder = ops.mapBuilder();
                    input.forEach((ingredient, amount) -> mapBuilder.add(INGREDIENT_CODEC.encode(ingredient, ops, null).get().orThrow(), ops.createInt(amount)));
                    return mapBuilder.build(prefix);
                }

                @Override
                public <T> DataResult<Pair<Object2IntMap<Ingredient>, T>> decode(DynamicOps<T> ops, T input) {
                    MapLike<T> mapLike = ops.getMap(input).get().orThrow();
                    Object2IntMap<Ingredient> list = new Object2IntArrayMap<>();
                    mapLike.entries().forEachOrdered(ttPair -> list.put(INGREDIENT_CODEC.decode(ops, ttPair.getFirst()).get().orThrow().getFirst(), ops.getNumberValue(ttPair.getSecond()).get().orThrow().intValue()));
                    return DataResult.success(new Pair<>(list, input));
                }
            }.fieldOf("ingredients").forGetter(SpaceStationRecipe::ingredients)
    ).apply(i, SpaceStationRecipe::create));

    @Contract(value = "_ -> new", pure = true)
    static @NotNull SpaceStationRecipe create(Object2IntMap<Ingredient> list) {
        return new SpaceStationRecipeImpl(list);
    }

    Object2IntMap<Ingredient> ingredients();

    @Override
    boolean test(@NotNull Inventory inventory);
}