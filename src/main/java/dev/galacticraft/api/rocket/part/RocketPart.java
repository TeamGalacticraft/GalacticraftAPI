/*
 * Copyright (c) 2019-2022 Team Galacticraft
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
import dev.galacticraft.api.accessor.ResearchAccessor;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.rocket.travelpredicate.ConfiguredTravelPredicate;
import dev.galacticraft.api.rocket.travelpredicate.TravelPredicateType;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.rocket.travelpredicate.config.AccessTypeTravelPredicateConfig;
import dev.galacticraft.impl.rocket.travelpredicate.type.ConstantTravelPredicateType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RocketPart(TranslatableText name, RocketPartType type, ConfiguredTravelPredicate<?> travelPredicate,
                         boolean hasRecipe, Identifier research) {
    public static final Codec<RocketPart> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").xmap(TranslatableText::new, TranslatableText::getKey).forGetter(RocketPart::name),
            RocketPartType.CODEC.fieldOf("type").forGetter(RocketPart::type),
            ConfiguredTravelPredicate.CODEC.fieldOf("travel_predicate").forGetter(RocketPart::travelPredicate),
            Codec.BOOL.fieldOf("recipe").forGetter(RocketPart::hasRecipe),
            Identifier.CODEC.optionalFieldOf("research").xmap(o -> o.orElse(null), Optional::ofNullable).forGetter(RocketPart::research)
    ).apply(instance, RocketPart::new));

    public static final RocketPart INVALID = Builder.create()
            .name(new TranslatableText("tooltip.galacticraft-api.something_went_wrong"))
            .type(RocketPartType.UPGRADE)
            .travelPredicate(ConstantTravelPredicateType.INSTANCE.configure(new AccessTypeTravelPredicateConfig(TravelPredicateType.AccessType.BLOCK)))
            .research(new Identifier(Constant.MOD_ID, "unobtainable"))
            .recipe(false)
            .build();

    public RocketPart(@NotNull TranslatableText name, @NotNull RocketPartType type, ConfiguredTravelPredicate<?> travelPredicate, boolean hasRecipe, Identifier research) {
        this.type = type;
        this.name = name;
        this.travelPredicate = travelPredicate;
        this.hasRecipe = hasRecipe;
        this.research = research;
    }

    public static RocketPart deserialize(DynamicRegistryManager manager, Dynamic<?> dynamic) {
        return manager.get(AddonRegistry.ROCKET_PART_KEY).get(new Identifier(dynamic.asString("")));
    }

    public static Registry<RocketPart> getRegistry(DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ROCKET_PART_KEY);
    }

    public static RocketPart getById(DynamicRegistryManager manager, Identifier id) {
        return getById(getRegistry(manager), id);
    }

    public static Identifier getId(DynamicRegistryManager manager, RocketPart rocketPart) {
        return getId(getRegistry(manager), rocketPart);
    }

    public static RocketPart getById(Registry<RocketPart> registry, Identifier id) {
        return registry.get(id);
    }

    public static Identifier getId(Registry<RocketPart> registry, RocketPart rocketPart) {
        return registry.getId(rocketPart);
    }

    public boolean isUnlocked(PlayerEntity player) {
        if (this.research() == null) return true;
        return ((ResearchAccessor) player).hasUnlockedResearch(this.research());
    }

    public static class Builder {
        private TranslatableText name;
        private RocketPartType partType;
        private ConfiguredTravelPredicate<?> travelPredicate = ConstantTravelPredicateType.INSTANCE.configure(new AccessTypeTravelPredicateConfig(TravelPredicateType.AccessType.PASS));
        private boolean hasRecipe = true;
        private Identifier research = null;

        private Builder() {}

        public static Builder create() {
            return new Builder();
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
            if (name == null || partType == null) {
                throw new RuntimeException("Tried to build incomplete RocketPart!");
            }
            return new RocketPart(name, partType, travelPredicate, hasRecipe, research);
        }
    }
}
