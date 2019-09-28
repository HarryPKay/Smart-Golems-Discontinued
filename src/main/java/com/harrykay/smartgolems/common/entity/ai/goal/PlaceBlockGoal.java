package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.MathHelpers;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;



public class PlaceBlockGoal extends Goal {
    private final SmartGolemEntity creature;
    protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0D, 1D, 4D, 1D);
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
        } else if (this.creature.actions.peek().actionType != SmartGolemEntity.ActionType.PLACE_BLOCK) {
            return false;
        } else {

            BlockPos blockPos = creature.actions.peek().blockPos;

            AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(blockPos);
            if (!this.creature.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb).isEmpty()) {
                return false;
            }

            return !(MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) > minDistance);
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

        if (MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) > minDistance) {
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

        BlockPos blockPos = this.creature.actions.peek() != null ? this.creature.actions.peek().blockPos : null;
        if (blockPos == null) {
            return;
        }
        this.creature.getLookController().setLookPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 180, 180);
        if (this.creature.placeTimer > 0) {
            --this.creature.placeTimer;
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


//            this.creature.setActiveHand(Hand.MAIN_HAND);
//            this.creature.swingArm(Hand.MAIN_HAND);
            //this.creature.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);



            if (this.creature.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
                this.creature.world.destroyBlock(blockPos, false);
                if (!this.creature.world.getPlayers().isEmpty()) {
                    this.creature.attackEntityAsMob(this.creature.world.getPlayers().get(0));
                }
            }

            if (this.creature.world.setBlockState(blockPos, block.getDefaultState())) {
                System.out.println("abc: " + MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos));
                this.creature.addVelocity(0, 0.5, 0);
                this.creature.actions.poll();
                if (!this.creature.world.getPlayers().isEmpty()) {
                    this.creature.attackEntityAsMob(this.creature.world.getPlayers().get(0));
                }
            }
        }
    }
}