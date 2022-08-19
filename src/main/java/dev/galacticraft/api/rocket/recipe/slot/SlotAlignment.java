package dev.galacticraft.api.rocket.recipe.slot;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum SlotAlignment {
    LEFT,
    RIGHT;

    public static final Codec<SlotAlignment> CODEC = Codec.STRING.xmap(s -> SlotAlignment.valueOf(s.toUpperCase(Locale.ROOT)), a -> a.name().toLowerCase(Locale.ROOT));

    @Contract(pure = true)
    public @NotNull SlotAlignment inverse() {
        return this == LEFT ? RIGHT : LEFT;
    }
}
