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

import com.google.common.collect.ImmutableMap;
import dev.galacticraft.api.registry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AtmosphericInfo {
    public static final Codec<AtmosphericInfo> CODEC = RecordCodecBuilder.create(atmosphericInfoInstance -> atmosphericInfoInstance.group(
            RecordCodecBuilder.create((RecordCodecBuilder.Instance<Pair<Supplier<AtmosphericGas>, Double>> instance) -> instance.group(AtmosphericGas.REGISTRY_CODEC.fieldOf("gas").forGetter(Pair::getLeft),
                    Codec.DOUBLE.fieldOf("ppm").forGetter(Pair::getRight)).apply(instance, Pair::new)).listOf().fieldOf("composition").forGetter(i -> {
                List<Pair<Supplier<AtmosphericGas>, Double>> list = new LinkedList<>();
                for (Map.Entry<AtmosphericGas, Double> entry : i.composition.entrySet()) {
                    list.add(new Pair<>(entry::getKey, entry.getValue()));
                }
                return list;
            }),
            Codec.DOUBLE.fieldOf("temperature").forGetter(i -> i.temperature),
            Codec.FLOAT.fieldOf("pressure").forGetter(i -> i.pressure)
    ).apply(atmosphericInfoInstance, (pairs, temp, pressure) -> {
        ImmutableMap.Builder<AtmosphericGas, Double> map = ImmutableMap.builder();
        for (Pair<Supplier<AtmosphericGas>, Double> pair : pairs) {
            map.put(pair.getLeft().get(), pair.getRight());
        }
        return new AtmosphericInfo(map.build(), temp, pressure);
    }));

    private final Map<AtmosphericGas, Double> composition;
    private final double temperature;
    private final float pressure;

    /**
     *
     * @param composition make up of the atmosphere (gas, ppm)
     * @param temperature in celsius, used to determine tier of thermal protection
     * @param pressure affects how sounds are heard (1.0f is overworld/earth)
     */
    public AtmosphericInfo(Map<AtmosphericGas, Double> composition, double temperature, float pressure) {
        this.composition = composition;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public Map<AtmosphericGas, Double> getComposition() {
        return composition;
    }

    public double getTemperature() {
        return temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void writePacket(PacketByteBuf buf) {
        buf.writeInt(this.composition.size());
        buf.writeFloat(this.pressure);
        buf.writeDouble(this.temperature);
        for (Map.Entry<AtmosphericGas, Double> entry : this.composition.entrySet()) {;
            buf.writeIdentifier(entry.getKey().getId());
            buf.writeDouble(entry.getValue());
        }
    }

    public static AtmosphericInfo readPacket(DynamicRegistryManager dynamicRegistryManager, PacketByteBuf buf) {
        MutableRegistry<AtmosphericGas> reg = dynamicRegistryManager.get(AddonRegistry.ATMOSPHERIC_GAS_KEY);
        int size = buf.readInt();
        Builder builder = new Builder();
        builder.pressure(buf.readFloat());
        builder.temperature(buf.readDouble());
        for (int i = 0; i < size; i++) {
            builder.gas(reg.get(buf.readIdentifier()), buf.readDouble());
        }
        return builder.build();
    }

    public static class Builder {
        private final Map<AtmosphericGas, Double> composition = new HashMap<>();
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

        public Builder gas(AtmosphericGas gas, double ppm) {
            this.composition.put(gas, ppm);
            return this;
        }

        public AtmosphericInfo build() {
            return new AtmosphericInfo(this.composition, this.temperature, this.pressure);
        }
    }
}
