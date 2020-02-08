package com.hrznstudio.galacticraft.api.teams;

import com.hrznstudio.galacticraft.api.addon.AddonRegistry;
import com.hrznstudio.galacticraft.api.teams.data.Permission;
import com.hrznstudio.galacticraft.api.teams.data.Role;
import com.hrznstudio.galacticraft.api.teams.data.Team;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

public class TeamsTagUtil {

    public static CompoundTag toTag(CompoundTag tag, Teams teams) {
        CompoundTag teamsTag = new CompoundTag();
        for(Map.Entry<String, Team> t : teams.teams.entrySet()) {
            CompoundTag tTag = new CompoundTag();
            tTag.putString("id", t.getValue().id.toString());
            tTag.putString("name", t.getValue().name);
            tTag.putInt("color", t.getValue().color.getColorIndex());
            tTag.putUuid("owner", t.getValue().owner);

            CompoundTag players = new CompoundTag();
            for(Map.Entry<UUID, Identifier> p : t.getValue().players.entrySet()) {
                players.putString(p.getKey().toString(), p.getValue().toString());
            }
            tTag.put("players", players);

            CompoundTag roles = new CompoundTag();
            for(Map.Entry<Identifier, Role> r : t.getValue().roles.entrySet()) {
                CompoundTag role = new CompoundTag();
                role.putString("name", r.getValue().name);
                ListTag perms = new ListTag();
                for(Permission p : r.getValue().permissions) {
                    perms.add(new StringTag(p.getIdentifier().toString()));
                }
                role.put("permissions", perms);
                roles.put(r.getKey().toString(), role);
            }

            ListTag invites = new ListTag();
            for(UUID uuid : t.getValue().invites) {
                invites.add(new StringTag(uuid.toString()));
            }
            tTag.put("players", players);
            tTag.put("roles", roles);
            tTag.put("invites", invites);
            teamsTag.put(t.getKey(), tTag);
            tag.put("teams", teamsTag);
        }
        return tag;
    }

    public static Teams fromTag(CompoundTag tag) {
        CompoundTag teams = tag.getCompound("teams");

        Map<String, Team> teamMap = new HashMap<>();
        Map<UUID, String> playersMap = new HashMap<>();

        for(String teamKey : teams.getKeys()) {
            CompoundTag team = teams.getCompound(teamKey);
            Identifier id = Identifier.tryParse(team.getString("id"));
            String name = team.getString("name");
            Formatting color = Formatting.byColorIndex(team.getInt("color"));
            UUID owner = UUID.fromString(team.getString("owner"));

            CompoundTag playersTag = team.getCompound("players");
            Map<UUID, Identifier> players = new HashMap<>();
            for(String playerKey : playersTag.getKeys()) {
                players.put(UUID.fromString(playerKey), Identifier.tryParse(playersTag.getString(playerKey)));
                playersMap.put(UUID.fromString(playerKey), formatTeamName(name));
            }

            CompoundTag rolesTag = team.getCompound("roles");
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

            ListTag invitesTag = team.getList("invites", 8);
            List<UUID> invites = new ArrayList<>();
            for(Tag s : invitesTag) {
                invites.add(UUID.fromString(s.toString())); //TODO: again this could break things.
            }

            teamMap.put(formatTeamName(name), new Team(id, color, name, owner, players, roles, invites));
        }
        return new Teams(teamMap, playersMap);
    }

    public static String formatTeamName(String name) {
        return name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "-");
    }
}
