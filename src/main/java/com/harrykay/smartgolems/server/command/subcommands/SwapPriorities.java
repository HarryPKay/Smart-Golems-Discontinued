package com.harrykay.smartgolems.server.command.subcommands;

import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import static com.harrykay.smartgolems.server.command.Constants.*;

public class SwapPriorities {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(SHOW_GOALS_COMMAND)
                .then(Commands.argument(PRIORITIY_FROM_ARG, IntegerArgumentType.integer(0, SmartGolemEntity.maxTasks))
                        .then(Commands.argument(PRIORITIY_WITH_ARG, IntegerArgumentType.integer(0, SmartGolemEntity.maxTasks))
                                .executes(ctx -> {

//                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
//                                    if (golemEntity == null) {
//                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
//                                        return 0;
//                                    }
//
//                                    int priority = IntegerArgumentType.getInteger(ctx, PRIORITIY_FROM_ARG);
//                                    int withPriority = IntegerArgumentType.getInteger(ctx, PRIORITIY_WITH_ARG);
//
//                                    golemEntity.swapGoal(priority, withPriority);
                                    return 0;
                                })));
    }
}