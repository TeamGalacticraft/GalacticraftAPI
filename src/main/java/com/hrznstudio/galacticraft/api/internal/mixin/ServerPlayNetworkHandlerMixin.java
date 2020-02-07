package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.data.MinecraftServerTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.ServerTeams;
import com.hrznstudio.galacticraft.api.teams.Teams;
import com.hrznstudio.galacticraft.api.teams.data.Team;
import com.hrznstudio.galacticraft.api.teams.packet.TeamUpdateRequestC2SPacket;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.*;

@Mixin({ServerPlayNetworkHandler.class})
@Implements(@Interface(iface = ServerTeamsPacketListener.class, prefix = "stpl$"))
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {

    @Shadow @Final private MinecraftServer server;

    public void stpl$onTeamUpdateRequest(TeamUpdateRequestC2SPacket packet) {
        Teams teams = ((MinecraftServerTeamsGetter)this.server).getSpaceRaceTeams();
        Team team = teams.getTeam(packet.getOldName());
        if(team.owner.equals(packet.getRequester())) {
            teams.updateTeam(packet.getOldName(), packet.getUpdatedTeam());
        }
    }
}
