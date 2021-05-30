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

package dev.galacticraft.api.internal.mixin.client;

import dev.galacticraft.api.celestialbody.satellite.Satellite;
import dev.galacticraft.api.internal.accessor.ClientSatelliteAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin implements ClientSatelliteAccessor {
    private final @Unique List<Satellite> satellites_gcr = new ArrayList<>();
    private final @Unique List<SatelliteListener> listeners_gcr = new ArrayList<>();
    @Shadow private ClientWorld world;

    @Override
    public @Unmodifiable List<Satellite> getSatellites() {
        return this.satellites_gcr;
    }

    @Override
    public void addSatellite(Satellite satellite) {
        this.satellites_gcr.add(satellite);
        for (SatelliteListener listener : this.listeners_gcr) {
            listener.onSatelliteUpdated(satellite, true);
        }
    }

    @Override
    public void removeSatellite(Identifier id) {
        int index = -1;
        for (int i = 0; i < this.satellites_gcr.size(); i++) {
            if (this.satellites_gcr.get(i).getId() ==id) {
                index = i;
                break;
            }
        }

        Satellite removed = this.satellites_gcr.remove(index);;
        for (SatelliteListener listener : this.listeners_gcr) {
            listener.onSatelliteUpdated(removed, false);
        }
    }

    @Override
    public void addListener(SatelliteListener listener) {
        this.listeners_gcr.add(listener);
    }

    @Override
    public void removeListener(SatelliteListener listener) {
        this.listeners_gcr.remove(listener);
    }
}
