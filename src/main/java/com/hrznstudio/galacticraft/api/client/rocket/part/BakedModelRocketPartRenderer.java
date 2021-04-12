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
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public class BakedModelRocketPartRenderer implements RocketPartRendererRegistry.RocketPartRenderer {
    private final BakedModel model;
    private final RenderLayer layer;

    public BakedModelRocketPartRenderer(BakedModel model) {
        this.model = model;
        this.layer = RenderLayer.getEntityTranslucent(model.getSprite().getId(), true);
    }

    @Override
    public void renderGUI(ClientWorld world, MatrixStack matrices, VertexConsumerProvider vertices, float delta) {
        model.getTransformation().getTransformation(ModelTransformation.Mode.GUI).apply(false, matrices);
        matrices.translate(-0.5D, -0.5D, -0.5D);

        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertices.getBuffer(TexturedRenderLayers.getItemEntityTranslucentCull());
        for (BakedQuad quad : this.model.getQuads(null, null, world.random)) {
            vertexConsumer.quad(
                    entry,
                    quad,
                    1,
                    1,
                    1,
                    15728880,
                    OverlayTexture.DEFAULT_UV
            );
        }

    }

    @Override
    public void render(ClientWorld world, MatrixStack matrices, RocketEntity rocket, VertexConsumerProvider vertices, float delta, int light) {
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertices.getBuffer(layer);
        for (BakedQuad quad : this.model.getQuads(null, null, world.random)) {
            vertexConsumer.quad(
                    entry,
                    quad,
                    rocket.getColor()[0] * rocket.getColor()[3],
                    rocket.getColor()[1] * rocket.getColor()[3],
                    rocket.getColor()[2] * rocket.getColor()[3],
                    light,
                    //15728880,
                    OverlayTexture.DEFAULT_UV
            );
        }
    }
}