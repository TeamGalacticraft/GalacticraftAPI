/*
 * Copyright (c) 2019-2023 Team Galacticraft
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

package dev.galacticraft.api.gametest;

import dev.galacticraft.api.registry.AddonRegistries;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenAccessor;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import org.jetbrains.annotations.NotNull;

public class GalacticraftApiTestSuite implements FabricGameTest {
    private static final String MOD_ID = "gc-api-test";

    @GameTest(template = EMPTY_STRUCTURE)
    public void testForDatapackGalaxy(@NotNull GameTestHelper context) {
        context.succeedWhen(() -> {
            final var registry = context.getLevel().registryAccess().registryOrThrow(AddonRegistries.GALAXY);
            if (!registry.containsKey(ResourceKey.create(AddonRegistries.GALAXY, new ResourceLocation(MOD_ID, "example_galaxy")))) {
                context.fail("Expected custom datapack galaxy to be loaded!");
            }
        });
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void testForDatapackCelestialBodies(@NotNull GameTestHelper context) {
        context.succeedWhen(() -> {
            final var registry = context.getLevel().registryAccess().registryOrThrow(AddonRegistries.CELESTIAL_BODY);
            if (!registry.containsKey(ResourceKey.create(AddonRegistries.CELESTIAL_BODY, new ResourceLocation(MOD_ID, "example_star")))) {
                context.fail("Expected custom datapack star to be loaded!");
            } else if (!registry.containsKey(ResourceKey.create(AddonRegistries.CELESTIAL_BODY, new ResourceLocation(MOD_ID, "example_planet")))) {
                context.fail("Expected custom datapack planet to be loaded!");
            }
        });
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void testOxygenSerialization(@NotNull GameTestHelper context) {
        context.succeedWhen(() -> {
            var absolutePos = context.absolutePos(BlockPos.ZERO);
            var chunk = context.getLevel().getChunk(absolutePos);
            int x = absolutePos.getX() & 15;
            int y = absolutePos.getY();
            int z = absolutePos.getZ() & 15;
            ((ChunkOxygenAccessor) chunk).galacticraft$setInverted(x, y, z, true);
            if (!((ChunkOxygenAccessor) chunk).galacticraft$isInverted(x, y, z)) {
                context.fail("Expected area to become unbreathable!");
            } else {
                CompoundTag serialized = ChunkSerializer.write(context.getLevel(), chunk);
                ProtoChunk deserialized = ChunkSerializer.read(context.getLevel(), context.getLevel().getPoiManager(), chunk.getPos(), serialized);
                if (!((ChunkOxygenAccessor)deserialized).galacticraft$isInverted(x, y, z)) {
                    context.fail("Expected area to stay unbreathable upon deserialization!");
                }
            }
        });
    }
}
