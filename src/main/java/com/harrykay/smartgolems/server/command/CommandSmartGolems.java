package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class CommandSmartGolems {
    static final String COMMAND_NAME = "smartgolems";

    public CommandSmartGolems(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal(COMMAND_NAME)
                        .then(Spawn.register())
                        .then(RemoveAllGolems.register())
                        .then(RemoveGolem.register())
                        .then(Follow.register())
                        .then(PlaceBlock.register())
                        .then(MoveGolem.register())
                        .then(Halt.register())
                        .then(ShowAssignedTasks.register())
                        .then(MoveTask.register())
                        .then(SwapTasks.register())
                        .then(ShiftPriorities.register())
                        .then(RemoveTask.register())
                        .then(ShowAvailableTasks.register())
                        .then(SetPlayerFocus.register())
        );


        for (SmartGolemEntity.SurvivalSupportedTasks supportedTask : SmartGolemEntity.SurvivalSupportedTasks.values()) {
            dispatcher.register(
                    LiteralArgumentBuilder.<CommandSource>literal(COMMAND_NAME)
                            .then(insertTask.register(supportedTask)));
        }

        for (SmartGolemEntity.SurvivalSupportedTasks supportedTask : SmartGolemEntity.SurvivalSupportedTasks.values()) {
            dispatcher.register(
                    LiteralArgumentBuilder.<CommandSource>literal(COMMAND_NAME)
                            .then(AddTaskEnd.register(supportedTask)));
        }

        for (SmartGolemEntity.SurvivalSupportedTasks supportedTask : SmartGolemEntity.SurvivalSupportedTasks.values()) {
            dispatcher.register(
                    LiteralArgumentBuilder.<CommandSource>literal(COMMAND_NAME)
                            .then(AddTaskBegin.register(supportedTask)));
        }
    }
}
