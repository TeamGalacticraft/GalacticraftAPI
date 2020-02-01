package com.hrznstudio.galacticraft.api.teams;

import com.hrznstudio.galacticraft.api.teams.packet.TeamsS2CPacket;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;

public class ServerTeams extends Teams {
    private MinecraftServer server;
    private Runnable[] listeners = new Runnable[0];

    public ServerTeams(MinecraftServer server) {
        this.server = server;
    }

    public void addListener(TeamsSync teamsSync) {
        this.listeners = Arrays.copyOf(this.listeners, this.listeners.length);
        this.listeners[this.listeners.length - 1] = teamsSync;
    }

    private void runListeners() {
        for (Runnable runnable : this.listeners) {
            runnable.run();
        }
    }

    public void updatesTeams() {
        Packet<?> packet = new TeamsS2CPacket(this);
        for (ServerPlayerEntity p : this.server.getPlayerManager().getPlayerList()) {
            p.networkHandler.sendPacket(packet);
        }
    }
}
