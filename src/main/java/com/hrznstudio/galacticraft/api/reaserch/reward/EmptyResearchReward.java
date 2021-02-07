package com.hrznstudio.galacticraft.api.reaserch.reward;

import com.mojang.serialization.Codec;
import net.minecraft.server.network.ServerPlayerEntity;

public class EmptyResearchReward extends ResearchReward<DefaultResearchRewardConfig> {
    public EmptyResearchReward(Codec<DefaultResearchRewardConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(ServerPlayerEntity player, DefaultResearchRewardConfig config) {
    }
}
