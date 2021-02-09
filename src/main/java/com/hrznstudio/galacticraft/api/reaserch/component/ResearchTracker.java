package com.hrznstudio.galacticraft.api.reaserch.component;

import com.hrznstudio.galacticraft.api.reaserch.ResearchNode;
import com.hrznstudio.galacticraft.api.reaserch.progress.ResearchNodeProgress;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResearchTracker implements Component {
    private final PlayerEntity player;
    private final Map<ResearchNode, ResearchNodeProgress> tracking = new HashMap<>();
    private final Set<ResearchNode> completed = new HashSet<>();
    private final Set<ResearchNode> unclaimed = new HashSet<>();

    private ResearchTracker(PlayerEntity player) {
        this.player = player;
    }

    public Map<ResearchNode, ResearchNodeProgress> getTracking() {
        return tracking;
    }

    public Set<ResearchNode> getCompleted() {
        return completed;
    }

    public Set<ResearchNode> getUnclaimed() {
        return unclaimed;
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        Registry<ResearchNode> registry = player.getEntityWorld().getRegistryManager().get(AddonRegistry.RESEARCH_NODE_KEY);
        ListTag trackedNodes = compoundTag.getList("tracking", NbtType.STRING);
        for (int i = 0; i < trackedNodes.size(); i++) {
            tracking.put(registry.get(new Identifier(trackedNodes.getString(i))), new ResearchNodeProgress()); //todo tracking
        }
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {

    }

    public static ResearchTracker create(PlayerEntity player) {
        return new ResearchTracker(player);
    }
}
