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

package dev.galacticraft.api.teams.packet;

import dev.galacticraft.api.teams.packet.listener.ServerTeamsPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.UUID;

public class TeamPlayerInviteC2SPacket implements Packet<ServerTeamsPacketListener> {

    private Identifier teamId;
    private UUID invitedPlayer;
    private UUID requestingPlayer;

    public TeamPlayerInviteC2SPacket(Identifier teamId, UUID invitedPlayer, UUID requestingPlayer) {
        this.teamId = teamId;
        this.invitedPlayer = invitedPlayer;
        this.requestingPlayer = requestingPlayer;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.teamId = buf.readIdentifier();
        this.invitedPlayer = buf.readUuid();
        this.requestingPlayer = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeIdentifier(this.teamId);
        buf.writeUuid(this.invitedPlayer);
        buf.writeUuid(this.requestingPlayer);
    }

    @Override
    public void apply(ServerTeamsPacketListener listener) {
        listener.onTeamPlayerInvite(this);
    }

    @Environment(EnvType.SERVER)
    public Identifier getTeamId() {
        return teamId;
    }

    @Environment(EnvType.SERVER)
    public UUID getInvitedPlayer() {
        return invitedPlayer;
    }

    @Environment(EnvType.SERVER)
    public UUID getRequestingPlayer() {
        return requestingPlayer;
    }
}
