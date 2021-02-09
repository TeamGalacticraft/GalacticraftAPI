package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.hrznstudio.galacticraft.api.reaserch.progress.ResearchNodeProgress;
import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;

public class ConstantResearchCriteria extends ResearchCriteria<ConstantResearchCriteriaConfig> {
    public ConstantResearchCriteria(Codec<ConstantResearchCriteriaConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public ResearchNodeProgress getProgress(PlayerEntity player, ConstantResearchCriteriaConfig config) {
        return new ResearchNodeProgress();
    }

}
