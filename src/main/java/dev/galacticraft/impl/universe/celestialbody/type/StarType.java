package dev.galacticraft.impl.universe.celestialbody.type;

import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.star.Star;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import dev.galacticraft.impl.universe.celestialbody.config.StarConfig;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StarType extends CelestialBodyType<StarConfig> implements Star<StarConfig> {
    public static final StarType INSTANCE = new StarType();
    protected StarType() {
        super(StarConfig.CODEC);
    }

    @Override
    public @NotNull TranslatableText name(StarConfig config) {
        return config.name();
    }

    @Override
    public @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager, StarConfig config) {
        return null;
    }

    @Override
    public @NotNull RegistryKey<Galaxy> galaxy(StarConfig config) {
        return config.galaxy();
    }

    @Override
    public @NotNull TranslatableText description(StarConfig config) {
        return config.description();
    }

    @Override
    public @NotNull CelestialPosition<?, ?> position(StarConfig config) {
        return config.position();
    }

    @Override
    public @NotNull CelestialDisplay<?, ?> display(StarConfig config) {
        return config.display();
    }

    @Override
    public double luminance(StarConfig config) {
        return config.luminance();
    }

    @Override
    public int surfaceTemperature(StarConfig config) {
        return config.surfaceTemperature();
    }
}
