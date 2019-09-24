package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class ShiftPriorities {

    private static final String ARG_1 = "shift-priorities";
    private static final String ARG_2 = "golem name";
    private static final String ARG_3 = "amount";

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(ARG_1)
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument(ARG_2, StringArgumentType.string())
                        .then(Commands.argument(ARG_3, IntegerArgumentType.integer(-SmartGolemEntity.maxTasks, SmartGolemEntity.maxTasks))
                                .executes(ctx -> {
                                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem name"));
                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                                return 0;
                                            }

                                            Integer amount = IntegerArgumentType.getInteger(ctx, ARG_3);
                                            golemEntity.shiftGoalPriority(amount);
                                            return 0;
                                        }
                                )));
    }
}