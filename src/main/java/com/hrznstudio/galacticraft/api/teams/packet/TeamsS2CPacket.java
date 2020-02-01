package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.Teams;
import com.hrznstudio.galacticraft.api.teams.TeamsTagUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.util.Objects;
//TODO: Optimize this, dont send all teams when just one is updated.
public class TeamsS2CPacket implements Packet<ClientTeamsPacketListener> {

    private Teams teams;

    public TeamsS2CPacket(Teams teams) {
        this.teams = teams;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.teams = TeamsTagUtil.fromTag(Objects.requireNonNull(buf.readCompoundTag()));
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeCompoundTag(TeamsTagUtil.toTag(new CompoundTag(), this.teams));
    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onTeamsUpdate(this);
    }

    @Environment(EnvType.CLIENT)
    public Teams getTeams() {
        return this.teams;
    }
}
