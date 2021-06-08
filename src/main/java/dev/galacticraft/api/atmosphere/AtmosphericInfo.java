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

package dev.galacticraft.api.atmosphere;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.impl.internal.codec.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

public record AtmosphericInfo(Object2DoubleMap<RegistryKey<AtmosphericGas>> composition, double temperature, float pressure) {
    private static final MapCodec<RegistryKey<AtmosphericGas>, Double, Object2DoubleMap<RegistryKey<AtmosphericGas>>> MAP_CODEC = MapCodec.create(Object2DoubleArrayMap::new, Identifier.CODEC.xmap(id -> RegistryKey.of(AddonRegistry.ATMOSPHERIC_GAS_KEY, id), RegistryKey::getValue), Codec.DOUBLE);
    public static final Codec<AtmosphericInfo> CODEC = RecordCodecBuilder.create(atmosphericInfoInstance -> atmosphericInfoInstance.group(
            MAP_CODEC.fieldOf("composition").forGetter(AtmosphericInfo::composition),
            Codec.DOUBLE.fieldOf("temperature").forGetter(AtmosphericInfo::temperature),
            Codec.FLOAT.fieldOf("pressure").forGetter(AtmosphericInfo::pressure)
    ).apply(atmosphericInfoInstance, AtmosphericInfo::new));

    public boolean breathable() {
        return this.composition().getOrDefault(AtmosphericGas.OXYGEN_KEY, 0.0) > 1000.0; //todo: get joe to do actual numbers?
    }

    public void writePacket(PacketByteBuf buf) {
        buf.writeInt(this.composition.size());
        buf.writeFloat(this.pressure);
        buf.writeDouble(this.temperature);
        for (Object2DoubleMap.Entry<RegistryKey<AtmosphericGas>> entry : this.composition.object2DoubleEntrySet()) {
            buf.writeIdentifier(entry.getKey().getValue());
            buf.writeDouble(entry.getDoubleValue());
        }
    }

    public static AtmosphericInfo readPacket(PacketByteBuf buf) {
        int size = buf.readInt();
        Builder builder = new Builder();
        builder.pressure(buf.readFloat());
        builder.temperature(buf.readDouble());
        for (int i = 0; i < size; i++) {
            builder.gas(RegistryKey.of(AddonRegistry.ATMOSPHERIC_GAS_KEY, buf.readIdentifier()), buf.readDouble());
        }
        return builder.build();
    }

    public static class Builder {
        private final Object2DoubleMap<RegistryKey<AtmosphericGas>> composition = new Object2DoubleArrayMap<>();
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

        public Builder gas(RegistryKey<AtmosphericGas> gas, double ppm) {
            this.composition.put(gas, ppm);
            return this;
        }

        public Builder gas(Identifier gas, double ppm) {
            return this.gas(RegistryKey.of(AddonRegistry.ATMOSPHERIC_GAS_KEY, gas), ppm);
        }

        public AtmosphericInfo build() {
            return new AtmosphericInfo(this.composition, this.temperature, this.pressure);
        }
    }
}
