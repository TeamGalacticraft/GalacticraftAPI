package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ConstantResearchCriteriaConfig implements ResearchCriteriaConfig {
    public static final Codec<ConstantResearchCriteriaConfig> CODEC = RecordCodecBuilder.create(i ->
            i.group(Codec.BOOL.fieldOf("value").forGetter(ConstantResearchCriteriaConfig::getValue))
                    .apply(i, ConstantResearchCriteriaConfig::new));

    private final boolean value;

    public ConstantResearchCriteriaConfig(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
