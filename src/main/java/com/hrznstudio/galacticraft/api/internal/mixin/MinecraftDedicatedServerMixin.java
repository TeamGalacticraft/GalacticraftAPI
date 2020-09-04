package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
@Environment(EnvType.SERVER)
public abstract class MinecraftDedicatedServerMixin {

    @Inject(method = "setupServer", at = @At("HEAD"))
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        AddonRegistry.runRegistryCallbacks();
    }
}
