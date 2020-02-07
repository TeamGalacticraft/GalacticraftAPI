package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.data.Team;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.UUID;

public class TeamUpdateRequestC2SPacket implements Packet<ServerTeamsPacketListener> {

    private UUID requester;
    private String oldName;
    private Team updatedTeam;

    public TeamUpdateRequestC2SPacket(UUID requester, String oldName, Team updatedTeam) {
        this.requester = requester;
        this.oldName = oldName;
        this.updatedTeam = updatedTeam;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.requester = buf.readUuid();
        this.oldName = buf.readString();
        this.updatedTeam = Team.fromTag(buf.readCompoundTag());
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeUuid(this.requester);
        buf.writeString(this.oldName);
        buf.writeCompoundTag(this.updatedTeam.toTag());
    }

    @Override
    public void apply(ServerTeamsPacketListener listener) {
        listener.onTeamUpdateRequest(this);
    }

    @Environment(EnvType.SERVER)
    public UUID getRequester() {
        return requester;
    }

    @Environment(EnvType.SERVER)
    public String getOldName() {
        return oldName;
    }

    @Environment(EnvType.SERVER)
    public Team getUpdatedTeam() {
        return updatedTeam;
    }
}
