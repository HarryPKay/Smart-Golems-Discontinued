package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

public class Move {

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("move")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument("name", StringArgumentType.string())
                        .then(Commands.argument("block position", BlockPosArgument.blockPos())
                                .executes(ctx -> {

                                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "name"));

                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                                return 0;
                                            }

                                            BlockPos blockPos = BlockPosArgument.getBlockPos(ctx, "block position");

                                            golemEntity.getNavigator().clearPath();
                                            if (!golemEntity.getNavigator().tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.6D)) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not set path."));
                                            }

                                            return 0;
                                        }
                                )));
    }
}