package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class ExplodeNearPlayerGoal extends Goal {
    private final SmartGolemEntity creature;
    private final double speed;
    private final float maxTargetDistance;
    private final float minDistance;
    private PlayerEntity playerEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;

    public ExplodeNearPlayerGoal(SmartGolemEntity golem, double speedIn, float targetMaxDistance, float minDistance, PlayerEntity playerEntity) {
        this.creature = golem;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.minDistance = minDistance;
        this.playerEntity = playerEntity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.playerEntity == null) {
            return false;
        } else if (this.playerEntity.getDistanceSq(this.creature) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
            return false;
        } else {
            this.movePosX = this.playerEntity.posX;
            this.movePosY = this.playerEntity.posY;
            this.movePosZ = this.playerEntity.posZ;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath()
                && this.playerEntity.isAlive()
                && this.playerEntity.getDistanceSq(this.creature) < (double) (this.maxTargetDistance * this.maxTargetDistance);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.playerEntity = null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {

        if (this.playerEntity.getDistanceSq(this.creature) <= (double) (minDistance * minDistance)) {
            this.creature.explode();
        }

        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }
}