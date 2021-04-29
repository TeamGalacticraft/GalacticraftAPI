package dev.galacticraft.api.rocket.part.travel;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum AccessType implements StringIdentifiable {
    /**
     * Allow other rocket parts to decide whether or not the player may visit the celestial body
     */
    PASS,
    /**
     * Forcefully block access to celestial body - overrides {@link AccessType#ALLOW}
     */
    BLOCK,
    /**
     * Allow access to celestial body.
     * Can be overridden by {@link AccessType#BLOCK}
     */
    ALLOW;

    public static final Codec<AccessType> CODEC = StringIdentifiable.createCodec(Enum::ordinal, i -> AccessType.values()[i], s -> AccessType.valueOf(s.toUpperCase(Locale.ROOT)));

    public AccessType merge(AccessType other) {
        if (other == PASS) return this;
        if (other == BLOCK || this == BLOCK) return BLOCK;
        return ALLOW;
    }

    @Override
    public String asString() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
