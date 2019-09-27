package com.harrykay.smartgolems.server.command.subcommands;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import static com.harrykay.smartgolems.server.command.Constants.*;

public class ShiftPriorities {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(SHOW_GOALS_COMMAND)
                .then(Commands.argument(SHIFT_PRIORITY_AMOUNT_ARG, IntegerArgumentType.integer(0, SmartGolemEntity.maxTasks))
                        .executes(ctx -> {

                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
                                                return 0;
                                            }

                            int amount = IntegerArgumentType.getInteger(ctx, SHIFT_PRIORITY_AMOUNT_ARG);

                                            golemEntity.shiftGoalPriority(amount);
                                            return 0;
                        }));
    }
}