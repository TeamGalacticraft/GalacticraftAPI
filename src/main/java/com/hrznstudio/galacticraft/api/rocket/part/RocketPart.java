package com.hrznstudio.galacticraft.api.rocket.part;

import com.hrznstudio.galacticraft.api.reaserch.ResearchNode;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import org.jetbrains.annotations.NotNull;

public class RocketPart {
    public static final Codec<RocketPart> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(RocketPart::getId),
            Codec.STRING.fieldOf("name").forGetter(part -> part.getName().getKey()),
            Codec.INT.fieldOf("tier").forGetter(RocketPart::getTier),
            RocketPartType.CODEC.fieldOf("type").forGetter(RocketPart::getType),
            BlockState.CODEC.fieldOf("render_state").forGetter(RocketPart::getRenderState),
            ItemStack.CODEC.fieldOf("render_stack").forGetter(RocketPart::getRenderStack),
            Codec.BOOL.fieldOf("recipe").forGetter(RocketPart::hasRecipe),
            ResearchNode.REGISTRY_CODEC.fieldOf("research").forGetter((RocketPart part) -> part.getResearch()::get)
    ).apply(i, (id1, name1, tier1, type1, renderState1, renderStack1, hasRecipe1, research) -> new RocketPart(id1, name1, tier1, type1, renderState1, renderStack1, hasRecipe1, new Lazy<>(research))));

    private final Identifier id;
    private final TranslatableText name;
    private final int tier;
    private final RocketPartType type;
    private final BlockState renderState;
    private final ItemStack renderStack;
    private final boolean hasRecipe;
    private final Lazy<ResearchNode> research;

    private RocketPart(@NotNull Identifier id, @NotNull TranslatableText name, @NotNull RocketPartType type, int tier, @NotNull BlockState renderState, @NotNull ItemStack renderStack, boolean hasRecipe, Lazy<ResearchNode> research) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.renderState = renderState;
        this.renderStack = renderStack;
        this.tier = tier;
        this.hasRecipe = hasRecipe;
        this.research = research;
    }

    private RocketPart(@NotNull Identifier id, @NotNull String name, int tier, @NotNull RocketPartType type, @NotNull BlockState renderState, @NotNull ItemStack renderStack, boolean hasRecipe, Lazy<ResearchNode> research) {
        this(id, new TranslatableText(name), type, tier, renderState, renderStack, hasRecipe, research);
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

    public BlockState getRenderState() {
        return renderState;
    }

    public ItemStack getRenderStack() {
        return renderStack;
    }

    public Lazy<ResearchNode> getResearch() {
        return research;
    }

    public boolean isUnlocked(PlayerEntity player) {
//        if (research.get() != null) {
//            return true; //todo resarch tracking
//        }
        return true;
    }

    public boolean hasRecipe() {
        return hasRecipe;
    }

    public static class Builder {
        private Identifier id;
        private TranslatableText name;
        private RocketPartType partType;
        private BlockState renderState;
        private ItemStack renderItem;
        private int tier = 0;
        private boolean hasRecipe = true;
        private Lazy<ResearchNode> research = new Lazy<>(() -> null);

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

        public Builder renderState(BlockState renderState) {
            this.renderState = renderState;
            return this;
        }

        public Builder recipe(boolean hasRecipe) {
            this.hasRecipe = hasRecipe;
            return this;
        }

        public Builder renderItem(ItemStack renderItem) {
            this.renderItem = renderItem;
            return this;
        }

        public Builder tier(int tier) {
            this.tier = tier;
            return this;
        }

        public Builder research(Lazy<ResearchNode> research) {
            this.research = research;
            return this;
        }

        public RocketPart build() {
            if (id == null || name == null || partType == null || renderState == null) {
                throw new RuntimeException("Tried to build incomplete RocketPart!");
            }
            return new RocketPart(id, name, partType, tier, renderState, renderItem == null ? new ItemStack(renderState.getBlock().asItem()) : renderItem, hasRecipe, research);
        }
    }
}
