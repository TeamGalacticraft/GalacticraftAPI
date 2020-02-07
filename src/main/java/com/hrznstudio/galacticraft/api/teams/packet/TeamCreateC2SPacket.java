package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamCreateC2SPacket implements Packet<ServerTeamsPacketListener> {

    private UUID owner;
    private String name;

    public TeamCreateC2SPacket(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.owner = buf.readUuid();
        this.name = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeUuid(this.owner);
        buf.writeString(this.name);
    }

    @Override
    public void apply(ServerTeamsPacketListener listener) {
        listener.onTeamCreate(this);
    }

    @Environment(EnvType.SERVER)
    public UUID getOwner() {
        return owner;
    }

    @Environment(EnvType.SERVER)
    public String getName() {
        return name;
    }
}
