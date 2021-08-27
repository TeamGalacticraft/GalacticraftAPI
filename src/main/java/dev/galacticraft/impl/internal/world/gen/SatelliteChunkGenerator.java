/*
 * Copyright (c) 2019-2021 Team Galacticraft
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
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ApiStatus.Internal
public class SatelliteChunkGenerator extends ChunkGenerator {
    public static final SatelliteChunkGenerator VOID_INSTANCE = new SatelliteChunkGenerator(GcApiBiomes.SPACE, new Structure());
    public static final Codec<SatelliteChunkGenerator> CODEC = Codec.unit(VOID_INSTANCE);
    private static final BlockState AIR = Blocks.AIR.getDefaultState();

    private static final BlockSource EMPTY_SOURCE = (x, y, z) -> AIR;
    private static final VerticalBlockSample EMPTY_VIEW = new VerticalBlockSample(0, new BlockState[0]);

    public static final StructuresConfig STRUCTURES_CONFIG = new StructuresConfig(Optional.empty(), Collections.emptyMap());
    private final Structure structure;

    public SatelliteChunkGenerator(Biome biome, Structure structure) {
        super(new FixedBiomeSource(biome), STRUCTURES_CONFIG);
        this.structure = structure;
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
    public void populateBiomes(Registry<Biome> registry, Chunk chunk) {
    }

    @Override
    public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
    }

    @Override
    protected AquiferSampler createAquiferSampler(Chunk chunk) {
        return super.createAquiferSampler(chunk);
    }

    @Nullable
    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius, boolean skipExistingChunks) {
        return null;
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        if (region.isChunkLoaded(0, 0) && region.getChunk(0, 0, ChunkStatus.BIOMES, false) == null) this.structure.place(region.toServerWorld(), BlockPos.ORIGIN, BlockPos.ORIGIN, new StructurePlacementData().clearProcessors().setIgnoreEntities(true).setPlaceFluids(true).setUpdateNeighbors(false), new Random(78921412L), 0);
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        ChunkPos chunkPos = region.getCenterPos();
        Biome biome = region.getBiome(chunkPos.getStartPos());
        ChunkRandom chunkRandom = new ChunkRandom();
        chunkRandom.setPopulationSeed(region.getSeed(), chunkPos.getStartX(), chunkPos.getStartZ());
        SpawnHelper.populateEntities(region, biome, chunkPos, chunkRandom);
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
        return Pool.empty();
    }

    @Override
    public void setStructureStarts(DynamicRegistryManager manager, StructureAccessor accessor, Chunk chunk, StructureManager structureManager, long worldSeed) {
    }

    @Override
    public void addStructureReferences(StructureWorldAccess world, StructureAccessor accessor, Chunk chunk) {
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinimumY() {
        return 0;
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
    public BlockSource getBlockSource() {
        return EMPTY_SOURCE;
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
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
