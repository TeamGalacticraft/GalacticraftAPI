package com.hrznstudio.galacticraft.api.internal.accessor;

import net.minecraft.network.PacketByteBuf;

public interface ClientResearchAccessor extends ResearchAccessor {
    void readChanges(PacketByteBuf buf);
}
