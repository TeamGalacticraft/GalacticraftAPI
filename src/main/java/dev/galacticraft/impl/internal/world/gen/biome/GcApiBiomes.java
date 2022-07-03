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

package dev.galacticraft.impl.internal.world.gen.biome;

import dev.galacticraft.impl.Constant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class GcApiBiomes {
    public static final Biome SPACE = createSpaceBiome();

    private static Biome createSpaceBiome() {
        Biome.Builder builder = new Biome.Builder();
        SpawnSettings.Builder spawns = new SpawnSettings.Builder();
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        effects.fogColor(0).waterColor(4159204).waterFogColor(329011).skyColor(0);
        return builder
                .downfall(0)
                .temperature(1)
                .effects(effects.build())
                .spawnSettings(spawns.build())
                .precipitation(Biome.Precipitation.NONE)
                .generationSettings(genSettings.build())
                .temperatureModifier(Biome.TemperatureModifier.NONE).build();
    }

    public static void register() {
        Registry.register(BuiltinRegistries.BIOME, new Identifier(Constant.MOD_ID, "space"), SPACE);
    }
}
