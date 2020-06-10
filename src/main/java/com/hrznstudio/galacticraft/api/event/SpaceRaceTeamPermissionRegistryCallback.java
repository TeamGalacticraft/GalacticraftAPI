package com.hrznstudio.galacticraft.api.event;

import com.hrznstudio.galacticraft.api.teams.data.Permission;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.Registry;

public interface SpaceRaceTeamPermissionRegistryCallback {
    Event<SpaceRaceTeamPermissionRegistryCallback> EVENT = EventFactory.createArrayBacked(SpaceRaceTeamPermissionRegistryCallback.class,
            (listeners) -> (registry) -> {
                for (SpaceRaceTeamPermissionRegistryCallback listener : listeners) {
                    listener.register(registry);
                }
            });
    void register(Registry<Permission> registry);
}
