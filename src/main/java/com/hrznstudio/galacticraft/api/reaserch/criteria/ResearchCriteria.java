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
import net.minecraft.util.registry.Registry;

public abstract class ResearchCriteria<C extends ResearchCriteriaConfig> {
   private final Codec<ConfiguredResearchCriteria<C, ResearchCriteria<C>>> codec;

   private static <C extends ResearchCriteriaConfig, F extends ResearchCriteria<C>> F register(String name, F reward) {
      return Registry.register(AddonRegistry.RESEARCH_CRITERIA, name, reward);
   }

   public ResearchCriteria(Codec<C> configCodec) {
      this.codec = configCodec.fieldOf("config").xmap((config) -> new ConfiguredResearchCriteria<>(this, config), ConfiguredResearchCriteria::getConfig).codec();
   }

   public Codec<ConfiguredResearchCriteria<C, ResearchCriteria<C>>> getCodec() {
      return this.codec;
   }

   public ConfiguredResearchCriteria<C, ResearchCriteria<C>> configure(C config) {
      return new ConfiguredResearchCriteria<>(this, config);
   }

   public abstract ResearchNodeProgress getProgress(PlayerEntity player, C config);
}
