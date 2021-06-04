package dev.galacticraft.impl.universe.display.type;

import com.mojang.serialization.Codec;
import dev.galacticraft.api.rocket.part.travel.TravelPredicateType;
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
    public void render(MatrixStack matrices, BufferBuilder buffer, int mouseX, int mouseY, float delta, IconCelestialDisplayConfig config) {
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(-8 * config.scale(), -8 * config.scale(), 0).texture(config.u(), config.v()).color(255, 255, 255, 255).next();
        buffer.vertex(-8 * config.scale(), 8 * config.scale(), 0).texture(config.u(), config.v() + config.height()).color(255, 255, 255, 255).next();
        buffer.vertex(8 * config.scale(), 8 * config.scale(), 0).texture(config.u() + config.width(), config.v() + config.height()).color(255, 255, 255, 255).next();
        buffer.vertex(8 * config.scale(), -8 * config.scale(), 0).texture(config.u() + config.width(), config.v()).color(255, 255, 255, 255).next();
        buffer.end();
        BufferRenderer.draw(buffer);
    }
}
