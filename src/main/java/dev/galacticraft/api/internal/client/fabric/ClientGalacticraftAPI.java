/*
 * Copyright (c) 2019-2021 HRZN LTD
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

package dev.galacticraft.api.internal.client.fabric;

import dev.galacticraft.api.celestialbodies.satellite.Satellite;
import dev.galacticraft.api.internal.accessor.ClientResearchAccessor;
import dev.galacticraft.api.internal.accessor.SatelliteAccessor;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
            PacketByteBuf buf = new PacketByteBuf(buffer.copy());
            /*client.execute(() -> */((SatelliteAccessor) networkHandler).addSatellite(Satellite.fromPacket(client.getNetworkHandler().getRegistryManager(), buf))/*)*/;
        });
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "remove_satellite"), (client, networkHandler, buffer, sender) -> {
            PacketByteBuf buf = new PacketByteBuf(buffer.copy());
            /*client.execute(() -> */((SatelliteAccessor) networkHandler).removeSatellite(buf.readIdentifier())/*)*/;
        });
    }
}
