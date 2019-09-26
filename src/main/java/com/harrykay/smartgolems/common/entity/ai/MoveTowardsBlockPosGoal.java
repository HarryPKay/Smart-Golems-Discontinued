package com.harrykay.smartgolems.common.entity.ai;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MoveTowardsBlockPosGoal extends Goal {
    private final SmartGolemEntity creature;
    private final double speed;
    private double movePosX;
    private double movePosY;
    private double movePosZ;

    public MoveTowardsBlockPosGoal(SmartGolemEntity golem, double speedIn) {
        this.creature = golem;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.creature.targetBlockPos == null) {
            return false;
        } else if (creature.posX == creature.targetBlockPos.getX() && creature.posY == creature.targetBlockPos.getY() && creature.posZ == creature.targetBlockPos.getZ()) {
            return false;
        } else {
            this.movePosX = creature.targetBlockPos.getX();
            this.movePosY = creature.targetBlockPos.getY();
            this.movePosZ = creature.targetBlockPos.getZ();
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return creature.targetBlockPos != null && !this.creature.getNavigator().noPath() && creature.posX != creature.targetBlockPos.getX() && creature.posY != creature.targetBlockPos.getY() && creature.posZ != creature.targetBlockPos.getZ();
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