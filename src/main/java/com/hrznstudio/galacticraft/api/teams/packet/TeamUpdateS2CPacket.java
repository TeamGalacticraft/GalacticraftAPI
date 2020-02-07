package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.TeamsTagUtil;
import com.hrznstudio.galacticraft.api.teams.data.Team;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class TeamUpdateS2CPacket implements Packet<ClientTeamsPacketListener> {

    private Team team;
    private String oldName;

    public TeamUpdateS2CPacket(String oldName, Team team) {
        this.team = team;
        this.oldName = oldName;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.team = Team.fromTag(buf.readCompoundTag());
        this.oldName = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeCompoundTag(team.toTag());
        buf.writeString(oldName);
    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onTeamUpdate(this);
    }

    @Environment(EnvType.CLIENT)
    public Team getTeam() {
        return team;
    }

    @Environment(EnvType.CLIENT)
    public String getOldName() {
        return oldName;
    }
}
