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

package com.hrznstudio.galacticraft.api.teams.data;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

public class Team {

    public Identifier id;
    public Formatting color;
    public String name;
    public UUID owner;
    public Map<UUID, Identifier> players;
    public Map<Identifier, Role> roles;
    public List<UUID> invites;

    public Team(Identifier id, Formatting color, String name, UUID owner, Map<UUID, Identifier> players, Map<Identifier, Role> roles, List<UUID> invites) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.owner = owner;
        this.players = players;
        this.roles = roles;
        this.invites = invites;
    }

    public static Team fromTag(CompoundTag tag) {
        Identifier id = Identifier.tryParse(tag.getString("id"));
        String name = tag.getString("name");
        Formatting color = Formatting.byColorIndex(tag.getInt("color"));
        UUID owner = UUID.fromString(tag.getString("owner"));

        CompoundTag playersTag = tag.getCompound("players");
        Map<UUID, Identifier> players = new HashMap<>();
        for(String playerKey : playersTag.getKeys()) {
            players.put(UUID.fromString(playerKey), Identifier.tryParse(playersTag.getString(playerKey)));
        }

        CompoundTag rolesTag = tag.getCompound("roles");
        Map<Identifier, Role> roles = new HashMap<>();
        for(String roleKey : rolesTag.getKeys()) {
            String roleName = rolesTag.getCompound(roleKey).getString("name");
            ListTag permissionsTag = rolesTag.getCompound(roleKey).getList("permissions", 8);
            List<Permission> permissions = new ArrayList<>();
            for(Tag s : permissionsTag) {
                permissions.add(AddonRegistry.PERMISSIONS.get(Identifier.tryParse(s.toString()))); //TODO: make this not die if anything goes wrong.
            }
            roles.put(Identifier.tryParse(roleKey), new Role(roleName, permissions));
        }

        ListTag invitesTag = tag.getList("invites", 8);
        List<UUID> invites = new ArrayList<>();
        for(Tag s : invitesTag) {
            invites.add(UUID.fromString(s.toString())); //TODO: again this could break things.
        }

        return new Team(id, color, name, owner, players, roles, invites);
    }
    
    public CompoundTag toTag() {
        CompoundTag tTag = new CompoundTag();
        tTag.putString("id", this.id.toString());
        tTag.putString("name", this.name);
        tTag.putInt("color", this.color.getColorIndex());
        tTag.putUuid("owner", this.owner);

        CompoundTag players = new CompoundTag();
        for(Map.Entry<UUID, Identifier> p : this.players.entrySet()) {
            players.putString(p.getKey().toString(), p.getValue().toString());
        }
        tTag.put("players", players);

        CompoundTag roles = new CompoundTag();
        for(Map.Entry<Identifier, Role> r : this.roles.entrySet()) {
            CompoundTag role = new CompoundTag();
            role.putString("name", r.getValue().name);
            ListTag perms = new ListTag();
            for(Permission p : r.getValue().permissions) {
                perms.add(StringTag.of(p.getId().toString()));
            }
            role.put("permissions", perms);
            roles.put(r.getKey().toString(), role);
        }

        ListTag invites = new ListTag();
        for(UUID uuid : this.invites) {
            invites.add(StringTag.of(uuid.toString()));
        }
        tTag.put("players", players);
        tTag.put("roles", roles);
        tTag.put("invites", invites);
        return tTag;
    }
}
