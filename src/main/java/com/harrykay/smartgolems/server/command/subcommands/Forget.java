package com.harrykay.smartgolems.server.command.subcommands;

import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import static com.harrykay.smartgolems.server.command.Constants.FORGET_COMMAND;
import static com.harrykay.smartgolems.server.command.Constants.PRIORITY_ARG;

public class Forget {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(FORGET_COMMAND)
                .then(Commands.argument(PRIORITY_ARG, IntegerArgumentType.integer(1, SmartGolemEntity.maxTasks))
                        .executes(ctx -> {

//                                    if (ctx.getSource().getWorld().isRemote()) {
//                                        return 0;
//                                    }
//
//                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
//                                    if (golemEntity == null) {
//                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
//                                        return 0;
//                                    }
//
//                                    int priority = IntegerArgumentType.getInteger(ctx, PRIORITY_ARG);
//                                    if (!golemEntity.taskNameByPriority.containsKey(priority)) {
//                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("No task with that priority exists."));
//                                        return 0;
//                                    }
//
//                                    golemEntity.removeGoal(priority);
                                    return 0;
                                }
                        )
                ).then(Commands.argument("command", StringArgumentType.string())
                        .executes(ctx -> {

//                                    if (ctx.getSource().getWorld().isRemote()) {
//                                        return 0;
//                                    }
//
//                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem"));
//                                    if (golemEntity == null) {
//                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
//                                        return 0;
//                                    }
//
//
//                                    if (StringArgumentType.getString(ctx, "command") == null || !StringArgumentType.getString(ctx, "command").isEmpty()) {
//
//                                    }
//
//                                    //
////
////                                    Integer priority = IntegerArgumentType.getInteger(ctx, ARG_3);
////                                    if (!golemEntity.taskNameByPriority.containsKey(priority)) {
////                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("No task with that priority exists."));
////                                        return 0;
////                                    }
////
////                                    golemEntity.removeGoal(priority);
                                    return 0;
                                }
                        ));
    }
}