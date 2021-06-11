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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Role {
    private final UUID id;
    private String name;
    private List<String> permissions;

    public Role(UUID id, String name, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putUuid("id", this.id);
        nbt.putString("name", this.name);

        NbtList list = new NbtList();
        permissions.forEach(s -> list.add(NbtString.of(s)));
        nbt.put("permissions", list);

        return nbt;
    }

    public static Role readNbt(NbtCompound nbt) {
        return new Role(
                nbt.getUuid("id"),
                nbt.getString("name"),
                nbt.getList("permissions", NbtElement.STRING_TYPE).stream()
                        .map(NbtElement::asString).collect(Collectors.toList())
        );
    }
}
