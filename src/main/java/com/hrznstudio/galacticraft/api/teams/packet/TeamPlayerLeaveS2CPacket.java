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

package com.hrznstudio.galacticraft.api.teams.packet;

import com.hrznstudio.galacticraft.api.teams.packet.listener.ClientTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.UUID;

public class TeamPlayerLeaveS2CPacket implements Packet<ClientTeamsPacketListener> {

    private Identifier teamId;
    private UUID player;

    public TeamPlayerLeaveS2CPacket(Identifier teamId, UUID player) {
        this.teamId = teamId;
        this.player = player;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.teamId = buf.readIdentifier();
        this.player = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeIdentifier(this.teamId);
        buf.writeUuid(this.player);
    }

    @Override
    public void apply(ClientTeamsPacketListener listener) {
        listener.onPlayerLeave(this);
    }

    @Environment(EnvType.CLIENT)
    public Identifier getTeamId() {
        return teamId;
    }

    @Environment(EnvType.CLIENT)
    public UUID getPlayer() {
        return player;
    }
}
