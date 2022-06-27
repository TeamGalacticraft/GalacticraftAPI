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
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestServer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow public abstract MinecraftServer getServer();

    @Shadow public abstract void sendToAll(Packet<?> packet);

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void galacticraft_syncGearInventory(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PacketByteBuf buf = PacketByteBufs.create();
        Inventory inventory = ((GearInventoryProvider) player).getGearInv();
        buf.writeInt(player.getId());
        buf.writeInt(inventory.size());
        for (int i = 0; i < inventory.size(); i++) {
            buf.writeItemStack(inventory.getStack(i));
        }

        this.sendToAll(new CustomPayloadS2CPacket(new Identifier(Constant.MOD_ID, "gear_inv_sync"), buf));

        ((SatelliteAccessor) this.getServer()).getSatellites().forEach((id, satellite) -> {
            NbtCompound compound = (NbtCompound) SatelliteConfig.CODEC.encode(satellite.config(), NbtOps.INSTANCE, new NbtCompound()).get().orThrow();
            connection.send(new CustomPayloadS2CPacket(new Identifier(Constant.MOD_ID, "add_satellite"), PacketByteBufs.create().writeIdentifier(id).writeNbt(compound)));
        });
    }
}
