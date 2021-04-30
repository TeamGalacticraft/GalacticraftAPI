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

package dev.galacticraft.api.rocket.part;

import dev.galacticraft.api.entity.Rocket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Lazy;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BakedModelRocketPartRenderer implements RocketPartRendererRegistry.RocketPartRenderer {
    private final Lazy<BakedModel> model;
    private final Lazy<RenderLayer> layer;

    public BakedModelRocketPartRenderer(Lazy<BakedModel> model, Lazy<RenderLayer> layer) {
        this.model = model;
        this.layer = layer;
    }

    public BakedModelRocketPartRenderer(BakedModel model, RenderLayer layer) {
        this(new Lazy<>(() -> model), new Lazy<>(() -> layer));
    }

    public BakedModelRocketPartRenderer(Lazy<BakedModel> model) {
        this(model, new Lazy<>(() -> RenderLayer.getEntityTranslucent(model.get().getSprite().getId(), true)));
    }

    public BakedModelRocketPartRenderer(BakedModel model) {
        this(new Lazy<>(() -> model));
    }

    @Override
    public void renderGUI(ClientWorld world, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        model.get().getTransformation().getTransformation(ModelTransformation.Mode.GUI).apply(false, matrices);
        matrices.translate(-0.5D, -0.5D, -0.5D);

        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = Tessellator.getInstance().getBuffer();
        List<BakedQuad> quads = this.model.get().getQuads(null, null, world.random);
        if (!quads.isEmpty()) {
            Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
            for (BakedQuad quad : quads) {
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
            Tessellator.getInstance().draw();
        }
    }

    @Override
    public void render(ClientWorld world, MatrixStack matrices, Rocket rocket, VertexConsumerProvider vertices, float delta, int light) {
        matrices.translate(-0.5D, -0.5D, -0.5D);
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertices.getBuffer(layer.get());
        for (BakedQuad quad : this.model.get().getQuads(null, null, world.random)) {
            vertexConsumer.quad(
                    entry,
                    quad,
                    (((rocket.getColor() << 16) & 0xFF) / 255f) * (((rocket.getColor() << 24) & 0xFF) / 255f),
                    (((rocket.getColor() << 8) & 0xFF) / 255f) * (((rocket.getColor() << 24) & 0xFF) / 255f),
                    ((rocket.getColor() & 0xFF) / 255f) * (((rocket.getColor() << 24) & 0xFF) / 255f),
                    light,
                    //15728880,
                    OverlayTexture.DEFAULT_UV
            );
        }
    }
}
