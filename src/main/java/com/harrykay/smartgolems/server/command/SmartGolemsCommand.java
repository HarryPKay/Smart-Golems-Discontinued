package com.harrykay.smartgolems.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class SmartGolemsCommand {
    static final String MOD_ID = "smartgolems";

    public SmartGolemsCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal(MOD_ID)
                        .then(CommandSpawn.register())
                        .then(CommandRemoveAll.register())
                        .then(CommandRemove.register())
                        .then(CommandMove.register())
        );
    }
}
