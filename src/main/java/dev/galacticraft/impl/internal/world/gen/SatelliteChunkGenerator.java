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

package dev.galacticraft.impl.internal.world.gen;

import com.mojang.serialization.Codec;
import dev.galacticraft.impl.internal.world.gen.biome.GcApiBiomes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

@ApiStatus.Internal
public class SatelliteChunkGenerator extends ChunkGenerator {
    public static final SatelliteChunkGenerator VOID_INSTANCE = new SatelliteChunkGenerator(GcApiBiomes.SPACE, new Structure());
    public static final Codec<SatelliteChunkGenerator> CODEC = Codec.unit(VOID_INSTANCE);
    public static final StructuresConfig STRUCTURES_CONFIG = new StructuresConfig(Optional.empty(), Collections.emptyMap());
    private static final MultiNoiseUtil.NoiseValuePoint EMPTY_POINT = new MultiNoiseUtil.NoiseValuePoint(0, 0, 0, 0, 0, 0);
    private static final MultiNoiseUtil.MultiNoiseSampler EMPTY_SAMPLER = (x, y, z) -> EMPTY_POINT;
    private static final Pool<SpawnSettings.SpawnEntry> EMPTY_POOL = Pool.empty();
    private static final VerticalBlockSample EMPTY_VIEW = new VerticalBlockSample(0, new BlockState[0]);
    private final Structure structure;
    private final Biome biome;

    public SatelliteChunkGenerator(Biome biome, Structure structure) {
        super(new FixedBiomeSource(biome), STRUCTURES_CONFIG);
        this.structure = structure;
        this.biome = biome;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return this;
    }

    @Override
    public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
        return EMPTY_SAMPLER;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver generationStep) {
    }

    @Nullable
    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius, boolean skipExistingChunks) {
        return null;
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    @Override
    public CompletableFuture<Chunk> populateBiomes(Registry<Biome> registry, Executor executor, Blender arg, StructureAccessor structureAccessor, Chunk chunk) {
        chunk.setBiomeIfAbsent(() -> this.biome);
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biome;
    }

    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
    }

    @Override
    protected boolean testBiomeByKey(Registry<Biome> registry, Predicate<RegistryKey<Biome>> condition, Biome biome) {
        return registry.getKey(this.biome).filter(condition).isPresent();
    }

    @Override
    public StructuresConfig getStructuresConfig() {
        return STRUCTURES_CONFIG;
    }

    @Override
    public int getSpawnHeight(HeightLimitView world) {
        return 255;
    }

    @Override
    public int getWorldHeight() {
        return 256;
    }

    @Override
    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        return EMPTY_POOL;
    }

    @Override
    public void setStructureStarts(DynamicRegistryManager manager, StructureAccessor accessor, Chunk chunk, StructureManager structureManager, long worldSeed) {
    }

    @Override
    public void addStructureReferences(StructureWorldAccess world, StructureAccessor accessor, Chunk chunk) {
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender arg, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinimumY() {
        return -64;
    }

    @Override
    public int getHeightOnGround(int x, int z, Type heightmap, HeightLimitView world) {
        return 0;
    }

    @Override
    public int getHeightInGround(int x, int z, Type heightmap, HeightLimitView world) {
        return 0;
    }

    @Override
    public boolean isStrongholdStartingChunk(ChunkPos pos) {
        return false;
    }

    @Override
    public int getHeight(int x, int z, Type heightmap, HeightLimitView world) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return EMPTY_VIEW;
    }
}
