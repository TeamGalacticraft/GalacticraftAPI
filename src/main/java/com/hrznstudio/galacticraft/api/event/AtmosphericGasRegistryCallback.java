package com.hrznstudio.galacticraft.api.event;

import com.hrznstudio.galacticraft.api.atmosphere.AtmosphericGas;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.Registry;

public interface AtmosphericGasRegistryCallback {
    Event<AtmosphericGasRegistryCallback> EVENT = EventFactory.createArrayBacked(AtmosphericGasRegistryCallback.class,
            (listeners) -> (registry) -> {
                for (AtmosphericGasRegistryCallback listener : listeners) {
                    listener.register(registry);
                }
            });

    void register(Registry<AtmosphericGas> registry);
}
