package com.hrznstudio.galacticraft.api.internal.accessor;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AdvancementRewardsAccessor {
    void setRocketPartRewards(@NotNull Identifier @Nullable[] parts);
}
