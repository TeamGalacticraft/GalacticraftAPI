package dev.galacticraft.impl.universe.celestialbody.type;

import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.satellite.Orbitable;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.impl.universe.celestialbody.config.DecorativePlanetConfig;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DecorativePlanet extends CelestialBodyType<DecorativePlanetConfig> implements Orbitable<DecorativePlanetConfig> {
    public DecorativePlanet() {
        super(DecorativePlanetConfig.CODEC);
    }

    @Override
    public @NotNull TranslatableText name(DecorativePlanetConfig config) {
        return config.name();
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager, DecorativePlanetConfig config) {
        return manager.get(AddonRegistry.CELESTIAL_BODY_KEY).get(config.parent());
    }

    @Override
    public @NotNull RegistryKey<Galaxy> galaxy(DecorativePlanetConfig config) {
        return config.galaxy();
    }

    @Override
    public @NotNull TranslatableText description(DecorativePlanetConfig config) {
        return config.description();
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(DecorativePlanetConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(DecorativePlanetConfig config) {
        return config.display();
    }

    @Override
    public @Nullable SatelliteRecipe satelliteRecipe(DecorativePlanetConfig config) {
        return config.satelliteRecipe().orElse(null);
    }
}
