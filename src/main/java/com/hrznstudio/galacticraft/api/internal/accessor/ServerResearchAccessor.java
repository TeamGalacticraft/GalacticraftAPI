package com.hrznstudio.galacticraft.api.internal.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface ServerResearchAccessor extends ResearchAccessor {
    void setUnlocked_gcr(Identifier id, boolean unlocked);

    boolean changed_gcr();

    PacketByteBuf writeResearchChanges_gcr(PacketByteBuf buf);

    CompoundTag writeResearch_gcr(CompoundTag tag);

    void readFromTag_gcr(CompoundTag tag);
}
