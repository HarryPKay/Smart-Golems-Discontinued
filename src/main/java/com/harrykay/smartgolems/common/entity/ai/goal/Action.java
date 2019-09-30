package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
public class Action {
    public int priority;

    public SmartGolemEntity.ActionType actionType;

    public BlockPos blockPos = null;

    public Block block = null;

    public Rotation rotation = null;

    public Direction direction = null;

    public Action(int priority, SmartGolemEntity.ActionType actionType, BlockPos blockPos, @Nullable Block block, @Nullable Rotation rotation, @Nullable Direction direction) {

        this.priority = priority;

        this.actionType = actionType;

        this.blockPos = blockPos;

        this.block = block;

        this.rotation = rotation;

        this.direction = direction;

    }
}
