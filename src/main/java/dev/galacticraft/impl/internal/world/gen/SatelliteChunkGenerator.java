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

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.*;
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
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

@ApiStatus.Internal
public class SatelliteChunkGenerator extends ChunkGenerator {
    public static final Codec<Structure> STRUCTURE_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<Structure, T>> decode(DynamicOps<T> ops, T input) {
            Structure structure = new Structure();
            NbtElement nbtElement = ops.convertTo(NbtOps.INSTANCE, input);
            if (nbtElement instanceof NbtCompound compound) {
                structure.readNbt(compound);
                return DataResult.success(new Pair<>(structure, input));
            } else {
                return DataResult.error("Not a compound");
            }
        }

        @Override
        public <T> DataResult<T> encode(Structure input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(NbtOps.INSTANCE.convertTo(ops, input.writeNbt(new NbtCompound())));
        }
    };
    public static final Codec<SatelliteChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> method_41042(instance).and(instance.group(
            Biome.REGISTRY_CODEC.fieldOf("biome").forGetter(SatelliteChunkGenerator::getBiome),
            STRUCTURE_CODEC.fieldOf("structure").forGetter(SatelliteChunkGenerator::getStructure)
    )).apply(instance, SatelliteChunkGenerator::new));

    private static final MultiNoiseUtil.MultiNoiseSampler EMPTY_SAMPLER = new MultiNoiseUtil.MultiNoiseSampler(DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(),DensityFunctionTypes.zero(), DensityFunctionTypes.zero(),DensityFunctionTypes.zero(), Collections.emptyList());
    private static final VerticalBlockSample EMPTY_VIEW = new VerticalBlockSample(0, new BlockState[0]);
    private final Structure structure;
    private final RegistryEntry<Biome> biome;

    public SatelliteChunkGenerator(Registry<StructureSet> registry, RegistryEntry<Biome> biome, Structure structure) {
        super(registry, Optional.empty(), new FixedBiomeSource(biome));
        this.structure = structure;
        this.biome = biome;
    }

    public Structure getStructure() {
        return structure;
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
    public RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biome;
    }

    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        if (chunk.getPos().x == 0 && chunk.getPos().z == 0) {
            this.structure.place(world, new BlockPos(0, 60, 0), new BlockPos(0, 60, 0), new StructurePlacementData().setIgnoreEntities(true).setPlaceFluids(true).setRandom(world.getRandom()), world.getRandom(), 0);
        }
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

    @Nullable
    @Override
    public Pair<BlockPos, RegistryEntry<ConfiguredStructureFeature<?, ?>>> locateStructure(ServerWorld serverWorld, RegistryEntryList<ConfiguredStructureFeature<?, ?>> registryEntryList, BlockPos center, int radius, boolean skipExistingChunks) {
        return null;
    }

    @Override
    public Stream<RegistryEntry<StructureSet>> method_41039() {
        return Stream.empty();
    }

    @Override
    public Optional<RegistryKey<Codec<? extends ChunkGenerator>>> getCodecKey() {
        return super.getCodecKey();
    }

    @Override
    public boolean method_41053(RegistryKey<StructureSet> registryKey, long l, int i, int j, int k) {
        return false;
    }

    @Override
    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(RegistryEntry<Biome> biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        return Pool.empty();
    }

    @Override
    protected RegistryEntry<Biome> filterBiome(RegistryEntry<Biome> biome) {
        return this.biome;
    }

    public RegistryEntry<Biome> getBiome() {
        return biome;
    }

    @Override
    public void method_41058() {
//        super.method_41058();
    }

    @Nullable
    @Override
    public List<ChunkPos> getConcentricRingsStartChunks(ConcentricRingsStructurePlacement structurePlacement) {
        return null;
    }

    @Override
    public void getDebugHudText(List<String> text, BlockPos pos) {
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
