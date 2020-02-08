package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamPlayerInviteS2CPacket  implements Packet<ClientTeamsPacketListener> {

    private Identifier teamId;
    private UUID invitedPlayer;


    public TeamPlayerInviteS2CPacket(Identifier teamId, UUID invitedPlayer) {
        this.invitedPlayer = invitedPlayer;
        this.teamId = teamId;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.teamId = buf.readIdentifier();
        this.invitedPlayer = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeIdentifier(this.teamId);
        buf.writeUuid(this.invitedPlayer);
    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onTeamInvite(this);
    }

    @Environment(EnvType.CLIENT)
    public Identifier getTeamId() {
        return teamId;
    }

    @Environment(EnvType.CLIENT)
    public UUID getInvitedPlayer() {
        return invitedPlayer;
    }
}
