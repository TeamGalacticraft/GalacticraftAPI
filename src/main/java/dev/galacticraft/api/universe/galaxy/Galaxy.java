package dev.galacticraft.api.universe.galaxy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import dev.galacticraft.api.universe.position.CelestialPosition;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

public record Galaxy(@NotNull TranslatableText name, @NotNull TranslatableText description, CelestialPosition<?, ?> position, CelestialDisplay<?, ?> display) {
    public static final Codec<Galaxy> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(Galaxy::name),
            Codec.STRING.fieldOf("description").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(Galaxy::description),
            CelestialPosition.CODEC.fieldOf("position").forGetter(Galaxy::position),
            CelestialDisplay.CODEC.fieldOf("display").forGetter(Galaxy::display)
    ).apply(instance, Galaxy::new));
}
