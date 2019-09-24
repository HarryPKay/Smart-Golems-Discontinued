package com.harrykay.smartgolems.common.entity.ai;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class MoveTowardsPlayerGoal extends Goal {
    private final SmartGolemEntity creature;
    private final double speed;
    private final float maxTargetDistance;
    private final float minDistance;
    private PlayerEntity player;
    private double movePosX;
    private double movePosY;
    private double movePosZ;

    public MoveTowardsPlayerGoal(SmartGolemEntity golem, double speedIn, float targetMaxDistance, float minDistance) {
        this.creature = golem;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.minDistance = minDistance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        this.player = this.creature.focusedPlayer;
        if (this.player == null) {
            return false;
        } else if (this.player.getDistanceSq(this.creature) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
            return false;
        } else if (this.player.getDistanceSq(this.creature) < (double) (this.minDistance * this.minDistance)) {
            return false; // Is too close to follow.
        } else {
//            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 3, 3, new Vec3d(this.player.posX, this.player.posY, this.player.posZ));
//            if (vec3d == null) {
//                return false;
//            } else {
//                this.movePosX = vec3d.x;
//                this.movePosY = vec3d.y;
//                this.movePosZ = vec3d.z;
//                return true;
//            }
            this.movePosX = this.player.posX + 1;
            this.movePosY = this.player.posY;
            this.movePosZ = this.player.posZ + 1;
                return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath() && this.player.isAlive() && this.player.getDistanceSq(this.creature) < (double) (this.maxTargetDistance * this.maxTargetDistance);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.player = null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }
}