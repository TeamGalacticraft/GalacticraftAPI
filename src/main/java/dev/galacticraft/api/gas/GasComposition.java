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

package dev.galacticraft.api.gas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.impl.codec.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

public record GasComposition(Object2DoubleMap<RegistryKey<Gas>> composition, double temperature, float pressure) {
    private static final MapCodec<RegistryKey<Gas>, Double, Object2DoubleMap<RegistryKey<Gas>>> MAP_CODEC = MapCodec.create(Object2DoubleArrayMap::new, Identifier.CODEC.xmap(id -> RegistryKey.of(AddonRegistry.GAS_KEY, id), RegistryKey::getValue), Codec.DOUBLE);
    public static final Codec<GasComposition> CODEC = RecordCodecBuilder.create(atmosphericInfoInstance -> atmosphericInfoInstance.group(
            MAP_CODEC.fieldOf("composition").forGetter(GasComposition::composition),
            Codec.DOUBLE.fieldOf("temperature").forGetter(GasComposition::temperature),
            Codec.FLOAT.fieldOf("pressure").forGetter(GasComposition::pressure)
    ).apply(atmosphericInfoInstance, GasComposition::new));

    public static GasComposition readPacket(PacketByteBuf buf) {
        int size = buf.readInt();
        Builder builder = new Builder();
        builder.pressure(buf.readFloat());
        builder.temperature(buf.readDouble());
        for (int i = 0; i < size; i++) {
            builder.gas(RegistryKey.of(AddonRegistry.GAS_KEY, buf.readIdentifier()), buf.readDouble());
        }
        return builder.build();
    }

    public boolean breathable() {
        double oxygen = this.composition().getOrDefault(Gas.OXYGEN_KEY, 0.0);
        return oxygen > 195000.0 && oxygen < 235000.0; //195000ppm to 235000ppm (19.5% to 23.5%)
    }

    public void writePacket(PacketByteBuf buf) {
        buf.writeInt(this.composition.size());
        buf.writeFloat(this.pressure);
        buf.writeDouble(this.temperature);
        for (Object2DoubleMap.Entry<RegistryKey<Gas>> entry : this.composition.object2DoubleEntrySet()) {
            buf.writeIdentifier(entry.getKey().getValue());
            buf.writeDouble(entry.getDoubleValue());
        }
    }

    public static class Builder {
        private final Object2DoubleMap<RegistryKey<Gas>> composition = new Object2DoubleArrayMap<>();
        private double temperature = 15.0;
        private float pressure = 1.0f;

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder pressure(float pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder gas(RegistryKey<Gas> gas, double ppm) {
            this.composition.put(gas, ppm);
            return this;
        }

        public Builder gas(Identifier gas, double ppm) {
            return this.gas(RegistryKey.of(AddonRegistry.GAS_KEY, gas), ppm);
        }

        public GasComposition build() {
            return new GasComposition(this.composition, this.temperature, this.pressure);
        }
    }
}
