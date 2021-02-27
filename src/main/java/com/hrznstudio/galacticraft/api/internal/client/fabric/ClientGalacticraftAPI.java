package com.hrznstudio.galacticraft.api.internal.client.fabric;

import com.hrznstudio.galacticraft.api.internal.accessor.ClientResearchAccessor;
import com.hrznstudio.galacticraft.api.internal.fabric.GalacticraftAPI;
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
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(GalacticraftAPI.MOD_ID, "research_update"), (client, networkHandler, buffer, sender) -> {
            PacketByteBuf buf = new PacketByteBuf(buffer.copy());
            client.execute(() -> ((ClientResearchAccessor) Objects.requireNonNull(client.player)).readChanges(buf));
        });
    }
}
