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
