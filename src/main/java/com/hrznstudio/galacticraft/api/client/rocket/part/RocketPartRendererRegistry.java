/*
 * Copyright (c) 2019-2021 HRZN LTD
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

package com.hrznstudio.galacticraft.api.client.rocket.part;

import com.hrznstudio.galacticraft.api.entity.RocketEntity;
import com.hrznstudio.galacticraft.api.rocket.part.RocketPart;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class RocketPartRendererRegistry {
    @SuppressWarnings("rawtypes") @ApiStatus.Internal private static final Map<RocketPart, RocketPartRenderer> RENDERERS = new HashMap<>();

    private RocketPartRendererRegistry() {}

    public static void register(RocketPart part, RocketPartRenderer renderer) {
        RENDERERS.put(part, renderer);
    }

    public static RocketPartRenderer getRenderer(RocketPart part) {
        return RENDERERS.getOrDefault(part, EmptyRocketPartRenderer.INSTANCE);
    }

    @FunctionalInterface
    public interface RocketPartRenderer {
        default void renderGUI(ClientWorld world, MatrixStack matrices, VertexConsumerProvider vertices, float delta) {
        }

        void render(ClientWorld world, MatrixStack matrices, RocketEntity rocket, VertexConsumerProvider vertices, float delta, int light);
    }

}