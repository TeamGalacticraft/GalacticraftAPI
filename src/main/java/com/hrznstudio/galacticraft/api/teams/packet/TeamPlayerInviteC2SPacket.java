package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamPlayerInviteC2SPacket implements Packet<ServerTeamsPacketListener> {

    private Identifier teamId;
    private UUID invitedPlayer;
    private UUID requestingPlayer;

    public TeamPlayerInviteC2SPacket(Identifier teamId, UUID invitedPlayer, UUID requestingPlayer) {
        this.teamId = teamId;
        this.invitedPlayer = invitedPlayer;
        this.requestingPlayer = requestingPlayer;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.teamId = buf.readIdentifier();
        this.invitedPlayer = buf.readUuid();
        this.requestingPlayer = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeIdentifier(this.teamId);
        buf.writeUuid(this.invitedPlayer);
        buf.writeUuid(this.requestingPlayer);
    }

    @Override
    public void apply(ServerTeamsPacketListener listener) {
        listener.onTeamPlayerInvite(this);
    }

    @Environment(EnvType.SERVER)
    public Identifier getTeamId() {
        return teamId;
    }

    @Environment(EnvType.SERVER)
    public UUID getInvitedPlayer() {
        return invitedPlayer;
    }

    @Environment(EnvType.SERVER)
    public UUID getRequestingPlayer() {
        return requestingPlayer;
    }
}
