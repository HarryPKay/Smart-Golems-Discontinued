package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class AddTaskBegin {

    public static final String SUB_COMMAND = "add-task-begin";
    public static final String GOLEM_NAME = "golem name";
    public static final String PRIORITY = "priority";

    // Multiple register functions with enum that holds same type arguments


    static ArgumentBuilder<CommandSource, ?> register(SmartGolemEntity.SurvivalSupportedTasks supportedTasks) {
        return Commands.literal(SUB_COMMAND)
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.literal(supportedTasks.name())
                        .requires(cs -> cs.hasPermissionLevel(2))
                        .then(Commands.argument(GOLEM_NAME, StringArgumentType.string())
                                .executes(ctx -> {
                                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem name"));

                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                                return 0;
                                            }
                                            golemEntity.shiftGoalPriority(1);
                                            golemEntity.insertGoal(0, supportedTasks);
                                            return 0;
                                        }
                                )));
    }
}
