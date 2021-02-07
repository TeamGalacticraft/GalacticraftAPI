package com.hrznstudio.galacticraft.api.internal.serialiaztion;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public interface CodecProvider<T> {
    Identifier getId();

    Codec<T> getCodec();
}
