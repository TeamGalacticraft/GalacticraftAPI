package dev.galacticraft.api.universe.celestialbody.landable;

import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public enum FallbackCelestialTeleporter implements CelestialTeleporter {
    INSTANCE;
    @Override
    public void onEnterAtmosphere(ServerLevel planet, ServerPlayer player, CelestialBody<?, ?> body, CelestialBody<?, ?> fromBody) {
        player.teleportTo(planet, player.getX(), 500, player.getZ(), player.getYRot(), player.getXRot());
    }
}
