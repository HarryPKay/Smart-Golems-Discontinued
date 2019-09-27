package com.harrykay.smartgolems.common.entity.ai.goal;

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
    private boolean shouldStay;

    public MoveToBlockPosGoal(SmartGolemEntity golem, double speedIn, double minDistance) {
        this.creature = golem;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.minDistance = minDistance;
    }

    public double euclideanDistanceSq(BlockPos blockPos) {
        return pow(creature.posX - blockPos.getX(), 2) + pow(creature.posY - blockPos.getY(), 2) + pow(creature.posZ - blockPos.getZ(), 2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        System.out.println("abc: 30");
        if (this.creature.actions.isEmpty()) {
            System.out.println("abc: 31");
            return false;
        }

        BlockPos blockPos = creature.actions.peek().blockPos;
        if (blockPos == null) {
            System.out.println("abc: 32");
            return false;
        }


        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

//        SmartGolemEntity.ActionType actionType = creature.actions.peek().actionType;
//        if (actionType == PLACE_BLOCK)
//        {
//            double euclideanDistance
//        }


        //double euclideanDistanceXYZ = Math.sqrt(Math.pow(creature.posX - x, 2) + Math.pow(creature.posY - y, 2) + Math.pow(creature.posZ - z, 2));

        System.out.println("shouldExecute " + euclideanDistanceSq(blockPos));
        if (euclideanDistanceSq(blockPos) <= pow(minDistance, 2)) {
            System.out.println("abc: 33");

            return false;
        }

        this.movePosX = x;
        this.movePosY = y;
        this.movePosZ = z;

        System.out.println("abc: 34");
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {

        if (this.creature.actions.isEmpty()) {
            System.out.println("abc: 35");
            return false;
        } else if (this.creature.getNavigator().noPath()) {
            System.out.println("abc: 35");
            return false;
        }

        BlockPos blockPos = creature.actions.peek() != null ? creature.actions.peek().blockPos : null;
        if (blockPos == null) {
            System.out.println("abc: 36");
            return false;
        }

//        double x = blockPos.getX();
//        double y = blockPos.getY();
//        double z = blockPos.getZ();
        //double euclideanDistanceXYZ = Math.sqrt(Math.pow(creature.posX - x, 2) + Math.pow(creature.posY - y, 2) + Math.pow(creature.posZ - z, 2));
        System.out.println("cont " + euclideanDistanceSq(blockPos));
        SmartGolemEntity.ActionType actionType = creature.actions.peek() != null ? creature.actions.peek().actionType : null;
        if (actionType == null) {
            System.out.println("abc: 37");
            return false;
        }

        if (euclideanDistanceSq(blockPos) <= pow(minDistance, 2)) {
            System.out.println("abc: 50");
            return true;
        }
        System.out.println("abc: 61");

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