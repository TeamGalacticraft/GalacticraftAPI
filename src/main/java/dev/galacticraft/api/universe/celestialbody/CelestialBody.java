package dev.galacticraft.api.universe.celestialbody;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.galaxy.Galaxy;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CelestialBody<C extends CelestialBodyConfig, T extends CelestialBodyType<C>>(T type, C config) {
    public static final Codec<CelestialBody<?, ?>> CODEC = AddonRegistry.CELESTIAL_BODY_TYPE.dispatch(CelestialBody::type, CelestialBodyType::getCodec);

    public @NotNull TranslatableText name() {
        return this.type().name(this.config());
    }

    public @Nullable CelestialBody<?, ?> parent(DynamicRegistryManager manager) {
        return this.type().parent(manager, this.config());
    }

    public @NotNull RegistryKey<Galaxy> galaxy() {
        return this.type().galaxy(this.config());
    }

    public @NotNull TranslatableText description() {
        return this.type().description(this.config());
    }

    public @NotNull CelestialPosition<?, ?> position() {
        return this.type().position(this.config());
    }

    public @NotNull CelestialDisplay<?, ?> display() {
        return this.type().display(this.config());
    }

}
