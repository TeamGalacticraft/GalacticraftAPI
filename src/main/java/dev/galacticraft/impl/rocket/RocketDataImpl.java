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
import dev.galacticraft.impl.Constant;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public record RocketDataImpl(int color, Identifier cone, Identifier body, Identifier fin, Identifier booster, Identifier bottom, Identifier upgrade) implements RocketData {
    public static final RocketDataImpl EMPTY = new RocketDataImpl(0xffffffff, new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"), new Identifier(Constant.MOD_ID, "invalid"));

    public static RocketDataImpl fromNbt(NbtCompound nbt) {
        if (nbt.getBoolean("Empty")) return empty();
        return new RocketDataImpl(
                nbt.getInt("Color"),
                new Identifier(nbt.getString("Cone")),
                new Identifier(nbt.getString("Body")),
                new Identifier(nbt.getString("Fin")),
                new Identifier(nbt.getString("Booster")),
                new Identifier(nbt.getString("Bottom")),
                new Identifier(nbt.getString("Upgrade"))
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
        nbt.putString("Upgrade", this.upgrade().toString());
        return nbt;
    }

    @Override
    public int red() {
        return this.color() >> 16 & 0xFF;
    }

    @Override
    public int green() {
        return this.color() >> 8 & 0xFF;
    }

    @Override
    public int blue() {
        return this.color() & 0xFF;
    }

    @Override
    public int alpha() {
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
        Registry<RocketPart> registry = RocketPart.getRegistry(manager);
        for (Identifier part : this.parts()) {
            RocketPart rocketPart = RocketPart.getById(registry, part);
            map.put(rocketPart, true);
            type = type.merge(this.travel(manager, rocketPart, celestialBodyType, map));
        }
        return type == TravelPredicateType.AccessType.ALLOW;
    }

    private TravelPredicateType.AccessType travel(DynamicRegistryManager manager, RocketPart part, CelestialBody<?, ?> type, Object2BooleanMap<RocketPart> map) {
        return part.travelPredicate().canTravelTo(type, p -> map.computeBooleanIfAbsent((RocketPart) p, p1 -> {
            if (Arrays.asList(this.parts()).contains(p1)) {
                map.put((RocketPart) p, false);
                return travel(manager, p1, type, map) != TravelPredicateType.AccessType.BLOCK;
            } else {
                return false;
            }
        }));
    }

    @Override
    public Identifier getPartForType(RocketPartType type) {
        return this.parts()[type.ordinal()];
    }

    @Override
    public Identifier[] parts() {
        return new Identifier[]{this.cone(), this.body(), this.fin(), this.booster(), this.bottom(), this.upgrade()};
    }
}
