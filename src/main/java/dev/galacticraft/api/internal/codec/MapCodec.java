package dev.galacticraft.api.internal.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.Map;
import java.util.function.Supplier;

public class MapCodec<A, B, M extends Map<A, B>> implements Codec<M> {
    private final Supplier<M> supplier;
    private final Encoder<A> encoderA;
    private final Decoder<A> decoderA;
    private final Encoder<B> encoderB;
    private final Decoder<B> decoderB;

    private MapCodec(Supplier<M> supplier, Encoder<A> encoderA, Decoder<A> decoderA, Encoder<B> encoderB, Decoder<B> decoderB) {
        this.supplier = supplier;
        this.encoderA = encoderA;
        this.decoderA = decoderA;
        this.encoderB = encoderB;
        this.decoderB = decoderB;
    }

    public static <A, B, M extends Map<A, B>> MapCodec<A, B, M> create(Supplier<M> supplier, Encoder<A> encoderA, Decoder<A> decoderA, Encoder<B> encoderB, Decoder<B> decoderB) {
        return new MapCodec<>(supplier, encoderA, decoderA, encoderB, decoderB);
    }
    public static <A, B, M extends Map<A, B>> MapCodec<A, B, M> create(Supplier<M> supplier, Codec<A> codecA, Codec<B> codecB) {
        return new MapCodec<>(supplier, codecA, codecA, codecB, codecB);
    }

    @Override
    public <T> DataResult<Pair<M, T>> decode(DynamicOps<T> ops, T input) {
        M map = supplier.get();
        ops.getMap(input).get().orThrow().entries().forEach(pair -> map.put(this.decoderA.decode(ops, pair.getFirst()).get().orThrow().getFirst(), this.decoderB.decode(ops, pair.getSecond()).get().orThrow().getFirst()));
        return DataResult.success(new Pair<>(map, input));
    }

    @Override
    public <T> DataResult<T> encode(M input, DynamicOps<T> ops, T prefix) {
        RecordBuilder<T> recordBuilder = ops.mapBuilder();
        input.forEach((a, b) -> recordBuilder.add(encoderA.encode(a, ops, prefix), encoderB.encode(b, ops, prefix)));
        return recordBuilder.build(prefix);
    }
}
