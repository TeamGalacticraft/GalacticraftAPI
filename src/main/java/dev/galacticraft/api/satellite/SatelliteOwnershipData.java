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

package dev.galacticraft.api.satellite;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;
import java.util.stream.LongStream;

public class SatelliteOwnershipData {
    public static final Codec<SatelliteOwnershipData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG_STREAM.fieldOf("owner").xmap(stream -> {
                PrimitiveIterator.OfLong iterator = stream.iterator();
                return new UUID(iterator.nextLong(), iterator.nextLong());
            }, uuid -> LongStream.of(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits())).forGetter(SatelliteOwnershipData::getOwner),
            Codec.STRING.fieldOf("username").forGetter(SatelliteOwnershipData::getUsername),
            Codec.LONG_STREAM.listOf().fieldOf("owner").xmap(list -> {
                List<UUID> uuids = new ArrayList<>(list.size());
                for (LongStream longStream : list) {
                    PrimitiveIterator.OfLong iterator = longStream.iterator();
                    uuids.add(new UUID(iterator.nextLong(), iterator.nextLong()));
                }
                return uuids;
            }, list -> {
                List<LongStream> longs = new ArrayList<>(list.size());
                for (UUID uuid : list) {
                    longs.add(LongStream.of(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()));
                }
                return longs;
            }).forGetter(SatelliteOwnershipData::getTrusted),
            Codec.BOOL.fieldOf("open").forGetter(SatelliteOwnershipData::isOpen)
            ).apply(instance, SatelliteOwnershipData::new));

    private final UUID owner;
    private String username;
    private final List<UUID> trusted;
    private boolean open;

    public SatelliteOwnershipData(@NotNull UUID owner, String username, List<UUID> trusted, boolean open) {
        this.owner = owner;
        this.username = username;
        this.open = open;
        this.trusted = trusted;
    }

    public static SatelliteOwnershipData fromPacket(PacketByteBuf buf) {
        int size = buf.readInt();
        SatelliteOwnershipData data = new SatelliteOwnershipData(buf.readUuid(), buf.readString(), new ArrayList<>(size), buf.readBoolean());
        for (int i = 0; i < size; i++) {
            data.trusted.add(buf.readUuid());
        }
        return data;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull UUID getOwner() {
        return owner;
    }

    public boolean isOpen() {
        return this.open;
    }

    public @NotNull List<UUID> getTrusted() {
        return ImmutableList.copyOf(trusted);
    }

    public void addTrusted(UUID uuid) {
        this.trusted.add(uuid);
    }

    public void removeTrusted(UUID uuid) {
        this.trusted.remove(uuid);
    }

    public boolean canAccess(PlayerEntity player) {
        return player.getUuid().equals(getOwner()) || trusted.contains(player.getUuid());
    }

    public void writePacket(PacketByteBuf buf) {
        buf.writeUuid(getOwner());
        buf.writeString(getUsername());
        buf.writeBoolean(isOpen());
        buf.writeInt(trusted.size());
        for (UUID uuid : trusted) {
            buf.writeUuid(uuid);
        }
    }

    public NbtCompound toTag(NbtCompound tag) {
        tag.putUuid("owner", getOwner());
        tag.putString("username", getUsername());
        tag.putBoolean("open", isOpen());
        long[] trusted = new long[this.trusted.size() * 2];
        List<UUID> uuids = this.trusted;
        for (int i = 0, uuidsSize = uuids.size(); i < uuidsSize; i++) {
            UUID uuid = uuids.get(i);
            trusted[i * 2] = uuid.getMostSignificantBits();
            trusted[(i * 2) + 1] = uuid.getLeastSignificantBits();
        }
        tag.putLongArray("trusted", trusted);
        return tag;
    }

    public static SatelliteOwnershipData fromTag(NbtCompound tag) {
        long[] trusted = tag.getLongArray("trusted");
        SatelliteOwnershipData data = new SatelliteOwnershipData(tag.getUuid("owner"), tag.getString("username"), new ArrayList<>(trusted.length / 2), tag.getBoolean("open"));
        for (int i = 0; i < trusted.length; i += 2) {
            data.trusted.add(new UUID(trusted[i], trusted[i + 1]));
        }
        return data;
    }
}
