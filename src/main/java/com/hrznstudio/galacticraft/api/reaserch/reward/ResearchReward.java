package com.hrznstudio.galacticraft.api.reaserch.reward;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;

public abstract class ResearchReward<C extends ResearchRewardConfig> {
   private final Codec<ConfiguredResearchReward<C, ResearchReward<C>>> codec;

   private static <C extends ResearchRewardConfig, F extends ResearchReward<C>> F register(String name, F reward) {
      return Registry.register(AddonRegistry.RESEARCH_REWARDS, name, reward);
   }

   public ResearchReward(Codec<C> configCodec) {
      this.codec = configCodec.fieldOf("config").xmap((config) -> new ConfiguredResearchReward<>(this, config), (configuredResearchReward) -> configuredResearchReward.config).codec();
   }

   public Codec<ConfiguredResearchReward<C, ResearchReward<C>>> getCodec() {
      return this.codec;
   }

   public ConfiguredResearchReward<C, ?> configure(C config) {
      return new ConfiguredResearchReward<>(this, config);
   }

   public abstract void apply(ServerPlayerEntity player, C config);
}
