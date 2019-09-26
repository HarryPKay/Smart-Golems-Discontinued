package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RemoveAllGolems {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("remove-all-golems")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .executes(ctx -> {

                    if (ctx.getSource().getWorld().isRemote) {
                        return 0;
                    }

                    SmartGolems.removeAllGolems(ctx.getSource().asPlayer());
                    return 0;
                });
    }
}