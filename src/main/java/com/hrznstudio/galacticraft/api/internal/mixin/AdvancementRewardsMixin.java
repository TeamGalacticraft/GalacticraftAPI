package com.hrznstudio.galacticraft.api.internal.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hrznstudio.galacticraft.api.internal.accessor.AdvancementRewardsAccessor;
import com.hrznstudio.galacticraft.api.internal.accessor.ServerResearchAccessor;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(AdvancementRewards.class)
public abstract class AdvancementRewardsMixin implements AdvancementRewardsAccessor {
    @Unique
    @NotNull
    private Identifier @Nullable[] rocketParts = null;

    @Inject(method = "apply", at = @At("RETURN"))
    private void init_gcr(ServerPlayerEntity player, CallbackInfo ci) {
        if (this.rocketParts != null) {
            for (Identifier id : this.rocketParts) {
                ((ServerResearchAccessor) player).setUnlocked_gcr(id, true);
            }
        }
    }

    @Inject(method = "toJson", at = @At("RETURN"), cancellable = true)
    private void toJson_gcr(CallbackInfoReturnable<JsonElement> cir) {
        if (this.rocketParts != null) {
            JsonObject object = cir.getReturnValue().getAsJsonObject();
            JsonArray array = new JsonArray();
            for (Identifier id : this.rocketParts) {
                array.add(id.toString());
            }
            object.add("rocket_parts", array);
            cir.setReturnValue(object);
        }
    }

    @Inject(method = "toString", at = @At("RETURN"), cancellable = true)
    private void toString_gcr(CallbackInfoReturnable<String> cir) {
        String s = cir.getReturnValue();
        cir.setReturnValue(s.substring(0, s.length() - 1) + ", parts=" + Arrays.toString(this.rocketParts) + '}');
    }

    @Inject(method = "fromJson", at = @At("RETURN"), cancellable = true)
    private static void fromJson_gcr(JsonObject json, CallbackInfoReturnable<AdvancementRewards> cir) {
        if (json.has("rocket_parts")) {
            AdvancementRewards rewards = cir.getReturnValue();
            JsonArray array = json.get("rocket_parts").getAsJsonArray();
            Identifier[] ids = new Identifier[array.size()];
            for (int i = 0; i < array.size(); i++) {
                ids[i] = new Identifier(array.get(i).getAsString());
            }
            ((AdvancementRewardsAccessor) rewards).setRocketPartRewards(ids);
        }
    }

    @Override
    public void setRocketPartRewards(@NotNull Identifier @Nullable [] parts) {
        this.rocketParts = parts;
    }
}
