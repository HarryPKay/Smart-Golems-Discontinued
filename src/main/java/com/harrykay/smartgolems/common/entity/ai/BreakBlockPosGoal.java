package com.harrykay.smartgolems.common.entity.ai;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Hand;

public class BreakBlockPosGoal extends Goal {
    private final SmartGolemEntity creature;
    private boolean isBlockDestroyed = false;
    private double minDistance;

    public BreakBlockPosGoal(SmartGolemEntity golem, double minDistance) {
        isBlockDestroyed = false;
        this.creature = golem;
        this.minDistance = minDistance;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        //TODO: check all blocks that should not be destroyed.
        if (this.creature.targetBlockPos == null) {
            return false;
        } else if (creature.world.getBlockState(creature.targetBlockPos).getBlock() == Blocks.AIR) {
            return false;
        } else {
            double euclideanDistance = Math.sqrt(Math.pow(creature.posX - creature.targetBlockPos.getX(), 2) + Math.pow(creature.posY - creature.targetBlockPos.getY(), 2) + Math.pow(creature.posZ - creature.targetBlockPos.getZ(), 2));
            return euclideanDistance < minDistance;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.creature.targetBlockPos != null && !isBlockDestroyed && creature.world.getBlockState(creature.targetBlockPos).getBlock() != Blocks.AIR && creature.posX == creature.targetBlockPos.getX() && creature.posY == creature.targetBlockPos.getY() && creature.posZ == creature.targetBlockPos.getZ();
        //return !this.creature.getNavigator().noPath() && this.player.isAlive() && creature.posX == creature.targetBlockPos.getX() && creature.posY == creature.targetBlockPos.getY() && creature.posZ == creature.targetBlockPos.getZ();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.creature.targetBlockPos = null;
        isBlockDestroyed = false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.creature.swingArm(Hand.MAIN_HAND);
        this.creature.world.destroyBlock(creature.targetBlockPos, false);
    }
}