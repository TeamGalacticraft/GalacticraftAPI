package com.hrznstudio.galacticraft.api.rocket.part;

import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RocketPart {
    public static final Codec<RocketPart> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(RocketPart::getId),
            RocketPartType.CODEC.fieldOf("type").forGetter(RocketPart::getType),
            Codec.STRING.fieldOf("name").forGetter(part -> part.getName().getKey()),
            BlockState.CODEC.fieldOf("render_state").forGetter(RocketPart::getRenderState),
            ItemStack.CODEC.fieldOf("render_stack").forGetter(RocketPart::getRenderStack),
            Codec.INT.fieldOf("tier").forGetter(RocketPart::getTier),
            Codec.BOOL.fieldOf("recipe").forGetter(RocketPart::hasRecipe)
    ).apply(i, RocketPart::new));

    private final Identifier id;
    private final TranslatableText name;
    private final RocketPartType type;
    private final BlockState renderState;
    private final ItemStack renderStack;
    private final int tier;
    private final boolean hasRecipe;

    private RocketPart(@NotNull Identifier id, @NotNull RocketPartType type, @NotNull TranslatableText name, @NotNull BlockState renderState, @NotNull ItemStack renderStack, int tier, boolean hasRecipe) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.renderState = renderState;
        this.renderStack = renderStack;
        this.tier = tier;
        this.hasRecipe = hasRecipe;
    }

    private RocketPart(@NotNull Identifier id, @NotNull RocketPartType type, @NotNull String name, @NotNull BlockState renderState, @NotNull ItemStack renderStack, int tier, boolean hasRecipe) {
        this(id, type, new TranslatableText(name), renderState, renderStack, tier, hasRecipe);
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

    public CompoundTag toTag(CompoundTag tag) {
        tag.putString(this.getType().asString(), AddonRegistry.ROCKET_PARTS.getId(this).toString());
        return tag;
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

        public RocketPart build() {
            if (id == null || name == null || partType == null || renderState == null) {
                throw new RuntimeException("Tried to build incomplete RocketPart!");
            }
            return new RocketPart(id, partType, name, renderState, renderItem == null ? new ItemStack(renderState.getBlock().asItem()) : renderItem, tier, hasRecipe);
        }
    }
}
