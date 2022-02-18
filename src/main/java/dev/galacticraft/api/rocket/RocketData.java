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

package dev.galacticraft.api.rocket;

import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.api.rocket.part.*;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;
import dev.galacticraft.api.universe.celestialbody.CelestialBodyType;
import dev.galacticraft.api.universe.celestialbody.landable.Landable;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.rocket.RocketDataImpl;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public interface RocketData {
    Identifier INVALID_ID = new Identifier(Constant.MOD_ID, "invalid");

    @Contract("_, _, _, _, _, _, _ -> new")
    static @NotNull RocketData create(int color, Identifier cone, Identifier body, Identifier fin, Identifier booster, Identifier bottom, Identifier[] upgrade) {
        assert cone != INVALID_ID
                && body != INVALID_ID
                && fin != INVALID_ID
                && booster != INVALID_ID
                && bottom != INVALID_ID;
        return new RocketDataImpl(color, cone, body, fin, booster, bottom, upgrade);
    }

    static @Unmodifiable RocketData fromNbt(NbtCompound nbt) {
        return RocketDataImpl.fromNbt(nbt);
    }

    @Contract(pure = true)
    static @Unmodifiable RocketData empty() {
        return RocketDataImpl.empty();
    }

    NbtCompound toNbt(NbtCompound nbt);

    int color();

    //todo: is this actually correct since bytes are signed?
    default byte red() {
        return (byte) (this.color() >> 16 & 0xFF);
    }

    default byte green() {
        return (byte) (this.color() >> 8 & 0xFF);
    }

    default byte blue() {
        return (byte) (this.color() & 0xFF);
    }

    default byte alpha() {
        return (byte) (this.color() >> 24 & 0xFF);
    }

    default int upgradeCount() {
        return this.upgrades().length;
    }

    Identifier cone();

    Identifier body();

    Identifier fin();

    Identifier booster();

    Identifier bottom();

    Identifier[] upgrades();

    default RocketCone cone(@NotNull DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ROCKET_CONE_KEY).get(this.cone());
    }

    default RocketBody body(@NotNull DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ROCKET_BODY_KEY).get(this.body());
    }

    default RocketFin fin(@NotNull DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ROCKET_FIN_KEY).get(this.fin());
    }

    default RocketBooster booster(@NotNull DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ROCKET_BOOSTER_KEY).get(this.booster());
    }

    default RocketBottom bottom(@NotNull DynamicRegistryManager manager) {
        return manager.get(AddonRegistry.ROCKET_BOTTOM_KEY).get(this.bottom());
    }

    default RocketUpgrade[] upgrades(@NotNull DynamicRegistryManager manager) {
        Registry<RocketUpgrade> registry = manager.get(AddonRegistry.ROCKET_UPGRADE_KEY);
        RocketUpgrade[] upgrades = new RocketUpgrade[this.upgradeCount()];
        Identifier[] upgradeIds = this.upgrades();
        for (int i = 0; i < upgradeIds.length; i++) {
            upgrades[i] = registry.get(upgradeIds[i]);
        }
        return upgrades;
    }

    boolean isEmpty();

    default <FC extends CelestialBodyConfig, FT extends CelestialBodyType<FC> & Landable<FC>, TC extends CelestialBodyConfig, TT extends CelestialBodyType<TC> & Landable<TC>> boolean canTravelTo(DynamicRegistryManager manager, CelestialBody<FC, FT> from, CelestialBody<TC, TT> to) {

    }
}
