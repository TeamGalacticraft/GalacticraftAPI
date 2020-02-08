package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamPlayerLeaveS2CPacket implements Packet<ClientTeamsPacketListener> {

    private Identifier teamId;
    private UUID player;

    public TeamPlayerLeaveS2CPacket(Identifier teamId, UUID player) {
        this.teamId = teamId;
        this.player = player;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.teamId = buf.readIdentifier();
        this.player = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeIdentifier(this.teamId);
        buf.writeUuid(this.player);
    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onPlayerLeave(this);
    }

    @Environment(EnvType.CLIENT)
    public Identifier getTeamId() {
        return teamId;
    }

    @Environment(EnvType.CLIENT)
    public UUID getPlayer() {
        return player;
    }
}
