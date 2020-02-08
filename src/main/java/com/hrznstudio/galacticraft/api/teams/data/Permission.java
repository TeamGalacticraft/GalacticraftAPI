package com.hrznstudio.galacticraft.api.teams.data;

import com.hrznstudio.galacticraft.api.addon.AddonRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.UUID;

public class Permission {
    public static final Permission INVITE_PLAYER = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "invite_player")
            ).build()
    );
    public static final Permission MODIFY_FLAG = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "modify_flag")
            ).build()
    );
    public static final Permission MODIFY_NAME = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "modify_flag")
            ).build()
    );
    public static final Permission MODIFY_COLOR = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "modify_color")
            ).build()
    );
    public static final Permission MODIFY_ROLES = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "modify_roles")
            ).build()
    );

    private static Permission register(Permission permission) {
        return Registry.register(AddonRegistry.PERMISSIONS, permission.identifier, permission);
    }

    public static Permission getById(Identifier id) {
        return AddonRegistry.PERMISSIONS.get(id);
    }

    private final Identifier identifier;
    private final String translationKey;

    public Permission(Identifier identifier, String translationKey) {
        this.identifier = identifier;
        this.translationKey = translationKey;
    }

    public Identifier getIdentifier() {
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
