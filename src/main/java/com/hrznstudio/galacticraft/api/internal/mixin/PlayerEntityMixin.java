package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.accessor.ResearchAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ResearchAccessor {
    @Override
    public boolean hasUnlocked_gcr(Identifier id) {
        throw new UnsupportedOperationException("This shouldn't be possible!");
    }
}
