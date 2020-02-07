package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.data.MinecraftServerTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.ServerTeams;
import com.hrznstudio.galacticraft.api.teams.Teams;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import com.hrznstudio.galacticraft.api.teams.data.Team;
import com.hrznstudio.galacticraft.api.teams.packet.*;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.*;

@Mixin({ServerPlayNetworkHandler.class})
@Implements(@Interface(iface = ServerTeamsPacketListener.class, prefix = "stpl$"))
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {

    @Shadow @Final private MinecraftServer server;

    @Shadow public ServerPlayerEntity player;

    public void stpl$onTeamUpdateRequest(TeamUpdateRequestC2SPacket packet) {
        Teams teams = ((MinecraftServerTeamsGetter)this.server).getSpaceRaceTeams();
        Team team = teams.getTeam(packet.getOldName());
        if(team.owner.equals(packet.getRequester()) || Permission.canModify(packet.getRequester(), team, packet.getUpdatedTeam())) {
            teams.updateTeam(packet.getOldName(), packet.getUpdatedTeam());
            server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(new TeamUpdateS2CPacket(packet.getOldName(), packet.getUpdatedTeam())));
        }
    }
    public void stpl$onTeamDeleteRequest(TeamDeleteRequestC2SPacket packet) {
        Teams teams = ((MinecraftServerTeamsGetter)this.server).getSpaceRaceTeams();
        Team team = teams.getTeam(packet.getName());
        if(team.owner.equals(packet.getRequester())) {
            teams.deleteTeam(packet.getName());
            server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(new TeamDeleteS2CPacket(packet.getName())));
        }
    }

    public void stpl$onTeamPlayerInvite(TeamPlayerInviteC2SPacket packet) {
        Teams teams = ((MinecraftServerTeamsGetter)this.server).getSpaceRaceTeams();
        Team team = teams.getTeam(packet.getTeamId().getPath());
        if(team.players.containsKey(packet.getRequestingPlayer())
                && team.roles.get(team.players.get(packet.getRequestingPlayer())).permissions.contains(Permission.INVITE_PLAYER)
                && !team.invites.contains(packet.getInvitedPlayer())) {
            team.invites.add(packet.getInvitedPlayer());
            server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(new TeamPlayerInviteS2CPacket(team.id, packet.getInvitedPlayer())));
        }
    }

    public void stpl$onTeamPlayerLeave(TeamPlayerLeaveC2SPacket packet) {
        Teams teams = ((MinecraftServerTeamsGetter)this.server).getSpaceRaceTeams();
        Team team = teams.getTeam(packet.getPlayer());
        if(team != null) {
            team.players.remove(packet.getPlayer());
            server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(new TeamPlayerLeaveS2CPacket(team.id, packet.getPlayer())));
        }
    }

    public void stpl$onTeamCreate(TeamCreateC2SPacket packet) {
        Teams teams = ((MinecraftServerTeamsGetter)this.server).getSpaceRaceTeams();
        Team team = teams.getTeam(packet.getOwner());
        if(team != null) {
            teams.createTeam(packet.getOwner(), packet.getName());
            server.getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(new TeamUpdateS2CPacket(packet.getName(), teams.getTeam(packet.getOwner()))));
        }
    }
}
