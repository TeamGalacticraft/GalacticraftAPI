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

package com.hrznstudio.galacticraft.api.internal.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.util.Lazy;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public final class LazyRegistryElementCodec<E> implements Codec<Supplier<E>> {
    private final RegistryKey<? extends Registry<E>> registryRef;
    private final Lazy<Codec<E>> codec;
    private final boolean allowInlineDefinitions;

    public static <E> LazyRegistryElementCodec<E> of(RegistryKey<? extends Registry<E>> registryRef, Lazy<Codec<E>> codec) {
        return new LazyRegistryElementCodec<>(registryRef, codec, true);
    }

    private LazyRegistryElementCodec(RegistryKey<? extends Registry<E>> registryRef, Lazy<Codec<E>> codec, boolean bl) {
        this.registryRef = registryRef;
        this.codec = codec;
        this.allowInlineDefinitions = bl;
    }

    public <T> DataResult<T> encode(Supplier<E> supplier, DynamicOps<T> dynamicOps, T object) {
        if (supplier.get() == null) {
            return dynamicOps.mergeToPrimitive(object, dynamicOps.createString("null"));
        }
        return dynamicOps instanceof RegistryReadingOps ? ((RegistryReadingOps<T>)dynamicOps).encodeOrId(supplier.get(), object, this.registryRef, this.codec.get()) : this.codec.get().encode(supplier.get(), dynamicOps, object);
    }

    public <T> DataResult<Pair<Supplier<E>, T>> decode(DynamicOps<T> ops, T input) {
        Optional<String> optional = ops.getStringValue(input).get().left();
        if (optional.isPresent()) {
            if (optional.get().equals("null")) {
                return DataResult.success(new Pair<>(() -> null, input));
            }
        }
        return ops instanceof RegistryOps ? ((RegistryOps<T>)ops).decodeOrId(input, this.registryRef, this.codec.get(), this.allowInlineDefinitions) : this.codec.get().decode(ops, input).map((pair) -> pair.mapFirst((object) -> () -> object));
    }

    public String toString() {
        return "RegistryElementCodec[" + this.registryRef + " " + this.codec.get().getClass() + "]";
    }
}
