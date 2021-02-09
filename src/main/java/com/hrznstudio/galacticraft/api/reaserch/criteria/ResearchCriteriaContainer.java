package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ResearchCriteriaContainer {
    public static final Codec<ResearchCriteriaContainer> CODEC = RecordCodecBuilder.create(i -> i.group(
            ConfiguredResearchCriteria.LIST_REGISTRY_CODEC.listOf().fieldOf("criteria").forGetter(ResearchCriteriaContainer::getCriteria)
    ).apply(i, ResearchCriteriaContainer::new));

    public static final ResearchCriteriaContainer EMPTY = new ResearchCriteriaContainer(Collections.emptyList());
    private final List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> criteria;

    public ResearchCriteriaContainer(List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> criteria) {
        this.criteria = criteria;
    }

    public List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> getCriteria() {
        return criteria;
    }
}
