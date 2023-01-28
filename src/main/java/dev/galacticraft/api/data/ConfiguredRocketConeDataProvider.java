/*
 * Copyright (c) 2019-2023 Team Galacticraft
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

import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import dev.galacticraft.api.rocket.part.RocketCone;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ConfiguredRocketConeDataProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<ResourceLocation, RocketCone<?, ?>> galaxies = new LinkedHashMap<>();
    private final PackOutput.PathProvider pathProvider;

    public ConfiguredRocketConeDataProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "rocket_cone");
        this.generateGalaxies();
    }

    public abstract void generateGalaxies();

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {
        return CompletableFuture.allOf(this.galaxies.entrySet().stream().map(e ->
                        DataProvider.saveStable(
                                cachedOutput,
                                RocketCone.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, e.getValue())
                                        .getOrThrow(false, LOGGER::error),
                                pathProvider.json(e.getKey())))
                .toArray(CompletableFuture[]::new)
        );
    }

    public void add(@NotNull ResourceKey<RocketCone<?, ?>> key, @NotNull RocketCone<?, ?> rocketCone) {
        this.galaxies.put(key.location(), rocketCone);
    }
}
