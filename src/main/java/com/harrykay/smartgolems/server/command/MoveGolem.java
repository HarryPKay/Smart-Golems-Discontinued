package com.harrykay.smartgolems.server.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;

public class MoveGolem {

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("move")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument("golem name", StringArgumentType.string())
                        .then(Commands.argument("block position", BlockPosArgument.blockPos())
                                .executes(ctx -> {


//                                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem name"));
//
//                                            if (golemEntity == null) {
//                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
//                                                return 0;
//                                            }
//
//                                            BlockPos blockPos = BlockPosArgument.getBlockPos(ctx, "block position");
//
//                                            golemEntity.getNavigator().clearPath();
//                                            if (!golemEntity.getNavigator().tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.6D)) {
//                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not set path."));
//                                            }

                                            return 0;
                                        }
                                )));
    }
}