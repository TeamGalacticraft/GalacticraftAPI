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
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.galacticraft.api.accessor.WorldOxygenAccessor;
import dev.galacticraft.api.registry.AddonRegistry;
import dev.galacticraft.impl.Constant;
import dev.galacticraft.impl.command.argument.RegistryArgumentType;
import dev.galacticraft.impl.universe.celestialbody.type.SpaceStationType;
import dev.galacticraft.impl.command.argument.serializer.RegistryArgumentTypeSerializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.mixin.command.ArgumentTypesAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@ApiStatus.Internal
public class GCApiCommands {
    public static void register() {
        RegistryArgumentTypeSerializer serializer = new RegistryArgumentTypeSerializer();
        ArgumentTypesAccessor.fabric_getClassMap().put( RegistryArgumentType.class, serializer);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, commandSelection) -> {
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                dispatcher.register(Commands.literal(Constant.MOD_ID + ":debug")
                            .requires(serverCommandSource -> serverCommandSource.hasPermission(2)).then(Commands.literal("registry").then(Commands.argument("registry", RegistryArgumentType.create())
                                    .then(Commands.literal("dump_ids").executes(GCApiCommands::dumpRegistries))
                                    .then(Commands.literal("get")
                                            .then(Commands.argument("id", ResourceLocationArgument.id())
                                                    .executes(GCApiCommands::getRegistryValue)))
                                    .then(Commands.literal("get_raw")
                                            .then(Commands.argument("id", IntegerArgumentType.integer())
                                                    .executes(GCApiCommands::getRegistryValueFromRawId)))
                                    .then(Commands.literal("to_raw")
                                            .then(Commands.argument("id", ResourceLocationArgument.id())
                                                    .executes(GCApiCommands::getRegistryRawId)))
                                    .then(Commands.literal("dump_values")
                                            .then(Commands.argument("id", ResourceLocationArgument.id())
                                                    .executes(GCApiCommands::dumpRegistryValues))))));
            }
            dispatcher.register(Commands.literal("oxygen").requires(source -> source.hasPermission(2))
                    .then(Commands.literal("get").then(Commands.argument("start_pos", BlockPosArgument.blockPos())
                            .executes(GCApiCommands::getOxygen)
                            .then(Commands.argument("end_pos", BlockPosArgument.blockPos())
                                    .executes(GCApiCommands::getOxygenArea))))
                    .then(Commands.literal("set")
                            .then(Commands.argument("start_pos", BlockPosArgument.blockPos())
                                    .then(Commands.argument("oxygen", BoolArgumentType.bool())
                                            .executes(GCApiCommands::setOxygen))
                                    .then(Commands.argument("end_pos", BlockPosArgument.blockPos())
                                            .then(Commands.argument("oxygen", BoolArgumentType.bool())
                                                    .executes(GCApiCommands::setOxygenArea))))));

