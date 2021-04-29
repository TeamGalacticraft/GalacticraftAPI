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

import com.google.common.collect.Lists;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.part.RocketPartType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.List;
import java.util.Objects;

public class RocketData {
    public static final RocketData EMPTY = new RocketData(-1, -1, null, null, null, null, null, null);

    private final int tier;
    private final int color; //ARGB
    private final RocketPart cone;
    private final RocketPart body;
    private final RocketPart fin;
    private final RocketPart booster;
    private final RocketPart bottom;
    private final RocketPart upgrade;

    public RocketData(int tier, int color, RocketPart cone, RocketPart body, RocketPart fin, RocketPart booster, RocketPart bottom, RocketPart upgrade) {
        this.tier = tier;
        this.color = color;
        this.cone = cone;
        this.body = body;
        this.fin = fin;
        this.booster = booster;
        this.bottom = bottom;
        this.upgrade = upgrade;
    }
    
    public static RocketData fromTag(CompoundTag tag, DynamicRegistryManager registryManager) {
        if (tag.contains("tier")
                && tag.contains("color")
                && tag.contains("cone")
                && tag.contains("body") && tag.contains("fin")
                && tag.contains("booster") && tag.contains("bottom")
                && tag.contains("upgrade")) {
            return new RocketData(tag.getInt("tier"), tag.getInt("color"),
                    RocketPart.getById(registryManager, new Identifier(tag.getString("cone"))), RocketPart.getById(registryManager, new Identifier(tag.getString("body"))),
                    RocketPart.getById(registryManager, new Identifier(tag.getString("fin"))), RocketPart.getById(registryManager, new Identifier(tag.getString("booster"))),
                    RocketPart.getById(registryManager, new Identifier(tag.getString("bottom"))), RocketPart.getById(registryManager, new Identifier(tag.getString("upgrade"))));
        } else {
            return EMPTY;
        }
    }

    public CompoundTag toTag(DynamicRegistryManager registryManager) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("empty", isEmpty());
        if (!isEmpty()) {
            tag.putInt("tier", tier);
            tag.putInt("color", color);
            tag.putString("cone", Objects.requireNonNull(RocketPart.getId(registryManager, cone)).toString());
            tag.putString("body", Objects.requireNonNull(RocketPart.getId(registryManager, body)).toString());
            tag.putString("fin", Objects.requireNonNull(RocketPart.getId(registryManager, fin)).toString());
            tag.putString("booster", Objects.requireNonNull(RocketPart.getId(registryManager, booster)).toString());
            tag.putString("bottom", Objects.requireNonNull(RocketPart.getId(registryManager, bottom)).toString());
            tag.putString("upgrade", Objects.requireNonNull(RocketPart.getId(registryManager, upgrade)).toString());
        }
        return tag;
    }

    public int getTier() {
        return tier;
    }

    public int getColor() {
        return color;
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
        return cone;
    }

    public RocketPart getBody() {
        return body;
    }

    public RocketPart getFin() {
        return fin;
    }

    public RocketPart getBooster() {
        return booster;
    }

    public RocketPart getBottom() {
        return bottom;
    }

    public RocketPart getUpgrade() {
        return upgrade;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketData that = (RocketData) o;
        return tier == that.tier &&
                color == that.color &&
                cone == that.cone &&
                body == that.body &&
                fin == that.fin &&
                booster == that.booster &&
                bottom == that.bottom &&
                upgrade == that.upgrade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tier, color, cone, body, fin, booster, bottom, upgrade);
    }

    @Override
    public String toString() {
        return "RocketData{" +
                "tier=" + tier +
                ", color=" + color +
                ", cone=" + cone +
                ", body=" + body +
                ", fin=" + fin +
                ", booster=" + booster +
                ", bottom=" + bottom +
                ", upgrade=" + upgrade +
                '}';
    }

    public RocketPart getPartForType(RocketPartType value) {
        switch (value) {
            case UPGRADE:
                return getUpgrade();
            case FIN:
                return getFin();
            case BODY:
                return getBody();
            case CONE:
                return getCone();
            case BOTTOM:
                return getBottom();
            case BOOSTER:
                return getBooster();
            default:
                throw new IllegalArgumentException();
        }
    }

    public List<RocketPart> getParts() {
        return Lists.newArrayList(cone, body, fin, booster, bottom, upgrade);
    }
}
