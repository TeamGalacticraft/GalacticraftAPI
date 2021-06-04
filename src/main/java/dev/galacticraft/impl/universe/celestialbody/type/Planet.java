package dev.galacticraft.impl.universe.celestialbody.type;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.atmosphere.AtmosphericInfo;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.satellite.SatelliteRecipe;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import dev.galacticraft.api.universe.celestialbody.satellite.Orbitable;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.impl.universe.celestialbody.config.PlanetConfig;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Planet extends CelestialBodyType<PlanetConfig> implements Landable<PlanetConfig>, Orbitable<PlanetConfig> {
    public static final Planet INSTANCE = new Planet();
    protected Planet() {
        super(PlanetConfig.CODEC);
    }

    @Override
    public @NotNull TranslatableText name(PlanetConfig config) {
        return config.name();
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager, PlanetConfig config) {
        return manager.get(AddonRegistry.CELESTIAL_BODY_KEY).get(config.parent());
    }

    @Override
    public @NotNull RegistryKey<Galaxy> galaxy(PlanetConfig config) {
        return config.galaxy();
    }

    @Override
    public @NotNull TranslatableText description(PlanetConfig config) {
        return config.description();
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(PlanetConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(PlanetConfig config) {
        return config.display();
    }

    @Override
    public @NotNull RegistryKey<World> world(PlanetConfig config) {
        return config.world();
    }

    @Override
    public @NotNull AtmosphericInfo atmosphere(PlanetConfig config) {
        return config.atmosphere();
    }

    @Override
    public float gravity(PlanetConfig config) {
        return config.gravity();
    }

    @Override
    public int accessWeight(PlanetConfig config) {
        return config.accessWeight();
    }

    @Override
    public @Nullable SatelliteRecipe satelliteRecipe(PlanetConfig config) {
        return config.satelliteRecipe().orElse(null);
    }
}
