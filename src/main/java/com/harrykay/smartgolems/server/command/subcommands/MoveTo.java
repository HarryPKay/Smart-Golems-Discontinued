package com.harrykay.smartgolems.server.command.subcommands;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

import static com.harrykay.smartgolems.server.command.Constants.*;

public class MoveTo {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(MOVE_TO_COMMAND)
                .then(Commands.argument(POSITION_ARG, BlockPosArgument.blockPos())
                        .executes(ctx -> {
                                    if (ctx.getSource().getWorld().isRemote()) {
                                        return 0;
                                    }

                            BlockPos blockPos = BlockPosArgument.getBlockPos(ctx, POSITION_ARG);
                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
                                    if (golemEntity == null) {
                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
                                        return 0;
                                    }
                                    golemEntity.moveTo(blockPos);
                                    return 1;
                                }
                        )
                ).then(Commands.argument(PLAYER_NAME_ARG, StringArgumentType.string())
                        .executes(ctx -> {

                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
                            if (golemEntity == null) {
                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
                                return 0;
                            }

                            //TODO: check that the player is also close
                            boolean found = false;
                            for (PlayerEntity player : ctx.getSource().getWorld().getPlayers()) {
                                if (player.getDisplayName().getString().equals(StringArgumentType.getString(ctx, PLAYER_NAME_ARG))) {
                                    golemEntity.moveTo(player);
                                    return 1;
                                }
                            }
                            ctx.getSource().asPlayer().sendMessage(new StringTextComponent(PLAYER_NOT_FOUND));
                            return 0;
                        }));
    }
}