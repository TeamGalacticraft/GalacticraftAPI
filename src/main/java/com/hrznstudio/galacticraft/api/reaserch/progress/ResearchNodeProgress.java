package com.hrznstudio.galacticraft.api.reaserch.progress;

import java.util.HashSet;
import java.util.Set;

public class ResearchNodeProgress {
    private final Set<ResearchCriteriaProgress> progress = new HashSet<>();

    public boolean completed() {
        return false;
    }
}
