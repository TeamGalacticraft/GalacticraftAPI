package com.hrznstudio.galacticraft.api.internal.mixin.client;

import com.hrznstudio.galacticraft.api.internal.accessor.ClientResearchAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements ClientResearchAccessor {
    @Unique
    private final List<Identifier> unlockedResearch = new ArrayList<>();

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
}
