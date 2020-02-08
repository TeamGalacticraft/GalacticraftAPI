package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class TeamDeleteS2CPacket implements Packet<ClientTeamsPacketListener> {

    private String name;

    public TeamDeleteS2CPacket(String name) {
        this.name = name;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.name = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeString(this.name);
    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onTeamDelete(this);
    }

    @Environment(EnvType.CLIENT)
    public String getName() {
        return name;
    }
}
