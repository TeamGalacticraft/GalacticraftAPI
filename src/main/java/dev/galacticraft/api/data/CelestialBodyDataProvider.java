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

package dev.galacticraft.api.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public abstract class CelestialBodyDataProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<ResourceLocation, CelestialBody<?, ?>> celestialBodies = Maps.newLinkedHashMap();
    protected final DataGenerator.PathProvider pathProvider;

    public CelestialBodyDataProvider(DataGenerator dataGenerator) {
        this.pathProvider = dataGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "celestial_body");
    }

    @Override
    public void run(CachedOutput cachedOutput) {
        celestialBodies.clear();
        generateCelestialBodies();
        celestialBodies.forEach((key, celestialBody) -> {
            JsonElement element = CelestialBody.CODEC.encodeStart(JsonOps.INSTANCE, celestialBody).getOrThrow(false, LOGGER::error);
            Path path = pathProvider.json(key);

            try {
                DataProvider.saveStable(cachedOutput, element, path);
            } catch (IOException var9) {
                LOGGER.error("Couldn't save celestial body to {}", path, var9);
            }
        });
    }

    public void celestialBody(ResourceKey<CelestialBody<?, ?>> key, CelestialBody<?, ?> celestialBody) {
        celestialBody(key.location(), celestialBody);
    }

    public void celestialBody(ResourceLocation key, CelestialBody<?, ?> celestialBody) {
        celestialBodies.put(key, celestialBody);
    }

    public abstract void generateCelestialBodies();
}
