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

package dev.galacticraft.api.teams.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.registry.AddonRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;

import java.util.List;
import java.util.UUID;

public class Permission {
    public static final Codec<Permission> CODEC = RecordCodecBuilder.create(permissionInstance ->
            permissionInstance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(Permission::getId),
                    Codec.STRING.fieldOf("translation_key").forGetter(Permission::getTranslationKey)
            ).apply(permissionInstance, Permission::new)
    );

    public static final Permission INVITE_PLAYER = new Permission.Builder(
            new Identifier(GalacticraftAPI.MOD_ID, "invite_player")
    ).build();
    public static final Permission MODIFY_FLAG = new Permission.Builder(
            new Identifier(GalacticraftAPI.MOD_ID, "modify_flag")
    ).build();
    public static final Permission MODIFY_NAME = new Permission.Builder(
            new Identifier(GalacticraftAPI.MOD_ID, "modify_name")
    ).build();
    public static final Permission MODIFY_COLOR = new Permission.Builder(
            new Identifier(GalacticraftAPI.MOD_ID, "modify_color")
    ).build();
    public static final Permission MODIFY_ROLES = new Permission.Builder(
            new Identifier(GalacticraftAPI.MOD_ID, "modify_roles")
    ).build();
    public static final Permission ACCESS_SPACE_STATION = new Permission.Builder(
            new Identifier(GalacticraftAPI.MOD_ID, "access_space_station")
    ).build();

    public static Permission deserialize(DynamicRegistryManager registryManager, Dynamic<?> dynamic) {
        return registryManager.get(AddonRegistry.PERMISSION_KEY).get(new Identifier(dynamic.asString("")));
    }

    public static MutableRegistry<Permission> getAll(DynamicRegistryManager registryManager) {
        return registryManager.get(AddonRegistry.PERMISSION_KEY);
    }
    
    public static Permission getById(DynamicRegistryManager registryManager, Identifier id) {
        return registryManager.get(AddonRegistry.PERMISSION_KEY).get(id);
    }

    public static Identifier getId(DynamicRegistryManager registryManager, Permission type) {
        return registryManager.get(AddonRegistry.PERMISSION_KEY).getId(type);
    }
    
    private final Identifier identifier;
    private final String translationKey;

    public Permission(Identifier identifier, String translationKey) {
        this.identifier = identifier;
        this.translationKey = translationKey;
    }

    public Identifier getId() {
        return identifier;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public static boolean canModify(UUID player, Team oldTeam, Team newTeam) {
        List<Permission> permissions = oldTeam.roles.get(oldTeam.players.get(player)).permissions;
        if(oldTeam.owner.equals(player)) return true;
        if(permissions.isEmpty()) return false;
        if(oldTeam == newTeam) return false; // this should never happen but still good to check

        if(oldTeam.color != newTeam.color) return permissions.contains(Permission.MODIFY_COLOR);
        if(oldTeam.owner != newTeam.owner) return oldTeam.owner.equals(player);
        if(oldTeam.roles != newTeam.roles) return permissions.contains(MODIFY_ROLES);
        return true;
    }

    public static class Builder {

        private final Identifier identifier;
        private String translationKey;

        public Builder(Identifier identifier) {
            this.identifier = identifier;
        }

        /**
         * Optional, if not used one will be generated based on the provided identifier.
         * <pre><code>ui.NAMESPACE.team.perm.PATH</code></pre>
         * @param key Key uses for I18n translation
         * @return Current builder
         */
        public Builder translationKey(String key) {
            this.translationKey = key;
            return this;
        }

        public Permission build() {
            if(this.translationKey == null || this.translationKey.isEmpty()) {
                this.translationKey = "ui." + this.identifier.getNamespace() + ".team.perm." + this.identifier.getPath();
            }
            return new Permission(this.identifier, this.translationKey);
        }
    }
}
