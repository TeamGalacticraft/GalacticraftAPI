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

package dev.galacticraft.impl.internal.mixin;

import dev.galacticraft.api.accessor.GearInventoryProvider;
import dev.galacticraft.api.accessor.SatelliteAccessor;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.universe.position.config.SatelliteConfig;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin {
    @Shadow public abstract MinecraftServer getServer();

    @Shadow public abstract void broadcastAll(Packet<?> packet);

    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void galacticraft_syncGearInventory(Connection connection, ServerPlayer player, CallbackInfo ci) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        Container inventory = ((GearInventoryProvider) player).getGearInv();
        buf.writeInt(player.getId());
        buf.writeInt(inventory.getContainerSize());
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            buf.writeItem(inventory.getItem(i));
        }

        this.broadcastAll(new ClientboundCustomPayloadPacket(new ResourceLocation(Constant.MOD_ID, "gear_inv_sync"), buf));

        ((SatelliteAccessor) this.getServer()).getSatellites().forEach((id, satellite) -> {
            CompoundTag compound = (CompoundTag) SatelliteConfig.CODEC.encode(satellite.config(), NbtOps.INSTANCE, new CompoundTag()).get().orThrow();
            connection.send(new ClientboundCustomPayloadPacket(new ResourceLocation(Constant.MOD_ID, "add_satellite"), PacketByteBufs.create().writeResourceLocation(id).writeNbt(compound)));
        });
    }
}
