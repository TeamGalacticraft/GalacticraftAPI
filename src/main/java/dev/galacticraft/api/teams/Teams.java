/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.api.teams;

import dev.galacticraft.api.teams.data.Role;
import dev.galacticraft.api.teams.data.Team;
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
