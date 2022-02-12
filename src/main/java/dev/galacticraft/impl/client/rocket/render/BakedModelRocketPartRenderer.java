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

package dev.galacticraft.impl.client.rocket.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.galacticraft.api.client.rocket.render.RocketPartRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public record BakedModelRocketPartRenderer(Supplier<BakedModel> model,
                                           Supplier<RenderLayer> layer) implements RocketPartRenderer {
    private static final Direction[] DIRECTIONS_AND_NULL = new Direction[]{null, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    public BakedModelRocketPartRenderer(Supplier<BakedModel> model) {
        this(model, () -> RenderLayer.getEntityTranslucent(model.get().getParticleSprite().getId(), true));
    }

    @Override
    public void renderGUI(ClientWorld world, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        matrices.translate(0, 0, 150);
        matrices.translate(8, 8, 8);
        model.get().getTransformation().getTransformation(ModelTransformation.Mode.GUI).apply(false, matrices);
        matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(35));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(225));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        matrices.scale(16, 16, 16);

        MatrixStack.Entry entry = matrices.peek();
        List<BakedQuad> quads = new LinkedList<>();
        for (Direction direction : DIRECTIONS_AND_NULL) {
            quads.addAll(this.model.get().getQuads(null, direction, world.random));
        }
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, this.model.get().getParticleSprite().getAtlas().getGlId());
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (model.get().isSideLit()) {
            DiffuseLighting.enableGuiDepthLighting();
        } else {
            DiffuseLighting.disableGuiDepthLighting();
        }

        if (!quads.isEmpty()) {
            VertexConsumerProvider.Immediate entityVertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            VertexConsumer itemGlintConsumer = entityVertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());
            for (BakedQuad quad : quads) {
                itemGlintConsumer.quad(
                        entry,
                        quad,
                        1,
                        1,
                        1,
                        15728880,
                        OverlayTexture.DEFAULT_UV
                );
            }
            entityVertexConsumers.draw();
        }

        if (model.get().isSideLit()) {
            DiffuseLighting.enableGuiDepthLighting();
        }
    }

    @Override
    public void render(ClientWorld world, MatrixStack matrices, Rocket rocket, VertexConsumerProvider vertices, float delta, int light) {
        RenderSystem.setShaderColor((((rocket.getColor() >> 16) & 0xFF) / 255f), (((rocket.getColor() >> 8) & 0xFF) / 255f), ((rocket.getColor() & 0xFF) / 255f), (((rocket.getColor() >> 24) & 0xFF) / 255f));
        matrices.translate(0.5D, 0.5D, 0.5D);
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertices.getBuffer(layer.get());
        for (Direction direction : DIRECTIONS_AND_NULL) {
            for (BakedQuad quad : this.model.get().getQuads(null, direction, world.random)) {
                vertexConsumer.quad(
                        entry,
                        quad,
                        1,
                        1,
                        1,
                        light,
                        OverlayTexture.DEFAULT_UV
                );
            }
        }
    }
}
