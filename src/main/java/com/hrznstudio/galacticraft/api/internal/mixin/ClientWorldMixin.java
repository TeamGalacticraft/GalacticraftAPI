package com.hrznstudio.galacticraft.api.internal.mixin;

import com.hrznstudio.galacticraft.api.internal.data.ClientWorldTeamsGetter;
import com.hrznstudio.galacticraft.api.teams.Teams;
import net.minecraft.class_5269;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin({ClientWorld.class})
@Implements(@Interface(iface = ClientWorldTeamsGetter.class, prefix = "cwtg$"))
public abstract class ClientWorldMixin extends World {

    private Teams spaceRaceTeams;

    protected ClientWorldMixin(class_5269 arg, DimensionType dimensionType, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l) {
        super(arg, dimensionType, supplier, bl, bl2, l);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.spaceRaceTeams = new Teams();
    }

    public Teams cwtg$getSpaceRaceTeams() {
        return this.spaceRaceTeams;
    }
}
