package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.MathHelpers;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

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

        if (MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) < minDistance) {
            return false;
        }

        this.movePosX = blockPos.getX();
        this.movePosY = blockPos.getY();
        this.movePosZ = blockPos.getZ();

        if (creature.actions.peek().actionType == SmartGolemEntity.ActionType.MOVE_TO) {
            creature.actions.poll();
        }

        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {

        if (this.creature.actions.isEmpty()) {
            return false;
        }

        return !this.creature.getNavigator().noPath();
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