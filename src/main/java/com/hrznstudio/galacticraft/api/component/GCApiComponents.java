package com.hrznstudio.galacticraft.api.component;

import com.hrznstudio.galacticraft.api.reaserch.component.ResearchTracker;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class GCApiComponents implements EntityComponentInitializer {
    public static final ComponentKey<ResearchTracker> RESEARCH_KEY = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("galacticraft-api", "research"), ResearchTracker.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(RESEARCH_KEY, ResearchTracker::create, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
