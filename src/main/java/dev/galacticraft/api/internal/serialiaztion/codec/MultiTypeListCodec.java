/*
 * Copyright (c) 2019-2021 HRZN LTD
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

package dev.galacticraft.api.internal.serialiaztion.codec;

import com.google.common.collect.ImmutableList;
import dev.galacticraft.api.internal.serialiaztion.CodecProvider;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;
import java.util.stream.Stream;

public final class MultiTypeListCodec<A extends CodecProvider<A>> implements Codec<List<A>> {
    private final Registry<A> registry;

    public MultiTypeListCodec(Registry<A> registry) {
        this.registry = registry;
    }

    @Override
    public <T> DataResult<T> encode(final List<A> input, final DynamicOps<T> ops, final T prefix) {
        final ListBuilder<T> builder = ops.listBuilder();

        for (final A a : input) {
            builder.add(Identifier.CODEC.encode(a.getId(), ops, prefix)).add(a.getCodec().encodeStart(ops, a));
        }

        return builder.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<List<A>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
            final ImmutableList.Builder<A> read = ImmutableList.builder();
            final Stream.Builder<T> failed = Stream.builder();
            final MutableObject<DataResult<Unit>> result = new MutableObject<>(DataResult.success(Unit.INSTANCE, Lifecycle.stable()));
            stream.accept(t -> {
                Pair<Identifier, T> id = Identifier.CODEC.decode(ops, t).get().orThrow();
                final DataResult<? extends Pair<? extends A, T>> element = this.registry.get(id.getFirst()).getCodec().decode(ops, id.getSecond());

                element.error().ifPresent(e -> failed.add(t));
                result.setValue(result.getValue().apply2stable((r, v) -> {
                    read.add(v.getFirst());
                    return r;
                }, element));
            });

            final ImmutableList<A> elements = read.build();
            final T errors = ops.createList(failed.build());

            final Pair<List<A>, T> pair = Pair.of(elements, errors);

            return result.getValue().map(unit -> pair).setPartial(pair);
        });
    }
}
