package com.hrznstudio.galacticraft.api.internal.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

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
        return dynamicOps instanceof RegistryReadingOps ? ((RegistryReadingOps<T>)dynamicOps).encodeOrId(supplier.get(), object, this.registryRef, this.codec.get()) : this.codec.get().encode(supplier.get(), dynamicOps, object);
    }

    public <T> DataResult<Pair<Supplier<E>, T>> decode(DynamicOps<T> ops, T input) {
        return ops instanceof RegistryOps ? ((RegistryOps<T>)ops).decodeOrId(input, this.registryRef, this.codec.get(), this.allowInlineDefinitions) : this.codec.get().decode(ops, input).map((pair) -> pair.mapFirst((object) -> () -> object));
    }

    public String toString() {
        return "RegistryElementCodec[" + this.registryRef + " " + this.codec.get() + "]";
    }
}
