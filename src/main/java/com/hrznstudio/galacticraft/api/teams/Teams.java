package com.hrznstudio.galacticraft.api.teams;

import com.hrznstudio.galacticraft.api.teams.data.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Teams {
    public Map<String, Team> teams = new HashMap<>();
    public Map<UUID, String> players = new HashMap<>();

    public Teams(Map<String, Team> teams, Map<UUID, String> players) {
        this.teams = teams;
        this.players = players;
    }

    public Teams() {}

    public Team getTeam(String name) {
        if(teams.containsKey(name)) {
            return teams.get(name);
        }
        return null;
    }

    public Team getTeam(UUID player) {
        if(players.containsKey(player)) {
            return getTeam(players.get(player));
        }
        return null;
    }
}
