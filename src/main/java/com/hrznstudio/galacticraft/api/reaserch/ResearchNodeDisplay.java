package com.hrznstudio.galacticraft.api.reaserch;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

public class ResearchNodeDisplay {
    public static final Codec<ResearchNodeDisplay> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("x").forGetter(ResearchNodeDisplay::getX),
            Codec.INT.fieldOf("y").forGetter(ResearchNodeDisplay::getY),
            ItemStack.CODEC.fieldOf("item").forGetter(ResearchNodeDisplay::getItem)
    ).apply(i, ResearchNodeDisplay::new));
    public static final ResearchNodeDisplay DEFAULT = Builder.create().build();

    private final int x;
    private final int y;
    private final ItemStack item;

    private ResearchNodeDisplay(int x, int y, ItemStack item) {
        this.x = x;
        this.y = y;
        this.item = item;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ItemStack getItem() {
        return item;
    }

    public static final class Builder {
        private int x = 0;
        private int y = 0;
        private ItemStack item = ItemStack.EMPTY;

        public Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder item(ItemStack item) {
            this.item = item;
            return this;
        }

        public ResearchNodeDisplay build() {
            assert this.item != null;
            return new ResearchNodeDisplay(this.x, this.y, this.item);
        }
    }
}
