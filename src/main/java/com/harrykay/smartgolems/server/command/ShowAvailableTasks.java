package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity.SupportedTasks;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class ShowAvailableTasks {

    private static final String ARG_1 = "show-available-tasks";
    //private static final String ARG_2 = "golem name";

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(ARG_1)
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .executes(ctx -> {

                    for (SupportedTasks supportedTask : SupportedTasks.values()) {
                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent(supportedTask.name()));
                    }

                    return 0;
                });
    }
}