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

package dev.galacticraft.impl.command.argument.serializer;

import com.google.gson.JsonObject;
import dev.galacticraft.impl.command.argument.RegistryArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class RegistryArgumentTypeSerializer implements ArgumentTypeInfo<RegistryArgumentType<?>, RegistryArgumentTypeSerializer.RegistryArgumentProperties> {

    @Override
    public void serializeToNetwork(RegistryArgumentProperties properties, FriendlyByteBuf buf) {

    }

    @Override
    public RegistryArgumentProperties deserializeFromNetwork(FriendlyByteBuf buf) {
        return null;
    }

    @Override
    public void serializeToJson(RegistryArgumentProperties properties, JsonObject json) {

    }

    @Override
    public RegistryArgumentProperties unpack(RegistryArgumentType argumentType) {
        return new RegistryArgumentProperties();
    }

    public final class RegistryArgumentProperties implements Template<RegistryArgumentType<?>> {

        @Override
        public RegistryArgumentType<?> instantiate(CommandBuildContext commandRegistryAccess) {
            return null;
        }

        @Override
        public ArgumentTypeInfo<RegistryArgumentType<?>, ?> type() {
            return RegistryArgumentTypeSerializer.this;
        }
    }
}
