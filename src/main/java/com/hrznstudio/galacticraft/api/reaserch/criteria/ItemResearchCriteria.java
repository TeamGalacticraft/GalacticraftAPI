package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.hrznstudio.galacticraft.api.reaserch.progress.ResearchNodeProgress;
import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;

public class ItemResearchCriteria extends ResearchCriteria<ItemResearchCriteriaConfig> {
    public ItemResearchCriteria(Codec<ItemResearchCriteriaConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public ResearchNodeProgress getProgress(PlayerEntity player, ItemResearchCriteriaConfig config) {
        return new ResearchNodeProgress();
    }
}
