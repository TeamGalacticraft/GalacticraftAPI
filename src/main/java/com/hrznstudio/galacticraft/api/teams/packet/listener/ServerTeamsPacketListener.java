package com.hrznstudio.galacticraft.api.teams.packet.listener;

import com.hrznstudio.galacticraft.api.teams.packet.*;
import net.minecraft.network.listener.PacketListener;

public interface ServerTeamsPacketListener extends PacketListener {
    void onTeamUpdateRequest(TeamUpdateRequestC2SPacket packet);
    void onTeamDeleteRequest(TeamDeleteRequestC2SPacket packet);
    void onTeamPlayerInvite(TeamPlayerInviteC2SPacket packet);
    void onTeamPlayerLeave(TeamPlayerLeaveC2SPacket packet);
    void onTeamCreate(TeamCreateC2SPacket packet);
}
