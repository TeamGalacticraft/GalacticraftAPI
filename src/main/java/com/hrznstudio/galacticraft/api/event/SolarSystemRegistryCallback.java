package com.hrznstudio.galacticraft.api.event;

import com.hrznstudio.galacticraft.api.celestialbodies.SolarSystemType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.Registry;

public interface SolarSystemRegistryCallback {
    Event<SolarSystemRegistryCallback> EVENT = EventFactory.createArrayBacked(SolarSystemRegistryCallback.class,
            (listeners) -> (registry) -> {
                for (SolarSystemRegistryCallback listener : listeners) {
                    listener.register(registry);
                }
            });

    void register(Registry<SolarSystemType> registry);
}
