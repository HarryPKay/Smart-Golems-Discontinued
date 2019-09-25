package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class SetPlayerFocus {

    public static final String SUB_COMMAND = "player-focus";
    public static final String ARG_1 = "golem name";
    public static final String ARG_2 = "player";

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal(SUB_COMMAND)
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument(ARG_1, StringArgumentType.string())
                        .then(Commands.argument(ARG_2, StringArgumentType.string())
                                .executes(ctx -> {

                                            if (ctx.getSource().getWorld().isRemote) {
                                                return 0;
                                            }

                                            SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, ARG_1));
                                            if (golemEntity == null) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                                return 0;
                                            }

                                            if (StringArgumentType.getString(ctx, ARG_2) == null || StringArgumentType.getString(ctx, ARG_2).equals("")) {
                                                golemEntity.focusedPlayer = null;
                                                return 0;
                                            }

                                            //TODO: check that the player is also close
                                            boolean found = false;
                                            for (PlayerEntity player : ctx.getSource().getWorld().getPlayers()) {
                                                if (player.getDisplayName().getString().equals(StringArgumentType.getString(ctx, ARG_2))) {
                                                    found = true;
                                                    golemEntity.focusedPlayer = player;
                                                    return 0;
                                                }
                                            }

                                            if (!found) {
                                                ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find player"));
                                            }

                                            return 0;
                                        }
                                )));
    }
}
