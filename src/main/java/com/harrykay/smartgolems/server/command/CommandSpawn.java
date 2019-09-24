package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandSpawn {

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("spawn")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .executes(ctx -> {
                    SmartGolems.createIntelGolem(ctx.getSource().asPlayer());
                    return 0;
                });
    }
}