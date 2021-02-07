package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.mojang.serialization.Codec;
import net.minecraft.server.network.ServerPlayerEntity;

public class ConstantResearchCriteria extends ResearchCriteria<ConstantResearchCriteriaConfig> {
    public ConstantResearchCriteria(Codec<ConstantResearchCriteriaConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean test(ServerPlayerEntity player, ConstantResearchCriteriaConfig config) {
        return config.getValue();
    }
}
