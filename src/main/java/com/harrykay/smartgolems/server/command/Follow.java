package com.harrykay.smartgolems.server.command;

import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.harrykay.smartgolems.common.entity.ai.MoveTowardsPlayerGoal;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class Follow {

    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("follow")
                .requires(cs -> cs.hasPermissionLevel(2)) //permission
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                                    SmartGolemEntity golemEntity = SmartGolems.getGolem(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "name"));

                                    if (golemEntity == null) {
                                        ctx.getSource().asPlayer().sendMessage(new StringTextComponent("Could not find that golem."));
                                        return 0;
                                    }

                            golemEntity.focusedPlayer = ctx.getSource().asPlayer();
                            golemEntity.goalSelector.addGoal(0, new MoveTowardsPlayerGoal(golemEntity, 0.6D, 32.0F, 2.0F));
                            golemEntity.goalSelector.addGoal(1, new LookAtGoal(golemEntity, PlayerEntity.class, 6.0F));
                            //golemEntity.goalSelector.removeGoal(golemEntity.goalSelector.getRunningGoals().findFirst().get());
                                    return 0;
                                }
                        ));
    }
}
