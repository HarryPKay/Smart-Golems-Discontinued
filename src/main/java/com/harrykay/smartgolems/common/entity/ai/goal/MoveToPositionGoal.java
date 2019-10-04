package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.MathHelpers;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.PriorityQueue;

public class MoveToPositionGoal extends Goal {
    private final SmartGolemEntity creature;
    private final double speed;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double minDistance;
    public PriorityQueue<Action> positions = new PriorityQueue<>(new ActionComparator());
    private int priorityCounter = 0;

    public MoveToPositionGoal(SmartGolemEntity golem, double speedIn, double minDistance) {
        this.creature = golem;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.minDistance = minDistance;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {

        if (positions.isEmpty()) {
            return false;
        }

        BlockPos blockPos = positions.peek().blockPos;

        if (MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) < minDistance) {
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

        if (positions.isEmpty()) {
            return false;
        }

        if (MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), positions.peek().blockPos) < minDistance) {
            positions.poll();
            return false;
        }

        if (this.creature.getNavigator().noPath()) {
            positions.peek().priority++;
            return false;
        }

        return true;
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