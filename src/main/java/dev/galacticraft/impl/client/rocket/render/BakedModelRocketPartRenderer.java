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

package dev.galacticraft.impl.client.rocket.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.galacticraft.api.client.rocket.render.RocketPartRenderer;
import dev.galacticraft.api.entity.Rocket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public record BakedModelRocketPartRenderer(Supplier<BakedModel> model, Supplier<RenderLayer> layer) implements RocketPartRenderer {
    public BakedModelRocketPartRenderer(Supplier<BakedModel> model) {
        this(model, () -> RenderLayer.getEntityTranslucent(model.get().getParticleSprite().getId(), true));
    }

    @Override
    public void renderGUI(ClientWorld world, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        model.get().getTransformation().getTransformation(ModelTransformation.Mode.GUI).apply(false, matrices);
        matrices.translate(0, 0, 250);
        matrices.translate(8, 8, 8);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(35));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        matrices.scale(10, 10, 10);

        MatrixStack.Entry entry = matrices.peek();
        List<BakedQuad> quads = this.model.get().getQuads(null, null, world.random);
        this.model.get().getParticleSprite().getAtlas().bindTexture();
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, this.model.get().getParticleSprite().getAtlas().getGlId());
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        DiffuseLighting.enableGuiDepthLighting();

        if (!quads.isEmpty()) {
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
            for (BakedQuad quad : quads) {
                buffer.quad(
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
        RenderSystem.setShaderColor((((rocket.getColor() >> 16) & 0xFF) / 255f), (((rocket.getColor() >> 8) & 0xFF) / 255f), ((rocket.getColor() & 0xFF) / 255f), (((rocket.getColor() >> 24) & 0xFF) / 255f));
        matrices.translate(0.5D, 0.5D, 0.5D);
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertices.getBuffer(layer.get());
        Direction[] values = Direction.values();
        int i = 0;
        Direction direction = null;
        do {
            for (BakedQuad quad : this.model.get().getQuads(null, direction, world.random)) {
                vertexConsumer.quad(
                        entry,
                        quad,
                        1,
                        1,
                        1,
                        light,
                        //15728880,
                        OverlayTexture.DEFAULT_UV
                );
            }
            direction = values[i++];
        } while (i < values.length);
    }
}
