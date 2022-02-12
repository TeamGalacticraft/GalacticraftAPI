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

package dev.galacticraft.api.rocket.part;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum RocketPartType implements StringIdentifiable {
    CONE(RocketCone.class),
    BODY(RocketBody.class),
    FIN(RocketFin.class),
    BOOSTER(RocketBooster.class),
    BOTTOM(RocketBottom.class),
    UPGRADE(RocketUpgrade.class);

    public static final Codec<RocketPartType> CODEC = Codec.STRING.xmap(s -> RocketPartType.valueOf(s.toUpperCase(Locale.ROOT)), RocketPartType::asString);
    private final Class<? extends RocketPart> clazz;

    RocketPartType(Class<? extends RocketPart> clazz) {
        this.clazz = clazz;
    }

    public boolean isOfType(RocketPart part) {
        return clazz.isInstance(part);
    }

    @Override
    public String asString() {
        return this.toString().toLowerCase(Locale.ROOT);
    }
}