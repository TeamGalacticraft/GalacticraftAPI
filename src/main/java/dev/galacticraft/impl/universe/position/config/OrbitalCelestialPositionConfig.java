package dev.galacticraft.impl.universe.position.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.universe.position.CelestialPositionConfig;

public record OrbitalCelestialPositionConfig(double orbitTime, double distance, boolean planet) implements CelestialPositionConfig {
    public static final Codec<OrbitalCelestialPositionConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("orbit_time").forGetter(OrbitalCelestialPositionConfig::orbitTime),
            Codec.DOUBLE.fieldOf("distance").forGetter(OrbitalCelestialPositionConfig::distance),
            Codec.BOOL.fieldOf("planet").orElse(true).forGetter(OrbitalCelestialPositionConfig::planet)
    ).apply(instance, OrbitalCelestialPositionConfig::new));
}
