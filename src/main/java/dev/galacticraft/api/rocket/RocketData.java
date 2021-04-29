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

package dev.galacticraft.api.rocket;

import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.part.RocketPartType;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.Arrays;
import java.util.Objects;

public class RocketData {
    public static final RocketData EMPTY = new RocketData(-1, -1, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID);

    private final int tier;
    private final int color; //ARGB
    private final RocketPart[] parts;

    public RocketData(int tier, int color, RocketPart cone, RocketPart body, RocketPart fin, RocketPart booster, RocketPart bottom, RocketPart upgrade) {
        this.tier = tier;
        this.color = color;
        this.parts = new RocketPart[6];

        this.parts[0] = cone;
        this.parts[1] = body;
        this.parts[2] = fin;
        this.parts[3] = booster;
        this.parts[4] = bottom;
        this.parts[5] = upgrade;
    }

    private RocketData(int tier, int color, RocketPart[] parts) {
        this.tier = tier;
        this.color = color;
        this.parts = parts;
    }

    public static RocketData fromTag(CompoundTag tag, DynamicRegistryManager registryManager) {
        if (tag.getBoolean("Empty")) return EMPTY;
        RocketPart[] parts = new RocketPart[6];
        ListTag list = tag.getList("Parts", NbtType.STRING);
        for (int i = 0; i < 6; i++) {
            parts[i] = RocketPart.getById(registryManager, new Identifier(list.get(i).asString()));
        }
        return new RocketData(tag.getInt("Tier"), tag.getInt("Color"), parts);
    }

    public CompoundTag toTag(DynamicRegistryManager registryManager, CompoundTag tag) {
        if (this.isEmpty()) {
            tag.putBoolean("Empty", true);
            return tag;
        }
        tag.putInt("Tier", this.getTier());
        tag.putInt("Color", this.getColor());
        ListTag tag1 = new ListTag();
        for (RocketPart part : this.getParts()) {
            tag1.add(StringTag.of(RocketPart.getId(registryManager, part).toString()));
        }
        tag.put("Parts", tag1);
        return tag;
    }

    public int getTier() {
        return this.tier;
    }

    public int getColor() {
        return this.color;
    }

    public int getRed() {
        return this.getColor() >> 16 & 0xFF;
    }

    public int getGreen() {
        return this.getColor() >> 8 & 0xFF;
    }

    public int getBlue() {
        return this.getColor() & 0xFF;
    }

    public int getAlpha() {
        return this.getColor() >> 24 & 0xFF;
    }

    public RocketPart getCone() {
        return this.parts[0];
    }

    public RocketPart getBody() {
        return this.parts[1];
    }

    public RocketPart getFin() {
        return this.parts[2];
    }

    public RocketPart getBooster() {
        return this.parts[3];
    }

    public RocketPart getBottom() {
        return this.parts[4];
    }

    public RocketPart getUpgrade() {
        return this.parts[5];
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketData that = (RocketData) o;
        return getTier() == that.getTier() && getColor() == that.getColor() && Arrays.equals(getParts(), that.getParts());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getTier(), getColor());
        result = 31 * result + Arrays.hashCode(getParts());
        return result;
    }

    public RocketPart getPartForType(RocketPartType type) {
        return this.parts[type.ordinal()];
    }

    public RocketPart[] getParts() {
        return Arrays.copyOf(this.parts, this.parts.length);
    }
}
