package com.hrznstudio.galacticraft.api.teams.packet;

import net.minecraft.network.listener.PacketListener;

public interface ClientTeamsPacketListener extends PacketListener {
    void onTeamsUpdate(TeamsS2CPacket packet);
}
