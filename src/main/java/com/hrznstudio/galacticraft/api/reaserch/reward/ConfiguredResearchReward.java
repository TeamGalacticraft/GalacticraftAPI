package com.hrznstudio.galacticraft.api.reaserch.reward;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.RegistryElementCodec;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConfiguredResearchReward<C extends ResearchRewardConfig, T extends ResearchReward<C>> implements Consumer<ServerPlayerEntity> {
   public static final Codec<ConfiguredResearchReward<?, ?>> CODEC = AddonRegistry.RESEARCH_REWARDS.dispatch((configuredResearchReward) -> configuredResearchReward.reward, ResearchReward::getCodec);;
   public static final Codec<Supplier<ConfiguredResearchReward<?, ?>>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.CONFIGURED_RESEARCH_REWARD_KEY, CODEC);;
   public static final Codec<List<Supplier<ConfiguredResearchReward<?, ?>>>> LIST_REGISTRY_CODEC = RegistryElementCodec.method_31194(AddonRegistry.CONFIGURED_RESEARCH_REWARD_KEY, CODEC);;
   public final T reward;
   public final C config;

   public ConfiguredResearchReward(T reward, C config) {
      this.reward = reward;
      this.config = config;
   }

   public T getReward() {
      return this.reward;
   }

   public C getConfig() {
      return this.config;
   }

   public void apply(ServerPlayerEntity player) {
      this.reward.apply(player, this.config);
   }

   public Stream<ConfiguredResearchReward<?, ?>> stream() {
      return Stream.concat(Stream.of(this), this.config.stream());
   }

   @Override
   public void accept(ServerPlayerEntity playerEntity) {
      this.apply(playerEntity);
   }
}
