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

package dev.galacticraft.api.gametest;

import dev.galacticraft.api.accessor.ChunkOxygenAccessor;
import dev.galacticraft.api.registry.AddonRegistry;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.ProtoChunk;

public class GalacticraftApiTestSuite implements FabricGameTest {
    private static final String MOD_ID = "gc-api-test";

    @GameTest(structureName = EMPTY_STRUCTURE)
    public void testForDatapackGalaxy(TestContext context) {
        context.addInstantFinalTask(() -> {
            final var registry = context.getWorld().getRegistryManager().get(AddonRegistry.GALAXY_KEY);
            if (!registry.contains(RegistryKey.of(AddonRegistry.GALAXY_KEY, new Identifier(MOD_ID, "galaxy")))) {
                context.throwGameTestException("Expected custom datapack galaxy to be loaded!");
            }
        });
    }

    @GameTest(structureName = EMPTY_STRUCTURE)
    public void testForDatapackCelestialBodies(TestContext context) {
        context.addInstantFinalTask(() -> {
            final var registry = context.getWorld().getRegistryManager().get(AddonRegistry.CELESTIAL_BODY_KEY);
            if (!registry.contains(RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, new Identifier(MOD_ID, "star")))) {
                context.throwGameTestException("Expected custom datapack star to be loaded!");
            } else if (!registry.contains(RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, new Identifier(MOD_ID, "planet")))) {
                context.throwGameTestException("Expected custom datapack planet to be loaded!");
            }
        });
    }

    @GameTest(structureName = EMPTY_STRUCTURE)
    public void testOxygenSerialization(TestContext context) {
        context.addInstantFinalTask(() -> {
            var absolutePos = context.getAbsolutePos(BlockPos.ORIGIN);
            var chunk = context.getWorld().getChunk(absolutePos);
            int x = absolutePos.getX() & 15;
            int y = absolutePos.getY() & 15;
            int z = absolutePos.getZ() & 15;
            ((ChunkOxygenAccessor) chunk).setBreathable(x, y, z, false);
            if (((ChunkOxygenAccessor) chunk).isBreathable(x, y, z)) {
                context.throwGameTestException("Expected area to become unbreathable!");
            } else {
                NbtCompound serialized = ChunkSerializer.serialize(context.getWorld(), chunk);
                ProtoChunk deserialized = ChunkSerializer.deserialize(context.getWorld(), context.getWorld().getPointOfInterestStorage(), chunk.getPos(), serialized);
                if (((ChunkOxygenAccessor) deserialized).isBreathable(x, y, z)) {
                    context.throwGameTestException("Expected area to stay unbreathable upon deserialization!");
                }
            }
        });
    }
}
