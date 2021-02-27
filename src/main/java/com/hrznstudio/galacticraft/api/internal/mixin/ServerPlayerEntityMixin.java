package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.accessor.ServerResearchAccessor;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerResearchAccessor { //todo: FTBq compat
    @Unique
    private final List<Identifier> unlockedResearch = new ArrayList<>();
    @Unique
    private final Object2BooleanMap<Identifier> changes = new Object2BooleanArrayMap<>();

    @Override
    public boolean hasUnlocked_gcr(Identifier id) {
        return false;
    }

    @Override
    public void setUnlocked_gcr(Identifier id, boolean unlocked) {
        if (unlocked) {
            if (!this.unlockedResearch.contains(id)) {
                this.unlockedResearch.add(id);
                this.changes.put(id, true);
            }
        } else {
            if (unlockedResearch.remove(id)) {
                this.changes.put(id, false);
            }
        }
    }

    @Override
    public boolean changed_gcr() {
        return !this.changes.isEmpty();
    }

    @Override
    public PacketByteBuf writeResearchChanges_gcr(PacketByteBuf buf) {
        buf.writeByte(this.changes.size());

        for (Object2BooleanMap.Entry<Identifier> entry : this.changes.object2BooleanEntrySet()) {
            buf.writeBoolean(entry.getBooleanValue());
            buf.writeString(entry.getKey().toString());
        }
        this.changes.clear();
        return buf;
    }

    @Override
    public CompoundTag writeResearch_gcr(CompoundTag tag) {
        tag.putInt("size", this.unlockedResearch.size());
        int i = 0;
        for (Identifier id : this.unlockedResearch) {
            tag.putString("id_" + i, id.toString());
            i++;
        }
        return tag;
    }

    @Override
    public void readFromTag_gcr(CompoundTag tag) {
        this.unlockedResearch.clear();
        int size = tag.getInt("size");
        for (int i = 0; i < size; i++) {
            this.unlockedResearch.add(new Identifier(tag.getString("id_" + i)));
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    private void readTag_gcr(CompoundTag tag, CallbackInfo ci) {
        this.readFromTag_gcr(tag);
    }

    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    private void writeTag_gcr(CompoundTag tag, CallbackInfo ci) {
        this.writeResearch_gcr(tag);
    }
}
