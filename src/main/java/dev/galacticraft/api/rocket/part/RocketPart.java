/*
 * Copyright (c) 2019-2021 Team Galacticraft
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

package dev.galacticraft.api.rocket.part;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.internal.accessor.ResearchAccessor;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.rocket.part.travel.ConfiguredTravelPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class RocketPart {
    public static final Codec<RocketPart> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(RocketPart::getId),
            Codec.STRING.fieldOf("name").forGetter(part -> part.getName().getKey()),
            ConfiguredTravelPredicate.REGISTRY_CODEC.fieldOf("travel_predicate").xmap(Supplier::get, predicate -> () -> predicate).forGetter(rocketPart -> (ConfiguredTravelPredicate)rocketPart.getTravelPredicate()),
            RocketPartType.CODEC.fieldOf("type").forGetter(RocketPart::getType),
            Codec.BOOL.fieldOf("recipe").forGetter(RocketPart::hasRecipe),
            Identifier.CODEC.optionalFieldOf("research").forGetter(part -> Optional.ofNullable(part.getResearch()))
    ).apply(i, RocketPart::new));

    public static final RocketPart INVALID = Builder.create(new Identifier(GalacticraftAPI.MOD_ID, "invalid"))
            .name(new TranslatableText("tooltip.galacticraft-api.something_went_wrong"))
            .type(RocketPartType.UPGRADE)
            .travelPredicate(ConfiguredTravelPredicate.NEVER)
            .research(new Identifier(GalacticraftAPI.MOD_ID, "unobtainable"))
            .recipe(false)
            .build();

    private final Identifier id;
    private final TranslatableText name;
    private final ConfiguredTravelPredicate<?> travelPredicate;
    private final RocketPartType type;
    private final boolean hasRecipe;
    private final Identifier research;

    private RocketPart(@NotNull Identifier id, @NotNull TranslatableText name, @NotNull RocketPartType type, ConfiguredTravelPredicate<?> travelPredicate, boolean hasRecipe, Identifier research) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.travelPredicate = travelPredicate;
        this.hasRecipe = hasRecipe;
        this.research = research;
    }

    private RocketPart(@NotNull Identifier id, @NotNull String name, ConfiguredTravelPredicate<?> travelPredicate, @NotNull RocketPartType type, boolean hasRecipe, Optional<Identifier> research) {
        this(id, new TranslatableText(name), type, travelPredicate, hasRecipe, research.orElse(null));
    }

    public Identifier getId() {
        return id;
    }

    public TranslatableText getName() {
        return name;
    }

    public ConfiguredTravelPredicate<?> getTravelPredicate() {
        return travelPredicate;
    }

    public RocketPartType getType() {
        return type;
    }

    public Identifier getResearch() {
        return this.research;
    }

    public boolean isUnlocked(PlayerEntity player) {
        if (this.getResearch() == null) return true;
        return ((ResearchAccessor) player).hasUnlocked_gcr(this.getResearch());
    }

    public boolean hasRecipe() {
        return hasRecipe;
    }
    
    public static RocketPart deserialize(DynamicRegistryManager registryManager, Dynamic<?> dynamic) {
        return registryManager.get(AddonRegistry.ROCKET_PART_KEY).get(new Identifier(dynamic.asString("")));
    }

    public static MutableRegistry<RocketPart> getAll(DynamicRegistryManager registryManager) {
        return registryManager.get(AddonRegistry.ROCKET_PART_KEY);
    }

    public static RocketPart getById(DynamicRegistryManager registryManager, Identifier id) {
        return registryManager.get(AddonRegistry.ROCKET_PART_KEY).get(id);
    }

    public static Identifier getId(DynamicRegistryManager registryManager, RocketPart type) {
        return registryManager.get(AddonRegistry.ROCKET_PART_KEY).getId(type);
    }
    
    public static class Builder {
        private Identifier id;
        private TranslatableText name;
        private RocketPartType partType;
        private ConfiguredTravelPredicate<?> travelPredicate = ConfiguredTravelPredicate.NEVER;
        private boolean hasRecipe = true;
        private Identifier research = null;

        public Builder(Identifier id) {
            this.id = id;
        }

        public static Builder create(Identifier id) {
            return new Builder(id);
        }

        public Builder id(Identifier id) {
            this.id = id;
            return this;
        }

        public Builder name(TranslatableText name) {
            this.name = name;
            return this;
        }

        public Builder type(RocketPartType type) {
            this.partType = type;
            return this;
        }

        public Builder recipe(boolean hasRecipe) {
            this.hasRecipe = hasRecipe;
            return this;
        }

        public Builder travelPredicate(ConfiguredTravelPredicate<?> travelPredicate) {
            this.travelPredicate = travelPredicate;
            return this;
        }

        public Builder research(Identifier research) {
            this.research = research;
            return this;
        }

        public RocketPart build() {
            if (id == null || name == null || partType == null) {
                throw new RuntimeException("Tried to build incomplete RocketPart!");
            }
            return new RocketPart(id, name, partType, travelPredicate, hasRecipe, research);
        }
    }
}
