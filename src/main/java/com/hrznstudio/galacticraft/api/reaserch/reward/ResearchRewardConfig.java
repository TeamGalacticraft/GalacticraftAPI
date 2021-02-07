package com.hrznstudio.galacticraft.api.reaserch.reward;

import java.util.stream.Stream;

public interface ResearchRewardConfig {
   default Stream<ConfiguredResearchReward<?, ?>> stream() {
      return Stream.empty();
   }
}
