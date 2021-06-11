package dev.galacticraft.api.team.network;

import dev.galacticraft.api.team.Team;
import net.minecraft.network.PacketByteBuf;

import java.util.List;
import java.util.UUID;

public interface ClientTeamsProvider {

    Team getGalacticraftTeam();
    void setGalacticraftTeam(Team team);

    List<UUID> getGalacticraftInvites();
    void addGalacticraftInvite(UUID uuid);

    void handleGalacticraftTeamPacket(PacketType type, PacketByteBuf buf);
}
