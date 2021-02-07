package com.hrznstudio.galacticraft.api.reaserch.reward;

import com.mojang.serialization.Codec;

public class DefaultResearchRewardConfig implements ResearchRewardConfig {
    public static final Codec<DefaultResearchRewardConfig> CODEC = Codec.unit(() -> DefaultResearchRewardConfig.INSTANCE);
    public static final DefaultResearchRewardConfig INSTANCE = new DefaultResearchRewardConfig();
}
