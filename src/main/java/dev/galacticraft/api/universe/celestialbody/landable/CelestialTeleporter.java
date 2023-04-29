package dev.galacticraft.api.universe.celestialbody.landable;

import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * This class is for determining how a celestial should handle a player teleporting to it.
 * Such as making custom landing sequences.
 */
public interface CelestialTeleporter {
    /**
     * @param level The current world for the celestial body
     * @param player The player.
     * @param body The celestial body being landed on.
     * @param fromBody The previous celestial body the player is traveling from.
     */
    void onEnterAtmosphere(ServerLevel level, ServerPlayer player, CelestialBody<?, ?> body, CelestialBody<?, ?> fromBody);
}
