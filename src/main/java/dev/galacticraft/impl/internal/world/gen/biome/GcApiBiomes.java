package dev.galacticraft.impl.internal.world.gen.biome;

import dev.galacticraft.impl.Constant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class GcApiBiomes {
    public static final Biome SPACE = createSpaceBiome();

    private static Biome createSpaceBiome() {
        Biome.Builder builder = new Biome.Builder().category(Biome.Category.NONE);
        SpawnSettings.Builder spawns = new SpawnSettings.Builder();
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        return builder
                .depth(-5)
                .scale(-10)
                .downfall(0)
                .temperature(0.1f)
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
