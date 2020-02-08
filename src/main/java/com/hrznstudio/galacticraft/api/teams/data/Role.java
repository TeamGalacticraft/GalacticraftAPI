package com.hrznstudio.galacticraft.api.teams.data;

import java.util.List;

public class Role {

    public String name;
    public List<Permission> permissions;

    public Role(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
