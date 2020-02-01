package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.data.ClientWorldTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.packet.ClientTeamsPacketListener;
import com.hrznstudio.galacticraft.api.teams.packet.TeamsS2CPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Implements(@Interface(iface = ClientTeamsPacketListener.class, prefix = "ctpl$"))
@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {

    @Shadow private ClientWorld world;

    public void ctpl$onTeamsUpdate(TeamsS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).setSpaceRaceTeams(packet.getTeams());
        }
    }
}
