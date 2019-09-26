package com.harrykay.smartgolems.server.command.selector;

import com.harrykay.smartgolems.server.command.BreakBlock;
import com.harrykay.smartgolems.server.command.Follow;
import com.harrykay.smartgolems.server.command.Forget;
import com.harrykay.smartgolems.server.command.MoveTo;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class Golem {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("golem")
                .then(Commands.argument("golem", StringArgumentType.string())
                        .executes(ctx -> 1
                        ).then(MoveTo.register())
                        .then(Follow.register())
                        .then(Forget.register())
                        .then(Forget.register())
                        .then(BreakBlock.register()));
    }
}
