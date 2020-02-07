package com.hrznstudio.galacticraft.api.teams.packet.listener;

import com.hrznstudio.galacticraft.api.teams.packet.TeamDeleteRequestC2SPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamUpdateRequestC2SPacket;
import net.minecraft.network.listener.PacketListener;

public interface ServerTeamsPacketListener extends PacketListener {
    void onTeamUpdateRequest(TeamUpdateRequestC2SPacket packet);
    void onTeamDeleteRequest(TeamDeleteRequestC2SPacket packet);
}
