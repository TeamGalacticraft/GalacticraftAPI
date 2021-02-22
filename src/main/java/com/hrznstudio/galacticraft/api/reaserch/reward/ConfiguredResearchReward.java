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
