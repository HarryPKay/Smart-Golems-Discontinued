package com.harrykay.smartgolems.server.command.subcommands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import static com.harrykay.smartgolems.server.command.Constants.SHOW_GOALS_COMMAND;

public class ShowGoals {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(SHOW_GOALS_COMMAND)
                        .executes(ctx -> {

//                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
//
//                            if (golemEntity == null) {
//                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
//                                return 0;
//                            }
//
//                            // TODO: Display using a better strategy.
//                            ctx.getSource().asPlayer().sendMessage(new StringTextComponent(PRIORITY_TO_GOAL_KEY));
//                            for (Integer priority : golemEntity.taskNameByPriority.keySet()) {
//                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(priority + ":" + golemEntity.taskNameByPriority.get(priority)));
//                            }

                            return 0;
                        });
    }
}