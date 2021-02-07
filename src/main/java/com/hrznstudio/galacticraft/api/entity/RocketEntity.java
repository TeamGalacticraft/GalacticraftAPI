package com.hrznstudio.galacticraft.api.entity;

import com.hrznstudio.galacticraft.api.rocket.LaunchStage;
import com.hrznstudio.galacticraft.api.rocket.part.RocketPart;
import com.hrznstudio.galacticraft.api.rocket.part.RocketPartType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;

public interface RocketEntity {
    float[] getColor();

    LaunchStage getStage();

    void setStage(LaunchStage stage);

    RocketPart[] getParts();

    BlockPos getLinkedPad();

    int getTier();

    void setLinkedPad(BlockPos linkedPad);

    double getSpeed();

    void setSpeed(double speed);

    void onJump();

    void setColor(float red, float green, float blue, float alpha);

    void onBaseDestroyed();

    void dropItems(DamageSource source, boolean exploded);

    void setPart(RocketPart part);

    void setParts(RocketPart[] parts);

    public RocketPart getPartForType(RocketPartType type);
}
