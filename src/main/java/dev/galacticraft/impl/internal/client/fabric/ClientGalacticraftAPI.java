/*
 * Copyright (c) 2019-2021 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.impl.internal.client.fabric;

import dev.galacticraft.api.accessor.ClientResearchAccessor;
import dev.galacticraft.api.accessor.SatelliteAccessor;
import dev.galacticraft.api.team.network.ClientTeamsProvider;
import dev.galacticraft.api.team.network.PacketType;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.impl.internal.fabric.GalacticraftAPI;
import dev.galacticraft.impl.universe.celestialbody.type.SatelliteType;
import dev.galacticraft.impl.universe.position.config.SatelliteConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ClientGalacticraftAPI implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GalacticraftAPI.LOGGER.info("Loaded client module");
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "research_update"), (client, networkHandler, buffer, sender) -> {
            PacketByteBuf buf = new PacketByteBuf(buffer.copy());
            client.execute(() -> ((ClientResearchAccessor) Objects.requireNonNull(client.player)).readChanges(buf));
        });
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "add_satellite"), (client, networkHandler, buffer, sender) -> {
            ((SatelliteAccessor) networkHandler).addSatellite(buffer.readIdentifier(), new CelestialBody<>(SatelliteType.INSTANCE, SatelliteConfig.CODEC.decode(NbtOps.INSTANCE, buffer.readNbt()).get().orThrow().getFirst()));
        });
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "remove_satellite"), (client, networkHandler, buffer, sender) -> {
            PacketByteBuf buf = new PacketByteBuf(buffer.copy());
            ((SatelliteAccessor) networkHandler).removeSatellite(buf.readIdentifier());
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "team_update"), (client, handler, buf, responseSender) -> {
            ((ClientTeamsProvider)client.player).handleGalacticraftTeamPacket(PacketType.UPDATE, new PacketByteBuf(buf.copy()));
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "team_invite"), (client, handler, buf, responseSender) -> {
            ((ClientTeamsProvider)client.player).handleGalacticraftTeamPacket(PacketType.INVITE, new PacketByteBuf(buf.copy()));
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "team_delete"), (client, handler, buf, responseSender) -> {
            ((ClientTeamsProvider)client.player).handleGalacticraftTeamPacket(PacketType.DELETE, new PacketByteBuf(buf.copy()));
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "team_remove"), (client, handler, buf, responseSender) -> {
            ((ClientTeamsProvider)client.player).handleGalacticraftTeamPacket(PacketType.REMOVE, new PacketByteBuf(buf.copy()));
        });
    }
}
