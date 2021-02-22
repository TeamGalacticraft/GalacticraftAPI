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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class RocketPartRendererRegistry {
    @SuppressWarnings("rawtypes") @ApiStatus.Internal private static final Map<RocketPart, Renderer> RENDERERS = new HashMap<>();
    public static final Renderer<? extends RocketEntity> DEFAULT = (stack, part, entity, vertexConsumers, light, tickDelta) -> MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(stack.peek(), vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout()), part.getRenderState(), MinecraftClient.getInstance().getBlockRenderManager().getModel(part.getRenderState()), entity.getColor()[0], entity.getColor()[1], entity.getColor()[2], light, OverlayTexture.DEFAULT_UV);

    private RocketPartRendererRegistry() {}

    public static <T extends Entity & RocketEntity> void register(RocketPart part, Renderer<T> renderer) {
        RENDERERS.put(part, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity & RocketEntity> void render(MatrixStack stack, RocketPart part, T entity, VertexConsumerProvider vertexConsumers, int light, float tickDelta) {
        RENDERERS.getOrDefault(part, DEFAULT).render(stack, part, entity, vertexConsumers, light, tickDelta);
    }

    @FunctionalInterface
    public interface Renderer<T extends Entity & RocketEntity> {
        void render(MatrixStack stack, RocketPart part, T entity, VertexConsumerProvider vertexConsumers, int light, float tickDelta);
    }
}
