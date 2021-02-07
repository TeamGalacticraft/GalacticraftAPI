package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.mojang.serialization.Codec;

public class DefaultResearchCriteriaConfig implements ResearchCriteriaConfig {
    public static final Codec<DefaultResearchCriteriaConfig> CODEC = Codec.unit(() -> DefaultResearchCriteriaConfig.INSTANCE);
    public static final DefaultResearchCriteriaConfig INSTANCE = new DefaultResearchCriteriaConfig();
}
