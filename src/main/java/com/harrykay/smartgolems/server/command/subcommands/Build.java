package com.harrykay.smartgolems.server.command.subcommands;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import static com.harrykay.smartgolems.server.command.Constants.*;

public class Build {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(BUILD_COMMAND)
                .executes(ctx -> {
                            if (ctx.getSource().getWorld().isRemote()) {
                                return 0;
                            }

                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
                            if (golemEntity == null) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
                                return 0;
                            }

                            golemEntity.buildHouse();
                            return 1;
                        }
                );
    }
}