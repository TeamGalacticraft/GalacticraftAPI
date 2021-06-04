package dev.galacticraft.impl.universe.celestialbody.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

public record StarConfig(@NotNull TranslatableText name, @NotNull TranslatableText description, @NotNull RegistryKey<Galaxy> galaxy, @NotNull CelestialPosition<?, ?> position, @NotNull CelestialDisplay<?, ?> display, double luminance, int surfaceTemperature) implements CelestialBodyConfig {
    public static final Codec<StarConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(StarConfig::name),
            Codec.STRING.fieldOf("description").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(StarConfig::description),
            Identifier.CODEC.fieldOf("galaxy").xmap(id -> RegistryKey.of(AddonRegistry.GALAXY_KEY, id), RegistryKey::getValue).forGetter(StarConfig::galaxy),
            CelestialPosition.CODEC.fieldOf("position").forGetter(StarConfig::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(StarConfig::display),
            Codec.DOUBLE.fieldOf("luminance").forGetter(StarConfig::luminance),
            Codec.INT.fieldOf("surface_temperature").forGetter(StarConfig::surfaceTemperature)
    ).apply(instance, StarConfig::new));
}
