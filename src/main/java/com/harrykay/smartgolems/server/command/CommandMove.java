package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CommandMove {

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("move")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "name"));

                                    if (golemEntity == null) {
                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                        return 0;
                                    }

                                    golemEntity.getNavigator().clearPath();
                                    if (!golemEntity.getNavigator().tryMoveToEntityLiving(ctx.getSource().asPlayer(), 1D)) {
                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not set path."));
                                    }

                                    ctx.getSource().asPlayer().sendMessage(new StringTextComponent("set path."));

                                    return 0;
                                }
                        ));
    }
}