            dispatcher.register(Commands.literal("space_station").requires(source -> source.hasPermission(2))
                    .then(Commands.literal("add").then(Commands.argument("world", ResourceLocationArgument.id())
                            .executes(GCApiCommands::addSpaceStation)
                            .then(Commands.argument("structure", ResourceLocationArgument.id())
                                    .executes(GCApiCommands::addSpaceStationStructured))))
                    .then(Commands.literal("remove").then(Commands.argument("id", ResourceLocationArgument.id())
                            .executes(GCApiCommands::removeSpaceStation))));
        });
    }

    private static int removeSpaceStation(@NotNull CommandContext<CommandSourceStack> ctx) {
        if (SpaceStationType.removeSatellite(ctx.getSource().getServer(), ResourceLocationArgument.getId(ctx, "id"))) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int addSpaceStationStructured(@NotNull CommandContext<CommandSourceStack> ctx) {
        StructureTemplate structure = ctx.getSource().getServer().getStructureManager().get(ResourceLocationArgument.getId(ctx, "structure")).orElseThrow();
        SpaceStationType.registerSpaceStation(ctx.getSource().getServer(), ctx.getSource().getPlayer(), Objects.requireNonNull(ctx.getSource().registryAccess().registryOrThrow(AddonRegistry.CELESTIAL_BODY_KEY).get(ResourceLocationArgument.getId(ctx, "world"))), structure);
        return 1;
    }

    private static int addSpaceStation(@NotNull CommandContext<CommandSourceStack> ctx) {
        SpaceStationType.registerSpaceStation(ctx.getSource().getServer(), ctx.getSource().getPlayer(), Objects.requireNonNull(ctx.getSource().registryAccess().registryOrThrow(AddonRegistry.CELESTIAL_BODY_KEY).get(ResourceLocationArgument.getId(ctx, "world"))), new StructureTemplate());
        return 1;
    }

    private static int setOxygen(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "start_pos");
        boolean b = BoolArgumentType.getBool(context, "oxygen");
        ((WorldOxygenAccessor) context.getSource().getLevel()).setBreathable(pos, b);
        context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.set.single"), true);
        return 1;
    }

    private static int setOxygenArea(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos startPos = BlockPosArgument.getLoadedBlockPos(context, "start_pos");
        BlockPos endPos = BlockPosArgument.getLoadedBlockPos(context, "end_pos");
        WorldOxygenAccessor accessor = (WorldOxygenAccessor) context.getSource().getLevel();
        BoundingBox box = BoundingBox.fromCorners(startPos, endPos);
        boolean b = BoolArgumentType.getBool(context, "oxygen");
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int y = box.minX(); y <= box.maxX(); y++) {
                for (int z = box.minX(); z <= box.maxX(); z++) {
                    accessor.setBreathable(mutable.set(x, y, z), b);
                }
            }
        }

        context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.set.multiple"), true);
        return 1;
    }

    private static int getOxygen(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "start_pos");
        if (((WorldOxygenAccessor) context.getSource().getLevel()).isBreathable(pos)) {
            context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.get.single.oxygen"), false);
        } else {
            context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.get.single.no_oxygen"), false);
        }
        return 1;
    }

    private static int getOxygenArea(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos startPos = BlockPosArgument.getLoadedBlockPos(context, "start_pos");
        BlockPos endPos = BlockPosArgument.getLoadedBlockPos(context, "end_pos");
        WorldOxygenAccessor accessor = (WorldOxygenAccessor) context.getSource().getLevel();
        BoundingBox box = BoundingBox.fromCorners(startPos, endPos);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        boolean allOxygen = true;
        boolean hasSomeOxygen = false;
        boolean breathable;
        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int y = box.minX(); y <= box.maxX(); y++) {
                for (int z = box.minX(); z <= box.maxX(); z++) {
                    breathable = accessor.isBreathable(mutable.set(x, y, z));
                    hasSomeOxygen |= breathable;
                    allOxygen = allOxygen && breathable;
                }
            }
        }
        if (allOxygen) {
            context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.get.area.full"), false);
        } else if (hasSomeOxygen) {
            context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.get.area.partial"), false);
        } else {
            context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.oxygen.get.area.none"), false);
        }
        return 1;
    }

    private static int getRegistryValue(CommandContext<CommandSourceStack> context) {
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.debug.registry.id", registry.key().location(), registry.get(ResourceLocationArgument.getId(context, "id"))), true);
        return 1;
    }

    private static int getRegistryRawId(CommandContext<CommandSourceStack> context) {
        Registry<? super Object> registry = RegistryArgumentType.getRegistry(context, "registry");
        Object o = registry.get(ResourceLocationArgument.getId(context, "id"));
        context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.debug.registry.id", registry.key().location(), registry.getId(o)), true);
        return 1;
    }

    private static int dumpRegistryValues(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        source.sendSuccess(Component.translatable("command.galacticraft-api.debug.registry.dump", registry.key().location().toString()), true);
        for (ResourceLocation id : registry.keySet()) {
            source.sendSuccess(Component.literal(id.toString() + " - " + registry.get(id)), false);
        }
        return 1;
    }

    private static int dumpRegistries(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        source.sendSuccess(Component.translatable("command.galacticraft-api.debug.registry.dump", registry.key().location().toString()), true);
        for (ResourceLocation id : registry.keySet()) {
            source.sendSuccess(Component.literal(id.toString()), false);
        }
        return 1;
    }

    private static int getRegistryValueFromRawId(CommandContext<CommandSourceStack> context) {
        Registry<?> registry = RegistryArgumentType.getRegistry(context, "registry");
        context.getSource().sendSuccess(Component.translatable("command.galacticraft-api.debug.registry.id", registry.key().location(), registry.byId(IntegerArgumentType.getInteger(context, "id"))), true);
        return 1;
    }
}
