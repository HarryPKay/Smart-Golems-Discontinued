package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import static java.lang.Math.pow;

public class PlaceBlockGoal extends Goal {
    private final SmartGolemEntity creature;
    private double minDistanceXZ;
    private double minDistanceY;
    private int placeTimer;

    public PlaceBlockGoal(SmartGolemEntity golem, double minDistanceXZ, double minDistanceY) {
        this.creature = golem;
        this.minDistanceXZ = minDistanceXZ;
        this.minDistanceY = minDistanceY;
    }

    public double euclideanDistanceSq(BlockPos blockPos) {
        return pow(creature.posX - blockPos.getX(), 2) + pow(creature.posY - blockPos.getY(), 2) + pow(creature.posZ - blockPos.getZ(), 2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        System.out.println("abc");
        if (this.creature.actions.isEmpty()) {
            System.out.println("abc: 11");
            return false;
        } else if (this.creature.actions.peek().block == null || this.creature.actions.peek().blockPos == null) {
            System.out.println("abc: 12");
            return false;
        } else {

            BlockPos blockPos = creature.actions.peek().blockPos;
            System.out.println("abc: 13");
            double x = blockPos.getX();
            double y = blockPos.getY();
            double z = blockPos.getZ();
            double euclideanDistanceXZ = Math.sqrt(pow(creature.posX - x, 2) + pow(creature.posZ - z, 2));

            return !(euclideanDistanceSq(blockPos) > pow(minDistanceXZ, 2));

//            if (euclideanDistanceXZ <= minDistanceXZ)
//            {
//                System.out.println("abc: 14");
//                double distanceY =  Math.abs(creature.posY - y);
//                return distanceY <= minDistanceY;
//            }
        }

    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {

        if (this.creature.actions.isEmpty()) {
            return false;
        }

        BlockPos blockPos = this.creature.actions.peek() != null ? this.creature.actions.peek().blockPos : null;
        if (blockPos == null) {
            System.out.println("abc: 4");
            return false;
        }
//        double x = blockPos.getX();
//        double y = blockPos.getY();
//        double z = blockPos.getZ();
        //double euclideanDistanceXZ = Math.sqrt(pow(creature.posX - x, 2) + pow(creature.posZ - z, 2));

        if (euclideanDistanceSq(blockPos) > minDistanceXZ) {
            System.out.println("abc: 6");
            return false;
        }
//        double distanceY =  Math.abs(creature.posY - y);
//        if (distanceY > minDistanceY)
//        {
//            System.out.println("abc: 7");
//            return false;
//        }

        System.out.println("abc: 62");
        return true;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        placeTimer = 10;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {

//        if (placeTimer > 0)
//        {
//            placeTimer--;
//            return;
//        }
//        placeTimer = 10;


        BlockPos blockPos = this.creature.actions.peek() != null ? this.creature.actions.peek().blockPos : null;
        if (blockPos == null) {
            System.out.println("abc: 4");
            return;
        }
        Block block = this.creature.actions.peek() != null ? this.creature.actions.peek().block : null;
        if (block == null) {
            System.out.println("abc: 3");
            return;
        }

        if (this.creature.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            System.out.println("abc: 2");
            this.creature.world.destroyBlock(blockPos, false);
        }

        if (this.creature.world.setBlockState(blockPos, block.getDefaultState())) {
            System.out.println("abc: 1");
            this.creature.actions.poll();
        }
    }
}