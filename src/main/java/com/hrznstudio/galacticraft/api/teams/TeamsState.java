package com.hrznstudio.galacticraft.api.teams;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

public class TeamsState extends PersistentState {

    private Teams teams;
    private CompoundTag tag;

    public TeamsState() {
        super("gcr-teams");
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
        if (this.tag != null) {
            this.fromTag(tag);
        }
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.teams = TeamsTagUtil.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return TeamsTagUtil.toTag(tag, this.teams);
    }
}
