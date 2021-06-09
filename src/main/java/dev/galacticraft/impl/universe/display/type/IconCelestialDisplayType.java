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

package dev.galacticraft.impl.universe.display.type;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.universe.display.CelestialDisplayType;
import dev.galacticraft.impl.universe.display.config.IconCelestialDisplayConfig;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;

public class IconCelestialDisplayType extends CelestialDisplayType<IconCelestialDisplayConfig> {
    public static final IconCelestialDisplayType INSTANCE = new IconCelestialDisplayType(IconCelestialDisplayConfig.CODEC);

    protected IconCelestialDisplayType(Codec<IconCelestialDisplayConfig> codec) {
        super(codec);
    }

    @Override
    public void render(MatrixStack matrices, BufferBuilder buffer, int scale, int mouseX, int mouseY, float delta, IconCelestialDisplayConfig config) {
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(-8 * config.scale(), -8 * config.scale(), 0).texture(config.u(), config.v()).color(255, 255, 255, 255).next();
        buffer.vertex(-8 * config.scale(), 8 * config.scale(), 0).texture(config.u(), config.v() + config.height()).color(255, 255, 255, 255).next();
        buffer.vertex(8 * config.scale(), 8 * config.scale(), 0).texture(config.u() + config.width(), config.v() + config.height()).color(255, 255, 255, 255).next();
        buffer.vertex(8 * config.scale(), -8 * config.scale(), 0).texture(config.u() + config.width(), config.v()).color(255, 255, 255, 255).next();
        buffer.end();
        BufferRenderer.draw(buffer);
    }
}
