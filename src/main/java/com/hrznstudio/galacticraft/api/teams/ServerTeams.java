package com.hrznstudio.galacticraft.api.teams;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class ServerTeams extends Teams {
    private MinecraftServer server;
    private List<Runnable> listeners = new ArrayList<>();

    public ServerTeams(MinecraftServer server) {
        this.server = server;
    }

    public void addListener(TeamsSync teamsSync) {
        this.listeners.add(teamsSync);
    }

    private void runListeners() {
        for (Runnable runnable : this.listeners) {
            runnable.run();
        }
    }
}
