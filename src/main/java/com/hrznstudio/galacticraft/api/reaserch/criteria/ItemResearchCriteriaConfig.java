package com.hrznstudio.galacticraft.api.reaserch.criteria;

import net.minecraft.recipe.Ingredient;

public class ItemResearchCriteriaConfig implements ResearchCriteriaConfig {
    private final Ingredient item;

    public ItemResearchCriteriaConfig(Ingredient item) {
        this.item = item;
    }

    public Ingredient getItem() {
        return item;
    }
}
