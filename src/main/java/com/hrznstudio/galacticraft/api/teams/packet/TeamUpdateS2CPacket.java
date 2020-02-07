package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.data.Team;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class TeamUpdateS2CPacket implements Packet<ClientTeamsPacketListener> {

    private Team team;

    public TeamUpdateS2CPacket(Team team) {
        this.team = team;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {

    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {

    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onTeamUpdate(this);
    }

    @Environment(EnvType.CLIENT)
    public Team getTeam() {
        return team;
    }
}
