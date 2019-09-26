package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class GetGoals {

    private static final String ARG_1 = "show-assigned-tasks";
    private static final String ARG_2 = "golem name";

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(ARG_1)
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument(ARG_2, StringArgumentType.string())
                        .executes(ctx -> {

                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, ARG_2));

                            if (golemEntity == null) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                return 0;
                            }

                            ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Priority:task"));
                            for (Integer priority : golemEntity.taskNameByPriority.keySet()) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(priority + ":" + golemEntity.taskNameByPriority.get(priority)));
                            }

                            return 0;
                        }));
    }
}