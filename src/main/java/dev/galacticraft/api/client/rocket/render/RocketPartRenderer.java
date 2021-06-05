package dev.galacticraft.api.client.rocket.render;

import dev.galacticraft.api.entity.Rocket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface RocketPartRenderer {
    default void renderGUI(ClientWorld world, MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    void render(ClientWorld world, MatrixStack matrices, Rocket rocket, VertexConsumerProvider vertices, float delta, int light);
}
