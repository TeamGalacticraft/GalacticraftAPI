/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
