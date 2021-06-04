package dev.galacticraft.impl.universe.position.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.universe.position.CelestialPositionConfig;

public record StaticCelestialPositionConfig(double x, double y) implements CelestialPositionConfig {
    public static final Codec<StaticCelestialPositionConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(StaticCelestialPositionConfig::x),
            Codec.DOUBLE.fieldOf("y").forGetter(StaticCelestialPositionConfig::y)
    ).apply(instance, StaticCelestialPositionConfig::new));
}
