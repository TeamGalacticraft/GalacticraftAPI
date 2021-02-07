package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.RegistryElementCodec;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConfiguredResearchCriteria<C extends ResearchCriteriaConfig, T extends ResearchCriteria<C>> implements Predicate<ServerPlayerEntity> {
   public static final Codec<ConfiguredResearchCriteria<?, ?>> CODEC = AddonRegistry.RESEARCH_CRITERIA.dispatch((configuredResearchCriteria) -> configuredResearchCriteria.reward, ResearchCriteria::getCodec);;
   public static final Codec<Supplier<ConfiguredResearchCriteria<?, ?>>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.CONFIGURED_RESEARCH_CRITERIA_KEY, CODEC);
   public static final Codec<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> LIST_REGISTRY_CODEC = RegistryElementCodec.method_31194(AddonRegistry.CONFIGURED_RESEARCH_CRITERIA_KEY, CODEC);;
   public final T reward;
   public final C config;

   public ConfiguredResearchCriteria(T reward, C config) {
      this.reward = reward;
      this.config = config;
   }

   public T getReward() {
      return this.reward;
   }

   public C getConfig() {
      return this.config;
   }

   public boolean test(ServerPlayerEntity player) {
      return this.reward.test(player, this.config);
   }

   public Stream<ConfiguredResearchCriteria<?, ?>> stream() {
      return Stream.concat(Stream.of(this), this.config.stream());
   }
}
