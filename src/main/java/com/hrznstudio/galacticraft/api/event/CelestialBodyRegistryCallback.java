package com.hrznstudio.galacticraft.api.event;


import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.Registry;

public interface CelestialBodyRegistryCallback {
    Event<CelestialBodyRegistryCallback> EVENT = EventFactory.createArrayBacked(CelestialBodyRegistryCallback.class,
            (listeners) -> (registry) -> {
                for (CelestialBodyRegistryCallback listener : listeners) {
                    listener.register(registry);
                }
            });

    void register(Registry<CelestialBodyType> registry);
}
