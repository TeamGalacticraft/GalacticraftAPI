package com.hrznstudio.galacticraft.api.internal.command.argument;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RegistryArgumentType implements ArgumentType<Registry<?>> {
    private RegistryArgumentType() {
    }

    public static RegistryArgumentType create() {
        return new RegistryArgumentType();
    }

    @Override
    public Registry<?> parse(StringReader reader) throws CommandSyntaxException {
        return Registry.REGISTRIES.get(new Identifier(reader.readString()));
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
