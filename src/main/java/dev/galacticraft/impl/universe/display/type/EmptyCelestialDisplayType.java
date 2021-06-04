package dev.galacticraft.impl.universe.display.type;

import dev.galacticraft.api.universe.display.CelestialDisplayType;
import dev.galacticraft.impl.universe.display.config.EmptyCelestialDisplayConfig;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;

public class EmptyCelestialDisplayType extends CelestialDisplayType<EmptyCelestialDisplayConfig> {
    public static final EmptyCelestialDisplayType INSTANCE = new EmptyCelestialDisplayType();

    private EmptyCelestialDisplayType() {
        super(EmptyCelestialDisplayConfig.CODEC);
    }

    @Override
    public void render(MatrixStack matrices, BufferBuilder buffer, int mouseX, int mouseY, float delta, EmptyCelestialDisplayConfig config) {

    }
}
