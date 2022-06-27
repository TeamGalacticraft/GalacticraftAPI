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

package dev.galacticraft.impl.internal.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.galacticraft.api.accessor.WorldOxygenAccessor;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.command.argument.RegistryArgumentType;
import dev.galacticraft.impl.universe.celestialbody.type.SatelliteType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.structure.Structure;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@ApiStatus.Internal
public class GCApiCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, server) -> {
            dispatcher.register(CommandManager.literal(Constant.MOD_ID + ":debug")
                            .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)).then(CommandManager.literal("registry").then(CommandManager.argument("registry", RegistryArgumentType.create())
                    .then(CommandManager.literal("dump_ids").executes(GCApiCommands::dumpRegistries))
                    .then(CommandManager.literal("get")
                            .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                    .executes(GCApiCommands::getRegistryValue)))
                    .then(CommandManager.literal("get_raw")
                            .then(CommandManager.argument("id", IntegerArgumentType.integer())
                                    .executes(GCApiCommands::getRegistryValueFromRawId)))
                    .then(CommandManager.literal("to_raw")
                            .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                    .executes(GCApiCommands::getRegistryRawId)))
                    .then(CommandManager.literal("dump_values")
                            .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                    .executes(GCApiCommands::dumpRegistryValues))))));

            dispatcher.register(CommandManager.literal(Constant.MOD_ID + ":oxygen").requires(source -> source.hasPermissionLevel(3))
                    .then(CommandManager.literal("get").then(CommandManager.argument("start_pos", BlockPosArgumentType.blockPos())
                            .executes(GCApiCommands::getOxygen)
                            .then(CommandManager.argument("end_pos", BlockPosArgumentType.blockPos())
                                    .executes(GCApiCommands::getOxygenArea))))
                    .then(CommandManager.literal("set")
                            .then(CommandManager.argument("start_pos", BlockPosArgumentType.blockPos())
                                    .then(CommandManager.argument("oxygen", BoolArgumentType.bool())
                                            .executes(GCApiCommands::setOxygen))
                                    .then(CommandManager.argument("end_pos", BlockPosArgumentType.blockPos())
                                            .then(CommandManager.argument("oxygen", BoolArgumentType.bool())
                                                    .executes(GCApiCommands::setOxygenArea))))));

            dispatcher.register(CommandManager.literal(Constant.MOD_ID + ":satellite").requires(source -> source.hasPermissionLevel(3))
                    .then(CommandManager.literal("add").then(CommandManager.argument("world", IdentifierArgumentType.identifier())
                            .executes(GCApiCommands::addSatellite)
                            .then(CommandManager.argument("structure", IdentifierArgumentType.identifier())
                                    .executes(GCApiCommands::addSatelliteStructured))))
                    .then(CommandManager.literal("remove").then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                            .executes(GCApiCommands::removeSatellite))));
        });
    }

    private static int removeSatellite(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        if (SatelliteType.removeSatellite(ctx.getSource().getServer(), IdentifierArgumentType.getIdentifier(ctx, "id"))) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int addSatelliteStructured(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Structure structure = ctx.getSource().getServer().getStructureManager().getStructure(IdentifierArgumentType.getIdentifier(ctx, "structure")).orElseThrow();
        SatelliteType.registerSatellite(ctx.getSource().getServer(), ctx.getSource().getPlayer(), Objects.requireNonNull(ctx.getSource().getRegistryManager().get(AddonRegistry.CELESTIAL_BODY_KEY).get(IdentifierArgumentType.getIdentifier(ctx, "world"))), structure);
        return 1;
    }

    private static int addSatellite(@NotNull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        SatelliteType.registerSatellite(ctx.getSource().getServer(), ctx.getSource().getPlayer(), Objects.requireNonNull(ctx.getSource().getRegistryManager().get(AddonRegistry.CELESTIAL_BODY_KEY).get(IdentifierArgumentType.getIdentifier(ctx, "world"))), new Structure());
        return 1;
    }

    private static int setOxygen(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgumentType.getLoadedBlockPos(context, "start_pos");
        boolean b = BoolArgumentType.getBool(context, "oxygen");
        ((WorldOxygenAccessor) context.getSource().getWorld()).setBreathable(pos, b);
        context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.set.single"), true);
        return 1;
    }

    private static int setOxygenArea(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos startPos = BlockPosArgumentType.getLoadedBlockPos(context, "start_pos");
        BlockPos endPos = BlockPosArgumentType.getLoadedBlockPos(context, "end_pos");
        WorldOxygenAccessor accessor = (WorldOxygenAccessor) context.getSource().getWorld();
        BlockBox box = BlockBox.create(startPos, endPos);
        boolean b = BoolArgumentType.getBool(context, "oxygen");
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = box.getMinX(); x <= box.getMaxX(); x++) {
            for (int y = box.getMinX(); y <= box.getMaxX(); y++) {
                for (int z = box.getMinX(); z <= box.getMaxX(); z++) {
                    accessor.setBreathable(mutable.set(x, y, z), b);
                }
            }
        }

        context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.set.multiple"), true);
        return 1;
    }

    private static int getOxygen(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgumentType.getLoadedBlockPos(context, "start_pos");
        if (((WorldOxygenAccessor) context.getSource().getWorld()).isBreathable(pos)) {
            context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.get.single.oxygen"), false);
        } else {
            context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.get.single.no_oxygen"), false);
        }
        return 1;
    }

    private static int getOxygenArea(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos startPos = BlockPosArgumentType.getLoadedBlockPos(context, "start_pos");
        BlockPos endPos = BlockPosArgumentType.getLoadedBlockPos(context, "end_pos");
        WorldOxygenAccessor accessor = (WorldOxygenAccessor) context.getSource().getWorld();
        BlockBox box = BlockBox.create(startPos, endPos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        boolean allOxygen = true;
        boolean hasSomeOxygen = false;
        boolean breathable;
        for (int x = box.getMinX(); x <= box.getMaxX(); x++) {
            for (int y = box.getMinX(); y <= box.getMaxX(); y++) {
                for (int z = box.getMinX(); z <= box.getMaxX(); z++) {
                    breathable = accessor.isBreathable(mutable.set(x, y, z));
                    hasSomeOxygen |= breathable;
                    allOxygen = allOxygen && breathable;
                }
            }
        }
        if (allOxygen) {
            context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.get.area.full"), false);
        } else if (hasSomeOxygen) {
            context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.get.area.partial"), false);
        } else {
            context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.oxygen.get.area.none"), false);
        }
        return 1;
    }

    private static int getRegistryValue(CommandContext<ServerCommandSource> context) {
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.get(IdentifierArgumentType.getIdentifier(context, "id"))), true);
        return 1;
    }

    private static int getRegistryRawId(CommandContext<ServerCommandSource> context) {
        Registry<? super Object> registry = RegistryArgumentType.getRegistry(context, "registry");
        Object o = registry.get(IdentifierArgumentType.getIdentifier(context, "id"));
        context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.getRawId(o)), true);
        return 1;
    }

    private static int dumpRegistryValues(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        source.sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.dump", registry.getKey().getValue().toString()), true);
        for (Identifier id : registry.getIds()) {
            source.sendFeedback(new LiteralText(id.toString() + " - " + registry.get(id)), false);
        }
        return 1;
    }

    private static int dumpRegistries(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        source.sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.dump", registry.getKey().getValue().toString()), true);
        for (Identifier id : registry.getIds()) {
            source.sendFeedback(new LiteralText(id.toString()), false);
        }
        return 1;
    }

    private static int getRegistryValueFromRawId(CommandContext<ServerCommandSource> context) {
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        context.getSource().sendFeedback(new TranslatableText("command.galacticraft-api.debug.registry.id", registry.getKey().getValue(), registry.get(IntegerArgumentType.getInteger(context, "id"))), true);
        return 1;
    }
}
