package com.hrznstudio.galacticraft.api.internal.dynamic;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.function.Supplier;

import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public final class RegistryElementCodec<E> implements Codec<Supplier<E>> {
    private final RegistryKey<? extends Registry<E>> registryRef;
    private final Supplier<Codec<E>> elementCodec;
    private final boolean allowInlineDefinitions;

    public static <E> RegistryElementCodec<E> of(RegistryKey<? extends Registry<E>> registryRef, Supplier<Codec<E>> codec) {
        return new RegistryElementCodec<>(registryRef, codec, true);
    }

    private RegistryElementCodec(RegistryKey<? extends Registry<E>> registryRef, Supplier<Codec<E>> codec, boolean bl) {
        this.registryRef = registryRef;
        this.elementCodec = codec;
        this.allowInlineDefinitions = bl;
    }

    public <T> DataResult<T> encode(Supplier<E> supplier, DynamicOps<T> dynamicOps, T object) {
        return dynamicOps instanceof RegistryReadingOps ? ((RegistryReadingOps<T>)dynamicOps).encodeOrId(supplier.get(), object, this.registryRef, this.elementCodec.get()) : this.elementCodec.get().encode(supplier.get(), dynamicOps, object);
    }

    public <T> DataResult<Pair<Supplier<E>, T>> decode(DynamicOps<T> ops, T input) {
        return ops instanceof RegistryOps ? ((RegistryOps<T>)ops).decodeOrId(input, this.registryRef, this.elementCodec.get(), this.allowInlineDefinitions) : this.elementCodec.get().decode(ops, input).map((pair) -> pair.mapFirst((object) -> () -> object));
    }

    public String toString() {
        return "RegistryElementCodec[" + this.registryRef + " " + this.elementCodec.get() + "]";
    }
}
