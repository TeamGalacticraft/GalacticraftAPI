package dev.galacticraft.impl.universe.display.config;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.universe.display.CelestialDisplayConfig;

public class EmptyCelestialDisplayConfig implements CelestialDisplayConfig {
    public static final EmptyCelestialDisplayConfig INSTANCE = new EmptyCelestialDisplayConfig();
    public static final Codec<EmptyCelestialDisplayConfig> CODEC = Codec.unit(EmptyCelestialDisplayConfig.INSTANCE);

    private EmptyCelestialDisplayConfig() {}
}
