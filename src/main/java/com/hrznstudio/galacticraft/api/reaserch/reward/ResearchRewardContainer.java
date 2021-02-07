package com.hrznstudio.galacticraft.api.reaserch.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ResearchRewardContainer {
    public static final Codec<ResearchRewardContainer> CODEC = RecordCodecBuilder.create(i -> i.group(ConfiguredResearchReward.LIST_REGISTRY_CODEC.fieldOf("rewards").forGetter(ResearchRewardContainer::getRewards)).apply(i, ResearchRewardContainer::new));

    public static final ResearchRewardContainer EMPTY = new ResearchRewardContainer();
    private final List<Supplier<ConfiguredResearchReward<?, ?>>> rewards;

    @SafeVarargs
    public ResearchRewardContainer(Supplier<ConfiguredResearchReward<?, ?>>... rewards) {
        this(Arrays.asList(rewards));
    }

    public ResearchRewardContainer(List<Supplier<ConfiguredResearchReward<?, ?>>> rewards) {
        this.rewards = rewards;
    }

    public List<Supplier<ConfiguredResearchReward<?, ?>>> getRewards() {
        return rewards;
    }

    public void apply(ServerPlayerEntity player) {
        for (Supplier<ConfiguredResearchReward<?, ?>> reward : rewards) {
            reward.get().apply(player);
        }
    }
}
