package com.harrykay.smartgolems.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class CommandRegister {
    static final String MOD_ID = "smartgolems";

    public CommandRegister(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal(MOD_ID)
                        .then(Spawn.register())
                        .then(RemoveAll.register())
                        .then(Remove.register())
                        .then(Follow.register())
                        .then(PlaceBlock.register())
                        .then(Move.register())
                        .then(Move.register())
        );
    }
}
