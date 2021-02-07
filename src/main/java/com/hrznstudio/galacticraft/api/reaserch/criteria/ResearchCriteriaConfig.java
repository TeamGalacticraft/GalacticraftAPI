package com.hrznstudio.galacticraft.api.reaserch.criteria;

import java.util.stream.Stream;

public interface ResearchCriteriaConfig {
   default Stream<ConfiguredResearchCriteria<?, ?>> stream() {
      return Stream.empty();
   }
}
