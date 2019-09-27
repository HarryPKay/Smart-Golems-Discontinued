package com.harrykay.smartgolems.server.command.subcommands;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

import static com.harrykay.smartgolems.server.command.Constants.*;

public class PlaceBlock {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(PLACE_BLOCK_COMMAND)
                .then(Commands.argument(GOLEM_NAME_ARG, BlockPosArgument.blockPos())
                        .then(Commands.argument(BLOCK_ARG, BlockStateArgument.blockState())
                                .executes(ctx -> {
                                            if (ctx.getSource().getWorld().isRemote()) {
                                                return 0;
                                            }

                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, GOLEM_NAME_ARG));
                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent(GOLEM_NOT_FOUND));
                                                return 0;
                                            }

                                    BlockPos blockPos = BlockPosArgument.getBlockPos(ctx, POSITION_ARG);
                                    Block block = BlockStateArgument.getBlockState(ctx, BLOCK_ARG).getState().getBlock();
                                    golemEntity.PlaceBlock(blockPos, block);
                                            return 1;
                                        }
                                )
                        ));
    }
}
