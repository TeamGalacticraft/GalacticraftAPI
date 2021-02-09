package com.hrznstudio.galacticraft.api.internal.mixin.client;

import com.hrznstudio.galacticraft.api.internal.data.ClientWorldTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.packet.TeamDeleteS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamPlayerInviteS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamPlayerLeaveS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamUpdateS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Implements(@Interface(iface = ClientTeamsPacketListener.class, prefix = "ctpl$", remap = Interface.Remap.NONE))
@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {

    @Shadow private ClientWorld world;

    public void ctpl$onTeamUpdate(TeamUpdateS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().updateTeam(packet.getTeam());
        }
    }

    public void ctpl$onTeamDelete(TeamDeleteS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().deleteTeam(packet.getName());
        }
    }

    public void ctpl$onTeamInvite(TeamPlayerInviteS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().getTeam(packet.getTeamId().getPath()).invites.add(packet.getInvitedPlayer());
        }
    }

    public void ctpl$onPlayerLeave(TeamPlayerLeaveS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().getTeam(packet.getPlayer()).players.remove(packet.getPlayer());
        }
    }
}
