package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.MathHelpers;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

import static java.lang.Math.pow;

public class MoveToBlockPosGoal extends Goal {
    private final SmartGolemEntity creature;
    private final double speed;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double minDistance;


    public MoveToBlockPosGoal(SmartGolemEntity golem, double speedIn, double minDistance) {
        this.creature = golem;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.minDistance = minDistance;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.creature.actions.isEmpty()) {
            return false;
        }

        BlockPos blockPos = creature.actions.peek().blockPos;
        if (blockPos == null) {
            return false;
        }

        if (MathHelpers.euclideanDistanceSq(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) < pow(minDistance, 2)) {
            return false;
        }

        this.movePosX = blockPos.getX();
        this.movePosY = blockPos.getY();
        this.movePosZ = blockPos.getZ();

        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {

        if (this.creature.actions.isEmpty()) {
            return false;
        } else return !this.creature.getNavigator().noPath();

//        BlockPos blockPos = creature.actions.peek() != null ? creature.actions.peek().blockPos : null;
//        if (blockPos == null) {
//            return false;
//        }
//
//        SmartGolemEntity.ActionType actionType = creature.actions.peek() != null ? creature.actions.peek().actionType : null;
//        if (actionType == null) {
//            return false;
//        }
//
//        if (MathHelpers.euclideanDistanceSq(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) < pow(minDistance, 2)) {
//            return false;
//        }

    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {

    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }
}