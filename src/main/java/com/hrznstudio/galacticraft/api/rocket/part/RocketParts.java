package com.hrznstudio.galacticraft.api.rocket.part;

import net.minecraft.block.Blocks;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class RocketParts {
    public static final RocketPart INVALID = RocketPart.Builder.create(new Identifier("galacticraft-api", "invalid"))
            .name(new TranslatableText("tooltip.galacticraft-api.something_went_wrong"))
            .type(RocketPartType.UPGRADE)
            .tier(-1)
            .renderState(Blocks.AIR.getDefaultState())
            .recipe(false)
            .build();
}
