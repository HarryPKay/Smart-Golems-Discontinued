package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class Halt {

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("halt")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument("golem name", StringArgumentType.string())
                        .executes(ctx -> {

                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem name"));

                            if (golemEntity == null) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                return 0;
                            }

                            golemEntity.removeAllGoals();

                            return 0;
                        }));
    }
}
