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
