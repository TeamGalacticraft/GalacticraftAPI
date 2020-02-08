package com.hrznstudio.galacticraft.api.teams;

import com.hrznstudio.galacticraft.api.teams.data.Role;
import com.hrznstudio.galacticraft.api.teams.data.Team;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
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

    public void updateTeam(Team team) {
        updateTeam(TeamsTagUtil.formatTeamName(team.name), team);
    }

    public void updateTeam(String name, Team team) {
        this.teams.put(name, team);
    }

    public void deleteTeam(String name) {
        teams.remove(name);
    }

    public Team getTeam(String name) {
        if(teams.containsKey(name)) {
            return teams.get(name);
        }
        if(teams.containsKey(TeamsTagUtil.formatTeamName(name))) {
            return teams.get(TeamsTagUtil.formatTeamName(name));
        }
        return null;
    }

    public void createTeam(UUID owner, String name) {
        teams.put(TeamsTagUtil.formatTeamName(name), new Team(
                new Identifier("gc-teams", TeamsTagUtil.formatTeamName(name)),
                Formatting.BLUE,
                name,
                owner,
                new HashMap<UUID, Identifier>(),
                new HashMap<Identifier, Role>(),
                new ArrayList<UUID>()
        ));
    }

    public Team getTeam(UUID player) {
        if(players.containsKey(player)) {
            return getTeam(players.get(player));
        }
        return null;
    }
}
