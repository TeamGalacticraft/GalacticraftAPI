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

package dev.galacticraft.impl.internal.client.fabric;

import dev.galacticraft.api.accessor.GearInventoryProvider;
import dev.galacticraft.api.accessor.SatelliteAccessor;
import dev.galacticraft.api.client.accessor.ClientResearchAccessor;
import dev.galacticraft.api.universe.celestialbody.CelestialBody;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.internal.accessor.ChunkOxygenSyncer;
import dev.galacticraft.impl.universe.celestialbody.type.SpaceStationType;
import dev.galacticraft.impl.universe.position.config.SpaceStationConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class GalacticraftAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constant.LOGGER.info("Loaded client module");
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(Constant.MOD_ID, "research_update"), (client, networkHandler, buffer, sender) -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(buffer.copy());
            client.execute(() -> ((ClientResearchAccessor) Objects.requireNonNull(client.player)).readChanges(buf));
        });
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(Constant.MOD_ID, "add_satellite"), (client, networkHandler, buffer, sender) -> {
            ((SatelliteAccessor) networkHandler).addSatellite(buffer.readResourceLocation(), new CelestialBody<>(SpaceStationType.INSTANCE, SpaceStationConfig.CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, networkHandler.registryAccess()), buffer.readNbt()).get().orThrow().getFirst()));
        });
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(Constant.MOD_ID, "remove_satellite"), (client, networkHandler, buffer, sender) -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(buffer.copy());
            ((SatelliteAccessor) networkHandler).removeSatellite(buf.readResourceLocation());
        });
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(Constant.MOD_ID, "oxygen_update"), (client, handler, buf, responseSender) -> {
            byte b = buf.readByte();
            ChunkOxygenSyncer syncer = ((ChunkOxygenSyncer) handler.getLevel().getChunk(buf.readInt(), buf.readInt()));
            syncer.readOxygenUpdate(b, buf);
        });

        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(Constant.MOD_ID, "gear_inv_sync"), (client, handler, buf, responseSender) -> {
            int entity = buf.readInt();
            ItemStack[] stacks = new ItemStack[buf.readInt()];
            for (int i = 0; i < stacks.length; i++) {
                stacks[i] = buf.readItem();
            }
            client.execute(() -> {
                for (int i = 0; i < stacks.length; i++) {
                    ((GearInventoryProvider) Objects.requireNonNull(client.level.getEntity(entity))).getGearInv().setItem(i, stacks[i]);
                }
            });
        });
    }
}
