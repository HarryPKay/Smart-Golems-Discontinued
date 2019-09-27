package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class Build {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("build")
                .executes(ctx -> {
                            if (ctx.getSource().getWorld().isRemote()) {
                                return 0;
                            }

                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem"));
                            if (golemEntity == null) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                return 0;
                            }

                            golemEntity.buildHouse();
                            return 1;
                        }
                );
    }
}