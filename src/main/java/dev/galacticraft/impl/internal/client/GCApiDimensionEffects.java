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

package dev.galacticraft.impl.internal.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.galacticraft.api.universe.display.CelestialDisplay;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.Random;

public final class GCApiDimensionEffects {
    public static final DimensionRenderingRegistry.CloudRenderer NO_CLOUDS = context -> {};
    public static final DimensionRenderingRegistry.WeatherRenderer NO_WEATHER = context -> {};

    public static DimensionRenderingRegistry.SkyRenderer createSpaceStationRenderer(float starSize, float planetSize, CelestialDisplay<?, ?> starDisplay, CelestialDisplay<?, ?> planetDisplay) {
        return new SatelliteSkyRenderer(starSize, planetSize, starDisplay, planetDisplay);
    }

    private static class SatelliteSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {
        private final VertexBuffer starBuffer = new VertexBuffer();
        private final float starSize;
        private final float planetSize;
        private final CelestialDisplay<?, ?> starDisplay;
        private final CelestialDisplay<?, ?> planetDisplay;

        public SatelliteSkyRenderer(float starSize, float planetSize, CelestialDisplay<?, ?> starDisplay, CelestialDisplay<?, ?> planetDisplay) {
            this.starSize = starSize;
            this.planetSize = planetSize;
            this.starDisplay = starDisplay;
            this.planetDisplay = planetDisplay;
            final Random random = new Random(27893L);
            final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionShader);
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
            for (int i = 0; i < 12000; ++i) {
                double j = random.nextFloat() * 2.0F - 1.0F;
                double k = random.nextFloat() * 2.0F - 1.0F;
                double l = random.nextFloat() * 2.0F - 1.0F;
                double m = 0.15F + random.nextFloat() * 0.1F;
                double n = j * j + k * k + l * l;

                if (n < 1.0D && n > 0.01D) {
                    n = 1.0D / Math.sqrt(n);
                    j *= n;
                    k *= n;
                    l *= n;
                    double o = j * 100.0D;
                    double p = k * 100.0D;
                    double q = l * 100.0D;
                    double r = Math.atan2(j, l);
                    double s = Math.sin(r);
                    double t = Math.cos(r);
                    double u = Math.atan2(Math.sqrt(j * j + l * l), k);
                    double v = Math.sin(u);
                    double w = Math.cos(u);
                    double x = random.nextDouble() * Math.PI * 2.0D;
                    double y = Math.sin(x);
                    double z = Math.cos(x);

                    for (int a = 0; a < 4; ++a) {
                        double b = 0.0D;
                        double c = ((a & 2) - 1) * m;
                        double d = ((a + 1 & 2) - 1) * m;
                        double e = c * z - d * y;
                        double f = d * z + c * y;
                        double g = e * v + b * w;
                        double h = b * v - e * w;
                        double aa = h * s - f * t;
                        double ab = f * s + h * t;
                        buffer.vertex((o + aa) * (i > 6000 ? -1 : 1), (p + g) * (i > 6000 ? -1 : 1), (q + ab) * (i > 6000 ? -1 : 1)).next();
                    }
                }
            }
            buffer.end();
            starBuffer.upload(buffer);
        }

        @Override
        public void render(WorldRenderContext context) {
            context.profiler().push("moon_sky_render");
            RenderSystem.disableTexture();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(false);

            final MatrixStack matrices = context.matrixStack();
            final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            float starBrightness = this.getStarBrightness(context.world(), context.tickDelta());

            context.profiler().push("stars");
            matrices.push();
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(context.world().getSkyAngle(context.tickDelta()) * 360.0f));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-19.0F));
            RenderSystem.setShaderColor(1.0F, 0.95F, 0.9F, starBrightness);
            RenderSystem.disableTexture();
            this.starBuffer.setShader(matrices.peek().getPositionMatrix(), context.projectionMatrix(), GameRenderer.getPositionShader());
            this.starBuffer.bind();
            this.starBuffer.drawVertices();
            VertexBuffer.unbind();

            matrices.pop();
            context.profiler().pop();

            context.profiler().push("sun");
            matrices.push();

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(context.world().getSkyAngle(context.tickDelta()) * 360.0f));
            matrices.translate(0.0, -100.0, 0.0);
            RenderSystem.enableTexture();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            starDisplay.render(matrices, buffer, starSize, Float.MAX_VALUE / 3, Float.MAX_VALUE / 3, context.tickDelta(), RenderSystem::setShader);

            matrices.pop();
            context.profiler().swap("planet");
            matrices.push();

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((context.world().getSkyAngle(context.tickDelta()) * 360.0F) * 0.001F + ((float) (context.world().getSpawnPos().getZ() - MinecraftClient.getInstance().player.getZ()) * 0.01F) + 200.0F));
            matrices.scale(0.6F, 0.6F, 0.6F);
            planetDisplay.render(matrices, buffer, planetSize, Float.MAX_VALUE / 3, Float.MAX_VALUE / 3, context.tickDelta(), RenderSystem::setShader);

            context.profiler().pop();
            matrices.pop();

            RenderSystem.disableTexture();
            RenderSystem.depthMask(true);
            context.profiler().pop();
        }

        private float getStarBrightness(World world, float delta) {
            final float skyAngle = world.getSkyAngle(delta);
            float brightness = 1.0F - (MathHelper.cos((float) (skyAngle * Math.PI * 2.0D) * 2.0F + 0.25F));

            if (brightness < 0.0F) {
                brightness = 0.0F;
            }

            if (brightness > 1.0F) {
                brightness = 1.0F;
            }

            return brightness * brightness * 0.5F + 0.3F;
        }
    }
}
