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

package dev.galacticraft.api.internal.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.galacticraft.api.internal.command.argument.RegistryArgumentType;
import dev.galacticraft.api.internal.fabric.GalacticraftAPI;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GCApiCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, server) -> {
            LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(GalacticraftAPI.MOD_ID + ":debug")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .executes(context -> {
                        context.getSource().sendError(new TranslatableText("command.galacticraft-api.debug"));
                        return -1;
                    });
            builder.then(CommandManager.literal("registry").then(CommandManager.argument("registry", RegistryArgumentType.create())
                    .then(CommandManager.literal("dump_ids").executes(context -> {
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
                        Registry<? super Object> registry = RegistryArgumentType.getRegistry(context, "registry");
                        Object o = registry.get(IdentifierArgumentType.getIdentifier(context, "id"));
                        context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.getRawId(o)), true);
                        return 1;
                    }))).then(CommandManager.literal("dump_values").then(CommandManager.argument("id", IdentifierArgumentType.identifier()).executes(context -> {
                        ServerCommandSource source = context.getSource();
                        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
                        source.sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.dump", registry.getKey().getValue().toString()), true);
                        for (Identifier id : registry.getIds()) {
                            source.sendFeedback(new LiteralText(id.toString() + " - " + registry.get(id).toString()), false);
                        }
                        return 1;
                    })))));
            commandDispatcher.register(builder);
        });
    }
}
