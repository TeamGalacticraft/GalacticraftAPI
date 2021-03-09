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

package com.hrznstudio.galacticraft.api.internal.mixin.client;

import com.hrznstudio.galacticraft.api.celestialbodies.satellite.Satellite;
import com.hrznstudio.galacticraft.api.internal.accessor.SatelliteAccessor;
import com.hrznstudio.galacticraft.api.internal.data.ClientWorldTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.packet.TeamDeleteS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamPlayerInviteS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamPlayerLeaveS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.TeamUpdateS2CPacket;
import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Implements(@Interface(iface = ClientTeamsPacketListener.class, prefix = "ctpl$", remap = Interface.Remap.NONE))
@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener, SatelliteAccessor {
    private final List<Satellite> satellites = new ArrayList<>();
    @Shadow private ClientWorld world;

    public void ctpl$onTeamUpdate(TeamUpdateS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().updateTeam(packet.getTeam());
        }
    }

    public void ctpl$onTeamDelete(TeamDeleteS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().deleteTeam(packet.getName());
        }
    }

    public void ctpl$onTeamInvite(TeamPlayerInviteS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().getTeam(packet.getTeamId().getPath()).invites.add(packet.getInvitedPlayer());
        }
    }

    public void ctpl$onPlayerLeave(TeamPlayerLeaveS2CPacket packet) {
        if(this.world != null) {
            ((ClientWorldTeamsGetter)world).getSpaceRaceTeams().getTeam(packet.getPlayer()).players.remove(packet.getPlayer());
        }
    }

    @Override
    public @Unmodifiable List<Satellite> getSatellites() {
        return this.satellites;
    }

    @Override
    public void addSatellite(Satellite satellite) {
        this.satellites.add(satellite);
    }

    @Override
    public void removeSatellite(Identifier id) {
        int index = -1;
        for (int i = 0; i < this.satellites.size(); i++) {
            if (this.satellites.get(i).getId() ==id) {
                index = i;
                break;
            }
        }
        this.satellites.remove(index);
    }
}
