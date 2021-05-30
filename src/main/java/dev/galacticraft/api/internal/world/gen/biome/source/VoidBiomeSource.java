package dev.galacticraft.api.internal.world.gen.biome.source;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.source.BiomeSource;

public class VoidBiomeSource extends BiomeSource {
    public static final VoidBiomeSource INSTANCE = new VoidBiomeSource();
    private static final Codec<VoidBiomeSource> CODEC = Codec.BOOL.xmap(b -> INSTANCE, i -> true);

    private VoidBiomeSource() {
        super(ImmutableList.of(BuiltinBiomes.THE_VOID));
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return INSTANCE;
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return BuiltinBiomes.THE_VOID;
    }

    @Override
    public Biome getBiomeForNoiseGen(ChunkPos chunkPos) {
        return BuiltinBiomes.THE_VOID;
    }
}
