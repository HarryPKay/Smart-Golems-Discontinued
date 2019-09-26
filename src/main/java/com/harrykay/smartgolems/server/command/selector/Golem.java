package com.harrykay.smartgolems.server.command.selector;

import com.harrykay.smartgolems.server.command.*;
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
                        .then(BreakBlock.register())
                        .then(ActLikeIronGolems.register())
                );
    }
}
