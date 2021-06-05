package dev.galacticraft.api.registry;

import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Optional;

public class RegistryUtil {
    private RegistryUtil() {}

    public static <T> Registry<T> getRegistry(DynamicRegistryManager manager, RegistryKey<Registry<T>> key) {
        return manager.get(key);
    }

    public static Optional<CelestialBody<?, ?>> getCelestialBodyByDimension(DynamicRegistryManager manager, RegistryKey<World> key) {
        return manager.get(AddonRegistry.CELESTIAL_BODY_KEY).stream().filter(body -> body.type() instanceof Landable && ((Landable) body.type()).world(body.config()).equals(key)).findFirst();
    }
}
