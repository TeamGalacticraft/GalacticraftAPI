package dev.galacticraft.impl.universe.celestialbody.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record DecorativePlanetConfig(@NotNull TranslatableText name, @NotNull TranslatableText description, @NotNull RegistryKey<Galaxy> galaxy, @NotNull RegistryKey<CelestialBody<?, ?>> parent, @NotNull CelestialPosition<?, ?> position, @NotNull CelestialDisplay<?, ?> display, @NotNull Optional<SatelliteRecipe> satelliteRecipe) implements CelestialBodyConfig {
    public static final Codec<DecorativePlanetConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(DecorativePlanetConfig::name),
            Codec.STRING.fieldOf("description").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(DecorativePlanetConfig::description),
            Identifier.CODEC.fieldOf("galaxy").xmap(id -> RegistryKey.of(AddonRegistry.GALAXY_KEY, id), RegistryKey::getValue).forGetter(DecorativePlanetConfig::galaxy),
            Identifier.CODEC.fieldOf("parent").xmap(id -> RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, id), RegistryKey::getValue).forGetter(DecorativePlanetConfig::parent),
            CelestialPosition.CODEC.fieldOf("position").forGetter(DecorativePlanetConfig::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(DecorativePlanetConfig::display),
            SatelliteRecipe.CODEC.optionalFieldOf("satellite_recipe").forGetter(DecorativePlanetConfig::satelliteRecipe)
    ).apply(instance, DecorativePlanetConfig::new));
}
