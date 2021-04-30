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

package dev.galacticraft.api.internal.fabric;

import dev.galacticraft.api.internal.accessor.ServerResearchAccessor;
import dev.galacticraft.api.internal.command.GCApiCommands;
import dev.galacticraft.api.internal.log.GCAPILogPrepender;
import dev.galacticraft.api.internal.world.gen.FlatChunkGenerator;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GalacticraftAPI implements ModInitializer {
    public static final String MOD_ID = "galacticraft-api";
    public static final Logger LOGGER = LogManager.getLogger("GalacticraftAPI", GCAPILogPrepender.INSTANCE);

    @Deprecated
    //todo look into why accessing this constant rather than getting it from a registry breaks everything - its not the same object somehow?!?
    public static final Biome SPACE = new Biome.Builder().generationSettings(new GenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.NOPE).build()).precipitation(Biome.Precipitation.NONE).category(Biome.Category.NONE).depth(0).downfall(0).spawnSettings(SpawnSettings.INSTANCE).effects(new BiomeEffects.Builder().fogColor(0).waterFogColor(0).waterColor(0).skyColor(0).build()).temperature(0).scale(0).build();

    @Override
    public void onInitialize() {
        long startInitTime = System.currentTimeMillis();
        LOGGER.info("Initializing...");
        GCApiCommands.register();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (((ServerResearchAccessor)player).changed_gcr()) {
                    ServerPlayNetworking.send(player, new Identifier(GalacticraftAPI.MOD_ID, "research_update"), ((ServerResearchAccessor) player).writeResearchChanges_gcr(new PacketByteBuf(Unpooled.buffer())));
                }
            }
        });
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(MOD_ID, "empty"), FlatChunkGenerator.CODEC);
        BuiltinBiomes.register(284, RegistryKey.of(Registry.BIOME_KEY, new Identifier(MOD_ID, "space")), SPACE);
        LOGGER.info("Initialization Complete. (Took {}ms).", System.currentTimeMillis() - startInitTime);
    }
}
