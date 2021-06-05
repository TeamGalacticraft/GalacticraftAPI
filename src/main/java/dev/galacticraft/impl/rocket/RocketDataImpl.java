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

package dev.galacticraft.impl.rocket;

import dev.galacticraft.api.rocket.RocketData;
import dev.galacticraft.api.rocket.part.RocketPart;
import dev.galacticraft.api.rocket.part.RocketPartType;
import dev.galacticraft.api.rocket.travelpredicate.TravelPredicateType;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.Arrays;

public record RocketDataImpl(int color, RocketPart cone, RocketPart body, RocketPart fin, RocketPart booster, RocketPart bottom, RocketPart upgrade) implements RocketData {
    public static final RocketDataImpl EMPTY = new RocketDataImpl(-1, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID, RocketPart.INVALID);

    public static RocketDataImpl fromTag(NbtCompound tag, DynamicRegistryManager manager) {
        if (tag.getBoolean("Empty")) return EMPTY;
        return new RocketDataImpl(
                tag.getInt("Color"),
                RocketPart.getById(manager, new Identifier(tag.getString("Cone"))),
                RocketPart.getById(manager, new Identifier(tag.getString("Body"))),
                RocketPart.getById(manager, new Identifier(tag.getString("Fin"))),
                RocketPart.getById(manager, new Identifier(tag.getString("Booster"))),
                RocketPart.getById(manager, new Identifier(tag.getString("Bottom"))),
                RocketPart.getById(manager, new Identifier(tag.getString("Upgrade")))
        );
    }

    @Override
    public NbtCompound toTag(DynamicRegistryManager manager, NbtCompound tag) {
        if (this.isEmpty()) {
            tag.putBoolean("Empty", true);
            return tag;
        }
        tag.putInt("Color", this.color());
        tag.putString("Cone", RocketPart.getId(manager, this.cone()).toString());
        tag.putString("Body", RocketPart.getId(manager, this.body()).toString());
        tag.putString("Fin", RocketPart.getId(manager, this.fin()).toString());
        tag.putString("Booster", RocketPart.getId(manager, this.booster()).toString());
        tag.putString("Bottom", RocketPart.getId(manager, this.bottom()).toString());
        tag.putString("Upgrade", RocketPart.getId(manager, this.upgrade()).toString());
        return tag;
    }

    @Override
    public int getRed() {
        return this.color() >> 16 & 0xFF;
    }

    @Override
    public int getGreen() {
        return this.color() >> 8 & 0xFF;
    }

    @Override
    public int getBlue() {
        return this.color() & 0xFF;
    }

    @Override
    public int getAlpha() {
        return this.color() >> 24 & 0xFF;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public boolean canTravelTo(DynamicRegistryManager manager, CelestialBody<?, ?> celestialBodyType) {
        Object2BooleanMap<RocketPart> map = new Object2BooleanArrayMap<>();
        TravelPredicateType.AccessType type = TravelPredicateType.AccessType.PASS;
        for (RocketPart part : this.getParts()) {
            map.put(part, true);
            type = type.merge(this.travel(manager, part, celestialBodyType, map));
        }
        return type == TravelPredicateType.AccessType.ALLOW;
    }

    private TravelPredicateType.AccessType travel(DynamicRegistryManager manager, RocketPart part, CelestialBody<?, ?> type, Object2BooleanMap<RocketPart> map) {
        return part.travelPredicate().canTravelTo(type, p -> map.computeBooleanIfAbsent((RocketPart) p, p1 -> {
            if (Arrays.stream(this.getParts()).anyMatch(p2 -> RocketPart.getId(manager, p2).equals(RocketPart.getId(manager, p1)))) {
                map.put((RocketPart) p, false);
                return travel(manager, p1, type, map) != TravelPredicateType.AccessType.BLOCK;
            } else {
                return false;
            }
        }));
    }

    @Override
    public RocketPart getPartForType(RocketPartType type) {
        return this.getParts()[type.ordinal()];
    }

    @Override
    public RocketPart[] getParts() {
        return new RocketPart[]{this.cone(), this.body(), this.fin(), this.booster(), this.bottom(), this.upgrade()};
    }
}
