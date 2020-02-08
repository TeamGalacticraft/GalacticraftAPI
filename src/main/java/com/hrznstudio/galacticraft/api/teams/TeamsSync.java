package com.hrznstudio.galacticraft.api.teams;

import net.minecraft.world.PersistentState;

public class TeamsSync implements Runnable {

    private final PersistentState compound;

    public TeamsSync(PersistentState compound) {
        this.compound = compound;
    }

    @Override
    public void run() {
        this.compound.markDirty();
    }
}
