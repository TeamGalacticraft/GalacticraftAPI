package com.hrznstudio.galacticraft.api.internal.data;

import com.hrznstudio.galacticraft.api.teams.Teams;

public interface ClientWorldTeamsGetter {
    Teams getSpaceRaceTeams();
    void setSpaceRaceTeams(Teams teams);
}
