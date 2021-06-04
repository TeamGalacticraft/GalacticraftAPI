package dev.galacticraft.impl.universe.celestialbody.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.atmosphere.AtmosphericInfo;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record PlanetConfig(@NotNull TranslatableText name, @NotNull TranslatableText description, @NotNull RegistryKey<Galaxy> galaxy, @NotNull RegistryKey<CelestialBody<?, ?>> parent, @NotNull CelestialPosition<?, ?> position, @NotNull CelestialDisplay<?, ?> display, @NotNull RegistryKey<World> world, @NotNull AtmosphericInfo atmosphere, float gravity, int accessWeight, @NotNull Optional<SatelliteRecipe> satelliteRecipe) implements CelestialBodyConfig {
    public static final Codec<PlanetConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(PlanetConfig::name),
            Codec.STRING.fieldOf("description").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(PlanetConfig::description),
            Identifier.CODEC.fieldOf("galaxy").xmap(id -> RegistryKey.of(AddonRegistry.GALAXY_KEY, id), RegistryKey::getValue).forGetter(PlanetConfig::galaxy),
            Identifier.CODEC.fieldOf("parent").xmap(id -> RegistryKey.of(AddonRegistry.CELESTIAL_BODY_KEY, id), RegistryKey::getValue).forGetter(PlanetConfig::parent),
            CelestialPosition.CODEC.fieldOf("position").forGetter(PlanetConfig::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(PlanetConfig::display),
            World.CODEC.fieldOf("world").forGetter(PlanetConfig::world),
            AtmosphericInfo.CODEC.fieldOf("atmosphere").forGetter(PlanetConfig::atmosphere),
            Codec.FLOAT.fieldOf("gravity").forGetter(PlanetConfig::gravity),
            Codec.INT.fieldOf("access_weight").forGetter(PlanetConfig::accessWeight),
            SatelliteRecipe.CODEC.optionalFieldOf("satellite_recipe").forGetter(PlanetConfig::satelliteRecipe)
    ).apply(instance, PlanetConfig::new));
}
