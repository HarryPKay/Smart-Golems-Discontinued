package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.MathHelpers;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import static java.lang.Math.pow;

public class PlaceBlockGoal extends Goal {
    private final SmartGolemEntity creature;
    protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0D, 1D, 3D, 1D);
    private double minDistance;

    public PlaceBlockGoal(SmartGolemEntity golem, double minDistance) {
        this.creature = golem;
        this.minDistance = minDistance;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.creature.actions.isEmpty()) {
            return false;
        } else if (this.creature.actions.peek().block == null || this.creature.actions.peek().blockPos == null) {
            return false;
        } else {

            BlockPos blockPos = creature.actions.peek().blockPos;

            AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(blockPos);
            if (!this.creature.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb).isEmpty()) {
                return false;
            }

            return !(MathHelpers.euclideanDistanceSq(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) > pow(minDistance, 2));
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
            return false;
        }

        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(blockPos);
        return this.creature.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb).isEmpty();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        //this.creature.placeTimer = 3;
    }

    public void tick() {
        if (this.creature.placeTimer > 0) {
            --this.creature.placeTimer;
            return;
        }

        BlockPos blockPos = this.creature.actions.peek() != null ? this.creature.actions.peek().blockPos : null;
        if (blockPos == null) {
            return;
        }
        Block block = this.creature.actions.peek() != null ? this.creature.actions.peek().block : null;
        if (block == null) {
            return;
        }

        this.creature.placeTimer = 10;

        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(blockPos);
        //TODO check if destroyable
        if (this.creature.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb).isEmpty()) {
            if (this.creature.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
                this.creature.world.destroyBlock(blockPos, false);
            }

            if (this.creature.world.setBlockState(blockPos, block.getDefaultState())) {
                this.creature.actions.poll();
            }
        }
    }
}