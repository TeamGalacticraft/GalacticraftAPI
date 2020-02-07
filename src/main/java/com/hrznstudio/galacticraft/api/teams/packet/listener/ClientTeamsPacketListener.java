package com.hrznstudio.galacticraft.api.teams.packet.listener;

import com.hrznstudio.galacticraft.api.teams.packet.TeamDeleteS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamUpdateS2CPacket;
import net.minecraft.network.listener.PacketListener;

public interface ClientTeamsPacketListener extends PacketListener {
    void onTeamUpdate(TeamUpdateS2CPacket packet);
    void onTeamDelete(TeamDeleteS2CPacket packet);
}
