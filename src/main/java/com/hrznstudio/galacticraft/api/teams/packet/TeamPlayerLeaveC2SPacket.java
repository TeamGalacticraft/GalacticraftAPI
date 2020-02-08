package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamPlayerLeaveC2SPacket implements Packet<ServerTeamsPacketListener> {

    private UUID player;

    public TeamPlayerLeaveC2SPacket(UUID player) {
        this.player = player;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.player = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeUuid(this.player);
    }

    @Override
    public void apply(ServerTeamsPacketListener listener) {
        listener.onTeamPlayerLeave(this);
    }

    @Environment(EnvType.SERVER)
    public UUID getPlayer() {
        return player;
    }
}
