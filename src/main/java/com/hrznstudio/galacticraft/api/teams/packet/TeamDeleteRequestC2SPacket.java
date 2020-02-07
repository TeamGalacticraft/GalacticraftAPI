package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamDeleteRequestC2SPacket implements Packet<ServerTeamsPacketListener> {

    public UUID requester;
    public String name;

    public TeamDeleteRequestC2SPacket(UUID requester, String name) {
        this.requester = requester;
        this.name = name;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.requester = buf.readUuid();
        this.name = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeUuid(this.requester);
        buf.writeString(this.name);
    }

    @Override
    public void apply(ServerTeamsPacketListener listener) {
        listener.onTeamDeleteRequest(this);
    }

    @Environment(EnvType.SERVER)
    public UUID getRequester() {
        return requester;
    }

    @Environment(EnvType.SERVER)
    public String getName() {
        return name;
    }
}
