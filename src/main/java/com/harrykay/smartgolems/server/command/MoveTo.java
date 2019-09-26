package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

public class MoveTo {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("move-to")
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
                                    golemEntity.moveTo(blockPos);
                                    return 1;
                                }
                        )
                ).then(Commands.argument("player", StringArgumentType.string())
                        .executes(ctx -> {

                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "golem"));
                            if (golemEntity == null) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                return 0;
                            }

                            //TODO: check that the player is also close
                            boolean found = false;
                            for (PlayerEntity player : ctx.getSource().getWorld().getPlayers()) {
                                if (player.getDisplayName().getString().equals(StringArgumentType.getString(ctx, "player"))) {
                                    golemEntity.moveTo(player);
                                    return 1;
                                }
                            }
                            ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find player"));
                            return 0;
                        }));
    }
}