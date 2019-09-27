package com.harrykay.smartgolems.server.command.selector;

import com.harrykay.smartgolems.server.command.subcommands.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import static com.harrykay.smartgolems.server.command.Constants.GOLEM_COMMAND;
import static com.harrykay.smartgolems.server.command.Constants.GOLEM_NAME_ARG;

public class Golem {


    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(GOLEM_COMMAND)
                .then(Commands.argument(GOLEM_NAME_ARG, StringArgumentType.string())
                        .executes(ctx -> 1)
                        .then(MoveTo.register())
                        .then(Follow.register())
                        .then(Forget.register())
                        .then(Halt.register())
                        .then(PlaceBlock.register())
                        .then(DoIronGolemThings.register())
                        .then(Build.register())
                        .then(ShowGoals.register())
                        .then(Suicide.register())
                        .then(DoCreeperThings.register())
                        .then(SwapPriorities.register())
                        .then(ShiftPriorities.register())

                );
    }
}
