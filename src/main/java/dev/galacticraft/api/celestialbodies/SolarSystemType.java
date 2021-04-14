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

package dev.galacticraft.api.celestialbodies;

import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import dev.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryElementCodec;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.Objects;
import java.util.function.Supplier;

public class SolarSystemType {
    public static final Codec<SolarSystemType> CODEC = RecordCodecBuilder.create(solarSystemTypeInstance -> solarSystemTypeInstance
            .group(Identifier.CODEC.fieldOf("id").forGetter(i -> i.id),
                    Codec.FLOAT.fieldOf("x").forGetter(i -> i.x),
                    Codec.FLOAT.fieldOf("y").forGetter(i -> i.y),
                    Codec.STRING.fieldOf("translation_key").forGetter(i -> i.translationKey),
                    Codec.STRING.fieldOf("galaxy_translation_key").forGetter(i -> i.galaxyTranslationKey)
            ).apply(solarSystemTypeInstance, SolarSystemType::new));

    public static final Codec<Supplier<SolarSystemType>> REGISTRY_CODEC = RegistryElementCodec.of(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY, SolarSystemType.CODEC);

    public static final SolarSystemType SOL = new SolarSystemType.Builder(new Identifier(GalacticraftAPI.MOD_ID, "sol"))
            .translationKey("galacticraft-api.solar_system.sol")
            .galaxyTranslationKey("galacticraft-api.galaxy.milky_way")
            .build();

    private final Identifier id;
    private final float x;
    private final float y;
    private final String translationKey;
    private final String galaxyTranslationKey;

    private SolarSystemType(Identifier id, float x, float y, String translationKey, String galaxyTranslationKey) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.translationKey = translationKey;
        this.galaxyTranslationKey = galaxyTranslationKey;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getGalaxyTranslationKey() {
        return galaxyTranslationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolarSystemType that = (SolarSystemType) o;
        return Float.compare(that.x, x) == 0 &&
                Float.compare(that.y, y) == 0 &&
                translationKey.equals(that.translationKey) &&
                galaxyTranslationKey.equals(that.galaxyTranslationKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, translationKey, galaxyTranslationKey);
    }

    @Override
    public String toString() {
        return "SolarSystemType{" +
                "x=" + x +
                ", y=" + y +
                ", translationKey='" + translationKey + '\'' +
                ", galaxyTranslationKey='" + galaxyTranslationKey + '\'' +
                '}';
    }

    public Identifier getId() {
        return this.id;
    }

    public static SolarSystemType getById(DynamicRegistryManager registryManager, Identifier id) {
        return registryManager.get(AddonRegistry.SOLAR_SYSTEM_TYPE_KEY).get(id);
    }

    public static class Builder {
        private final Identifier id;
        private float x = 0;
        private float y = 0;
        private String translationKey = null;
        private String galaxyTranslationKey = null;

        public Builder(Identifier id) {
            this.id = id;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        public Builder galaxyTranslationKey(String galaxyTranslationKey) {
            this.galaxyTranslationKey = galaxyTranslationKey;
            return this;
        }

        public SolarSystemType build() {
            if (translationKey == null || galaxyTranslationKey == null) throw new RuntimeException("Tried to build solar system without name!");
            return new SolarSystemType(id, x, y, translationKey, galaxyTranslationKey);
        }

        @Override
        public String toString() {
            return "SolarSystemTypeBuilder{" +
                    "x=" + x +
                    ", y=" + y +
                    ", translationKey='" + translationKey + '\'' +
                    ", galaxyTranslationKey='" + galaxyTranslationKey + '\'' +
                    '}';
        }
    }
}
