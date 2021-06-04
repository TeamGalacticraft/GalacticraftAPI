package dev.galacticraft.impl.universe.display.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.universe.display.CelestialDisplayConfig;
import net.minecraft.util.Identifier;

public record IconCelestialDisplayConfig(Identifier texture, int u, int v, int width, int height, int scale) implements CelestialDisplayConfig {
    public static final Codec<IconCelestialDisplayConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(IconCelestialDisplayConfig::texture),
            Codec.INT.fieldOf("u").forGetter(IconCelestialDisplayConfig::u),
            Codec.INT.fieldOf("v").forGetter(IconCelestialDisplayConfig::v),
            Codec.INT.fieldOf("width").forGetter(IconCelestialDisplayConfig::width),
            Codec.INT.fieldOf("height").forGetter(IconCelestialDisplayConfig::height),
            Codec.INT.fieldOf("scale").forGetter(IconCelestialDisplayConfig::scale)
    ).apply(instance, IconCelestialDisplayConfig::new));
}
