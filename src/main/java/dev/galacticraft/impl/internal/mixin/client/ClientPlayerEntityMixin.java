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

package dev.galacticraft.impl.internal.mixin.client;

import dev.galacticraft.api.accessor.ClientResearchAccessor;
import dev.galacticraft.api.team.Team;
import dev.galacticraft.api.team.network.ClientTeamsProvider;
import dev.galacticraft.api.team.network.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements ClientResearchAccessor, ClientTeamsProvider {
    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    @Unique
    private final List<Identifier> unlockedResearch = new ArrayList<>();
    private @Unique Team galacticraftTeam;

    @Override
    public void readChanges(PacketByteBuf buf) {
        byte size = buf.readByte();

        for (byte i = 0; i < size; i++) {
            if (buf.readBoolean()) {
                this.unlockedResearch.add(new Identifier(buf.readString()));
            } else {
                this.unlockedResearch.remove(new Identifier(buf.readString()));
            }
        }
    }

    @Override
    public boolean hasUnlocked_gcr(Identifier id) {
        return this.unlockedResearch.contains(id);
    }

    @Override
    public Team getGalacticraftTeam() {
        return this.galacticraftTeam;
    }

    @Override
    public void setGalacticraftTeam(Team team) {
        this.galacticraftTeam = team;
    }

    @Override
    public void handleGalacticraftTeamPacket(PacketType type, PacketByteBuf buf) {
        switch (type) {
            case DELETE -> {
                this.sendMessage(new LiteralText("[Galacticraft]: %s has been disbanded.".formatted(this.galacticraftTeam.getName())), false);
                this.galacticraftTeam = null;
            }
            case UPDATE -> this.galacticraftTeam = Team.readNbt(buf.readNbt()); // @todo: add more notifs to team update
            case REMOVE -> {
                this.sendMessage(new LiteralText("[Galacticraft]: You have been kicked from %s.".formatted(this.galacticraftTeam.getName())), false);
                this.galacticraftTeam = null;
            }
            case INVITE -> {
                this.sendMessage(new LiteralText("[Galacticraft]: You have received an invite to join %s".formatted(this.galacticraftTeam.getName())), false);
                this.addGalacticraftInvite(buf.readUuid());
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
