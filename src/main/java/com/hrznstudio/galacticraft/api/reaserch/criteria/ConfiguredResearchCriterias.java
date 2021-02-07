package com.hrznstudio.galacticraft.api.reaserch.criteria;

public class ConfiguredResearchCriterias {
    public static final ConfiguredResearchCriteria<ConstantResearchCriteriaConfig, ?> ALWAYS_TRUE = ResearchCriterias.CONSTANT_RESEARCH_CRITERIA.configure(new ConstantResearchCriteriaConfig(true));
    public static final ConfiguredResearchCriteria<ConstantResearchCriteriaConfig, ?> ALWAYS_FALSE = ResearchCriterias.CONSTANT_RESEARCH_CRITERIA.configure(new ConstantResearchCriteriaConfig(false));
}
