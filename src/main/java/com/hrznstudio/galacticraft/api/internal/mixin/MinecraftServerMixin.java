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

package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.data.MinecraftServerTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.PersistentStateManager;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftServer.class})
@Implements(@Interface(iface = MinecraftServerTeamsGetter.class, prefix = "gcr$", remap = Interface.Remap.NONE))
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements SnooperListener, CommandOutput, AutoCloseable, Runnable {

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
