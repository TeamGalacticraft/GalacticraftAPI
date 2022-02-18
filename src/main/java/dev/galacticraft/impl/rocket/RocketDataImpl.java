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

package dev.galacticraft.impl.rocket;

import dev.galacticraft.api.rocket.RocketData;
import dev.galacticraft.impl.Constant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record RocketDataImpl(int color, Identifier cone, Identifier body, Identifier fin, Identifier booster,
                             Identifier bottom, Identifier[] upgrades) implements RocketData {
    public static final RocketDataImpl EMPTY = new RocketDataImpl(0xffffffff, new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"));

    public static RocketDataImpl fromNbt(NbtCompound nbt) {
        if (nbt.getBoolean("Empty")) return empty();
        NbtList upgradeList = nbt.getList("Upgrade", NbtElement.STRING_TYPE);
        Identifier[] upgrades = new Identifier[upgradeList.size()];
        for (int i = 0; i < upgradeList.size(); i++) {
            upgrades[i] = new Identifier(upgradeList.getString(i));
        }
        return new RocketDataImpl(
                nbt.getInt("Color"),
                new Identifier(nbt.getString("Cone")),
                new Identifier(nbt.getString("Body")),
                new Identifier(nbt.getString("Fin")),
                new Identifier(nbt.getString("Booster")),
                new Identifier(nbt.getString("Bottom")),
                upgrades
        );
    }

    public static RocketDataImpl empty() {
        return EMPTY;
    }

    @Override
    public NbtCompound toNbt(NbtCompound nbt) {
        if (this.isEmpty()) {
            nbt.putBoolean("Empty", true);
            return nbt;
        }
        nbt.putInt("Color", this.color());
        nbt.putString("Cone", this.cone().toString());
        nbt.putString("Body", this.body().toString());
        nbt.putString("Fin", this.fin().toString());
        nbt.putString("Booster", this.booster().toString());
        nbt.putString("Bottom", this.bottom().toString());
        NbtList list = new NbtList();
        for (Identifier upgrade : this.upgrades) {
            list.add(NbtString.of(upgrade.toString()));
        }
        nbt.put("Upgrades", list);
        return nbt;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }
}
