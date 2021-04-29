package dev.galacticraft.api.event;

import dev.galacticraft.api.atmosphere.AtmosphericGas;
import dev.galacticraft.api.celestialbody.CelestialBodyType;
import dev.galacticraft.api.celestialbody.SolarSystemType;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.teams.data.Permission;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.MutableRegistry;

@FunctionalInterface
public interface RegistrationEvent<T> {
    Event<RegistrationEvent<AtmosphericGas>> ATMOSPHERIC_GAS = EventFactory.createArrayBacked(RegistrationEvent.class, RegistrationEvent::merge);
    Event<RegistrationEvent<SolarSystemType>> SOLAR_SYSTEM_TYPE = EventFactory.createArrayBacked(RegistrationEvent.class, RegistrationEvent::merge);
    Event<RegistrationEvent<CelestialBodyType>> CELESTIAL_BODY_TYPE = EventFactory.createArrayBacked(RegistrationEvent.class, RegistrationEvent::merge);
    Event<RegistrationEvent<Permission>> PERMISSION = EventFactory.createArrayBacked(RegistrationEvent.class, RegistrationEvent::merge);
    Event<RegistrationEvent<RocketPart>> ROCKET_PART = EventFactory.createArrayBacked(RegistrationEvent.class, RegistrationEvent::merge);

    void register(MutableRegistry<T> registry);

    static <T> RegistrationEvent<T> merge(RegistrationEvent<T>[] events) {
        return registry -> {
            for (RegistrationEvent<T> event : events) {
                event.register(registry);
            }
        };
    }
}
