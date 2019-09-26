package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.server.command.selector.Golem;
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
                        .then(Golem.register())
        );

    }
}
