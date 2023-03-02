/*
 * Copyright (c) 2019-2023 Team Galacticraft
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

package dev.galacticraft.impl.internal.mixin.oxygen;

import dev.galacticraft.api.accessor.LevelOxygenAccessor;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenAccessor;
import dev.galacticraft.impl.internal.accessor.InternalLevelOxygenAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(Level.class)
public abstract class LevelMixin implements LevelOxygenAccessor, InternalLevelOxygenAccessor, LevelAccessor {
    private static final int WORLD_SIZE = 30000000;

    private @Unique boolean breathable = true;

    @Shadow public abstract @NotNull LevelChunk getChunk(int i, int j);

    @Shadow public abstract boolean isClientSide();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initializeOxygenValues(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder holder, Supplier supplier, boolean bl, boolean bl2, long l, int i, CallbackInfo ci) {
        this.setDefaultBreathable(CelestialBody.getByDimension(registryAccess, resourceKey).map(c -> c.atmosphere().breathable()).orElse(true));
    }

    @Override
    public boolean isBreathable(int x, int y, int z) {
        return this.validPosition(x, y, z) ? (this.breathable && !((ChunkOxygenAccessor) this.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))).galacticraft$isInverted(x & 15, y, z & 15))
                || (!this.breathable && ((ChunkOxygenAccessor) this.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))).galacticraft$isInverted(x & 15, y, z & 15))
                : this.breathable;
    }

    @Override
    public void setBreathable(int x, int y, int z, boolean value) {
        if (this.validPosition(x, y, z)) {
            ((ChunkOxygenAccessor) this.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))).galacticraft$setInverted(x & 15, y, z & 15, this.breathable != value);
        }
    }

    @Override
    public boolean getDefaultBreathable() {
        return this.breathable;
    }

    @Override
    public void setDefaultBreathable(boolean breathable) {
        this.breathable = breathable;
    }

    private boolean validPosition(int x, int y, int z) {
        return y >= this.getMinBuildHeight() && y <= this.getMaxBuildHeight()
                && x < WORLD_SIZE && z < WORLD_SIZE && x >= -WORLD_SIZE && z >= -WORLD_SIZE;
    }
}
