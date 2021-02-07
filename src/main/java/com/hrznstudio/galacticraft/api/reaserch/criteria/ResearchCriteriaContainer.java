package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ResearchCriteriaContainer {
    public static final Codec<ResearchCriteriaContainer> CODEC = RecordCodecBuilder.create(i -> i.group(ConfiguredResearchCriteria.LIST_REGISTRY_CODEC.listOf().fieldOf("rewards").forGetter(ResearchCriteriaContainer::getRewards)).apply(i, ResearchCriteriaContainer::new));

    public static final ResearchCriteriaContainer EMPTY = new ResearchCriteriaContainer(Collections.emptyList());
    private final List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> rewards;

    public ResearchCriteriaContainer(List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> rewards) {
        this.rewards = rewards;
    }

    public List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> getRewards() {
        return rewards;
    }

    public boolean apply(ServerPlayerEntity player) {
        if (this.rewards.isEmpty()) return true;
        for (List<Supplier<ConfiguredResearchCriteria<?, ?>>> rewards : this.rewards) {
            boolean pass = true;
            for (Supplier<ConfiguredResearchCriteria<?, ?>> reward : rewards) {
                pass = reward.get().test(player);
                if (!pass) break;
            }
            if (pass) return true;
        }
        return false;
    }
}
