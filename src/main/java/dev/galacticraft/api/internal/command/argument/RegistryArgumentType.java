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

package dev.galacticraft.api.internal.command.argument;

import com.google.common.collect.ImmutableList;
import dev.galacticraft.api.registry.AddonRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RegistryArgumentType<T> implements ArgumentType<Registry<T>> {
    private RegistryArgumentType() {
    }

    public static <T> RegistryArgumentType<T> create() {
        return new RegistryArgumentType<>();
    }

    @Override
    public Registry<T> parse(StringReader reader) throws CommandSyntaxException {
        RegistryKey<Registry<T>> key = RegistryKey.ofRegistry(Identifier.fromCommandInput(reader));
        return ((Registry<Registry<T>>)Registry.REGISTRIES).get(key);
    }

    public static <T> Registry<T> getRegistry(CommandContext<ServerCommandSource> context, String id) {
        Registry<T> registry = context.getArgument(id, Registry.class);
        Optional<MutableRegistry<T>> dynamic = context.getSource().getRegistryManager().getOptional(registry.getKey());
        return dynamic.isPresent() ? dynamic.get() : registry;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(Registry.REGISTRIES.getIds().stream(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ImmutableList.of(
                AddonRegistry.CELESTIAL_BODY_TYPE_KEY.getValue().toString(),
                AddonRegistry.SOLAR_SYSTEM_TYPE_KEY.getValue().toString(),
                AddonRegistry.ATMOSPHERIC_GAS_KEY.getValue().toString(),
                AddonRegistry.PERMISSIONS_KEY.getValue().toString()
        );
    }
}
