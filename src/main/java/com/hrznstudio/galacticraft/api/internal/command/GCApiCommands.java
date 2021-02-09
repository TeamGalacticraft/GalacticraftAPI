package com.hrznstudio.galacticraft.api.internal.command;

import com.hrznstudio.galacticraft.api.internal.command.argument.RegistryArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class GCApiCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, server) -> {
            LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("gc-api:debug")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .executes(context -> {
                        context.getSource().sendError(new TranslatableText("command.galacticraft-api.debug"));
                        return -1;
                    });
            builder.then(CommandManager.literal("registry").then(CommandManager.argument("registry", RegistryArgumentType.create()).then(CommandManager.literal("dump_values").executes(context -> {
                ServerCommandSource source = context.getSource();
                Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
                source.sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.dump", registry.getKey().getValue().toString()), true);
                for (Identifier id : registry.getIds()) {
                    source.sendFeedback(new LiteralText(id.toString()), false);
                }
                return 1;
            })).then(CommandManager.literal("get").then(CommandManager.argument("id", IdentifierArgumentType.identifier()).executes(context -> {
                Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
                context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.get(IdentifierArgumentType.getIdentifier(context, "id"))), true);
                return 1;
            }))).then(CommandManager.literal("get_raw").then(CommandManager.argument("id", IntegerArgumentType.integer()).executes(context -> {
                Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
                context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.get(IntegerArgumentType.getInteger(context, "id"))), true);
                return 1;
            }))).then(CommandManager.literal("to_raw").then(CommandManager.argument("id", IdentifierArgumentType.identifier()).executes(context -> {
                Registry registry = RegistryArgumentType.getRegistry(context, "registry");
                Object o = registry.get(IdentifierArgumentType.getIdentifier(context, "id"));
                context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.getRawId(o)), true);
                return 1;
            })))));
            commandDispatcher.register(builder);
        });
    }
}
