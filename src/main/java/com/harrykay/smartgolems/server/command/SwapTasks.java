package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class SwapTasks {

    private static final String ARG_1 = "swap-tasks";
    private static final String ARG_2 = "golem name";
    private static final String ARG_3 = "prioritiy no";
    private static final String ARG_4 = "with no";

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(ARG_1)
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument(ARG_2, StringArgumentType.string())
                        .then(Commands.argument(ARG_3, IntegerArgumentType.integer(0, SmartGolemEntity.maxTasks))
                                .then(Commands.argument(ARG_4, IntegerArgumentType.integer(0, SmartGolemEntity.maxTasks))
                                        .executes(ctx -> {

                                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, ARG_2));
                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                                return 0;
                                            }

                                            Integer priority = IntegerArgumentType.getInteger(ctx, ARG_3);
                                            if (!golemEntity.taskNameByPriority.containsKey(priority)) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("No task with that priority exists."));
                                                return 0;
                                            }

                                            Integer withPriority = IntegerArgumentType.getInteger(ctx, ARG_4);
//                                            if (priority < 0 || priority > SmartGolemEntity.maxTasks || withPriority < 0 || withPriority > SmartGolemEntity.maxTasks)
//                                            {
//                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Invalid priority range (valid range: 0-" + SmartGolemEntity.maxTasks + " inclusive)."));
//                                                return 0;
//                                            }

                                            golemEntity.swapGoal(priority, withPriority);
                                            return 0;
                                        }))));
    }
}