package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.data.MinecraftServerTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.NonBlockingThreadExecutor;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.world.PersistentStateManager;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftServer.class})
@Implements(@Interface(iface = MinecraftServerTeamsGetter.class, prefix = "gcr$"))
public abstract class MinecraftServerMixin extends NonBlockingThreadExecutor<ServerTask> implements SnooperListener, CommandOutput, AutoCloseable, Runnable {

    public final ServerTeams gcr_teams = new ServerTeams((MinecraftServer)(Object)this);

    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Inject(
            method = "initScoreboard(Lnet/minecraft/world/PersistentStateManager;)V",
            at = @At("INVOKE")
    )
    private void initScoreboard(PersistentStateManager manager, CallbackInfo info) {
        TeamsState state = manager.getOrCreate(TeamsState::new, "gcr-teams");
        state.setTeams(this.gcr_teams);
        this.gcr_teams.addListener(new TeamsSync(state));
    }

    public ServerTeams gcr$getSpaceRaceTeams() {
        return this.gcr_teams;
    }
}
