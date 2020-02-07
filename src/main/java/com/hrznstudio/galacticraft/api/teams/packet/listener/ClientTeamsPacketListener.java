package com.hrznstudio.galacticraft.api.teams.packet.listener;

import com.hrznstudio.galacticraft.api.teams.packet.*;
import net.minecraft.network.listener.PacketListener;

public interface ClientTeamsPacketListener extends PacketListener {
    void onTeamUpdate(TeamUpdateS2CPacket packet);
    void onTeamDelete(TeamDeleteS2CPacket packet);
    void onTeamInvite(TeamPlayerInviteS2CPacket packet);
    void onPlayerLeave(TeamPlayerLeaveS2CPacket packet);
}
