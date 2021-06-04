package dev.galacticraft.api.universe.display;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;

public abstract class CelestialDisplayType<C extends CelestialDisplayConfig> {
    private final Codec<CelestialDisplay<C, CelestialDisplayType<C>>> codec;
    public CelestialDisplayType(Codec<C> codec) {
        this.codec = codec.fieldOf("config").xmap((config) -> new CelestialDisplay<>(this, config), CelestialDisplay::config).codec();
    }

    @Environment(EnvType.CLIENT)
    public abstract void render(MatrixStack matrices, BufferBuilder buffer, int mouseX, int mouseY, float delta, C config); //todo

    public Codec<CelestialDisplay<C, CelestialDisplayType<C>>> getCodec() {
        return this.codec;
    }

    public CelestialDisplay<C, CelestialDisplayType<C>> configure(C config) {
        return new CelestialDisplay<>(this, config);
    }
}
