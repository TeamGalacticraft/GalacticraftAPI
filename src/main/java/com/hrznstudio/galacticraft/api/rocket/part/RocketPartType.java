package com.hrznstudio.galacticraft.api.rocket.part;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum RocketPartType implements StringIdentifiable {
    CONE,
    BODY,
    FIN,
    BOOSTER,
    BOTTOM,
    UPGRADE;

    public static final Codec<RocketPartType> CODEC = StringIdentifiable.createCodec(Enum::ordinal, value -> RocketPartType.values()[value], s -> RocketPartType.valueOf(s.toUpperCase(Locale.ROOT)));

    @Override
    public String asString() {
        return this.toString().toLowerCase();
    }
}