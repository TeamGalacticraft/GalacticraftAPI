package com.hrznstudio.galacticraft.api.teams.data;

import com.hrznstudio.galacticraft.api.addon.AddonRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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
    public static final Permission CREATE_ROLE = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "create_role")
            ).build()
    );
    public static final Permission DELETE_ROLE = register(
            new Permission.Builder(
                    new Identifier("galacticraft-api", "delete_role")
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
