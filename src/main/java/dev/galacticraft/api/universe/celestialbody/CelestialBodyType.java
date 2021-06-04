package dev.galacticraft.api.universe.celestialbody;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CelestialBodyType<C extends CelestialBodyConfig> {
    private final Codec<CelestialBody<C, CelestialBodyType<C>>> codec;

    public CelestialBodyType(Codec<C> codec) {
        this.codec = codec.fieldOf("config").xmap((config) -> new CelestialBody<>(this, config), CelestialBody::config).codec();
    }

    public abstract @NotNull TranslatableText name(C config);

    public abstract @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager, C config);

    public abstract @NotNull RegistryKey<Galaxy> galaxy(C config);

    public abstract @NotNull TranslatableText description(C config);

    public abstract @NotNull CelestialPosition<?, ?> position(C config);

    public abstract @NotNull CelestialDisplay<?, ?> display(C config);

    public Codec<CelestialBody<C, CelestialBodyType<C>>> getCodec() {
        return this.codec;
    }

    public CelestialBody<C, CelestialBodyType<C>> configure(C config) {
        return new CelestialBody<>(this, config);
    }
}
