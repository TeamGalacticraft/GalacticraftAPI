package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.hrznstudio.galacticraft.api.reaserch.progress.ResearchNodeProgress;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.dynamic.RegistryElementCodec;

import java.util.List;
import java.util.function.Supplier;

public class ConfiguredResearchCriteria<C extends ResearchCriteriaConfig, T extends ResearchCriteria<C>> {
   public static final Codec<ConfiguredResearchCriteria<?, ?>> CODEC = AddonRegistry.RESEARCH_CRITERIA.dispatch((configuredResearchCriteria) -> configuredResearchCriteria.criteria, ResearchCriteria::getCodec);;
   public static final Codec<Supplier<ConfiguredResearchCriteria<?, ?>>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.CONFIGURED_RESEARCH_CRITERIA_KEY, CODEC);
   public static final Codec<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> LIST_REGISTRY_CODEC = RegistryElementCodec.method_31194(AddonRegistry.CONFIGURED_RESEARCH_CRITERIA_KEY, CODEC);;
   private final T criteria;
   private final C config;

   public ConfiguredResearchCriteria(T criteria, C config) {
      this.criteria = criteria;
      this.config = config;
   }

   public T getCriteria() {
      return this.criteria;
   }

   public C getConfig() {
      return this.config;
   }

   public ResearchNodeProgress getProgress(PlayerEntity player) {
      return this.criteria.getProgress(player, this.config);
   }
}
