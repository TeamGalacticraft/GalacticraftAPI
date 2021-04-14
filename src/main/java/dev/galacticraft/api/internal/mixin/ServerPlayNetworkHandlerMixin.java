/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.api.internal.mixin;

import dev.galacticraft.api.internal.data.MinecraftServerTeamsGetter;
import dev.galacticraft.api.teams.Teams;
import dev.galacticraft.api.teams.data.Permission;
import dev.galacticraft.api.teams.data.Team;
import dev.galacticraft.api.teams.packet.*;
import dev.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.*;

@Mixin({ServerPlayNetworkHandler.class})
@Implements(@Interface(iface = ServerTeamsPacketListener.class, prefix = "stpl$", remap = Interface.Remap.NONE))
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {

    @Shadow @Final private MinecraftServer server;

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
