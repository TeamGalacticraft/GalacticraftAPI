package com.hrznstudio.galacticraft.api.teams.data;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Team {

    public Formatting color;
    public String name;
    public UUID owner;
    public Map<UUID, Identifier> players;
    public Map<Identifier, Role> roles;
    public List<UUID> invites;

    public Team(Formatting color, String name, UUID owner, Map<UUID, Identifier> players, Map<Identifier, Role> roles, List<UUID> invites) {
        this.color = color;
        this.name = name;
        this.owner = owner;
        this.players = players;
        this.roles = roles;
        this.invites = invites;
    }
}
