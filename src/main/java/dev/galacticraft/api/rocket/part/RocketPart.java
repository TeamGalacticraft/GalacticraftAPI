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

package dev.galacticraft.api.rocket.part;

import dev.galacticraft.api.internal.accessor.ResearchAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RocketPart {
    public static final Codec<RocketPart> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(RocketPart::getId),
            Codec.STRING.fieldOf("name").forGetter(part -> part.getName().getKey()),
            Codec.INT.fieldOf("tier").forGetter(RocketPart::getTier),
            RocketPartType.CODEC.fieldOf("type").forGetter(RocketPart::getType),
            Codec.BOOL.fieldOf("recipe").forGetter(RocketPart::hasRecipe),
            Identifier.CODEC.optionalFieldOf("research").forGetter(RocketPart::getResearch)
    ).apply(i, RocketPart::new));

    private final Identifier id;
    private final TranslatableText name;
    private final int tier;
    private final RocketPartType type;
    private final boolean hasRecipe;
    private final Optional<Identifier> research;

    private RocketPart(@NotNull Identifier id, @NotNull TranslatableText name, @NotNull RocketPartType type, int tier, boolean hasRecipe, Optional<Identifier> research) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.tier = tier;
        this.hasRecipe = hasRecipe;
        this.research = research;
    }

    private RocketPart(@NotNull Identifier id, @NotNull String name, int tier, @NotNull RocketPartType type, boolean hasRecipe, Optional<Identifier> research) {
        this(id, new TranslatableText(name), type, tier, hasRecipe, research);
    }

    public Identifier getId() {
        return id;
    }

    public TranslatableText getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public RocketPartType getType() {
        return type;
    }

    public Optional<Identifier> getResearch() {
        return this.research;
    }

    public boolean isUnlocked(PlayerEntity player) {
        if (!this.getResearch().isPresent()) return true;
        return ((ResearchAccessor) player).hasUnlocked_gcr(this.getResearch().get());
    }

    public boolean hasRecipe() {
        return hasRecipe;
    }

    public static class Builder {
        private Identifier id;
        private TranslatableText name;
        private RocketPartType partType;
        private int tier = 0;
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

        public Builder tier(int tier) {
            this.tier = tier;
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
            return new RocketPart(id, name, partType, tier, hasRecipe, Optional.ofNullable(research));
        }
    }
}
