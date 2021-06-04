package dev.galacticraft.api.universe.display;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.registry.AddonRegistry;

public record CelestialDisplay<C extends CelestialDisplayConfig, T extends CelestialDisplayType<C>>(T type, C config) {
    public static final Codec<CelestialDisplay<?, ?>> CODEC = AddonRegistry.CELESTIAL_DISPLAY_TYPE.dispatch(CelestialDisplay::type, CelestialDisplayType::getCodec);
}
