/*
 * Copyright (c) 2019-2021 Team Galacticraft
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

package dev.galacticraft.api.team;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Team {
    private final UUID id;
    private String name;
    private UUID leader;

    private List<Role> roles = new ArrayList<>();
    private Map<UUID, UUID> members = new HashMap<>();
    private List<UUID> invites = new ArrayList<>();

    public Team(UUID id, String name, UUID leader) {
        this.id = id;
        this.leader = leader;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Map<UUID, UUID> getMembers() {
        return members;
    }

    public List<UUID> getInvites() {
        return invites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setMembers(Map<UUID, UUID> members) {
        this.members = members;
    }

    public void setInvites(List<UUID> invites) {
        this.invites = invites;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putUuid("id", this.id);
        nbt.putString("name", this.name);
        nbt.putUuid("leader", this.leader);

        NbtList rolesList = new NbtList();
        roles.forEach(r -> rolesList.add(r.writeNbt(new NbtCompound())));
        nbt.put("roles", rolesList);

        NbtList membersList = new NbtList();
        members.forEach((u ,r) -> {
            NbtCompound memberComp = new NbtCompound();
            memberComp.putUuid(u.toString(), r);
            membersList.add(memberComp);
        });
        nbt.put("members", membersList);

        NbtList invitesList = new NbtList();
        invites.forEach(i -> invitesList.add(NbtString.of(i.toString())));
        nbt.put("roles", invitesList);
        return nbt;
    }

    public static Team readNbt(NbtCompound nbt) {
        Team team = new Team(
                nbt.getUuid("id"),
                nbt.getString("name"),
                nbt.getUuid("leader")
        );
       team.setRoles(nbt.getList("roles", NbtElement.COMPOUND_TYPE).stream()
                .map(el -> Role.readNbt((NbtCompound) el)).collect(Collectors.toList()));

        team.setMembers(nbt.getList("members", NbtElement.COMPOUND_TYPE).stream()
                        .map(el -> {
                            UUID user = UUID.fromString(((NbtCompound)el).getKeys().stream().findFirst().orElse(""));
                            UUID role = ((NbtCompound) el).getUuid(user.toString());
                            return new Pair<>(user, role);
                        }).collect(HashMap::new, (map, pair) -> {map.put(pair.getLeft(), pair.getRight());}, HashMap::putAll));

        team.setInvites(nbt.getList("roles", NbtElement.STRING_TYPE).stream()
                        .map(el -> UUID.fromString(el.asString())).collect(Collectors.toList()));

        return team;
    }
}
