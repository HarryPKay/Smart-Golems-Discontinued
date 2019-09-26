package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

public class BreakBlock {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("break-block")
                .then(Commands.argument("position", BlockPosArgument.blockPos())
                        .executes(ctx -> {
                                    if (ctx.getSource().getWorld().isRemote()) {
                                        return 0;
                                    }

                                    BlockPos blockPos = BlockPosArgument.getBlockPos(ctx, "position");
                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem"));
                                    if (golemEntity == null) {
                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                        return 0;
                                    }

                                    golemEntity.breakBlock(blockPos);

                                    return 1;
                                }
                        )
                );
    }
}
