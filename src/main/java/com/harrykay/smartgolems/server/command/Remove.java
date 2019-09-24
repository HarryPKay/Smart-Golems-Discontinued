package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class Remove {

    static ArgumentBuilder<CommandSource, ?> register() {


        return Commands.literal("remove")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(ctx -> {

                                    if (!SmartGolems.removeGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "name"))) {
                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not remove that golem."));
                                    }
                                    return 0;
                                }
                        ));
    }
}
