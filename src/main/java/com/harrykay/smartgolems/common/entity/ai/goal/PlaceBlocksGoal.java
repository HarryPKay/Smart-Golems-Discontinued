package com.harrykay.smartgolems.common.entity.ai.goal;

import com.harrykay.smartgolems.MathHelpers;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import com.harrykay.smartgolems.pathfinding.Move;
import com.harrykay.smartgolems.pathfinding.Position;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.PriorityQueue;

//TODO; have move as part of this function:
public class PlaceBlocksGoal extends Goal {

    protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0D, 1D, 1D, 1D);
    private final SmartGolemEntity creature;
    public PriorityQueue<Action> actions = new PriorityQueue<>();
    public ArrayList<Move.Directions> directions = new ArrayList<Move.Directions>();
    private double minPlaceDistance;
    private double speed;
    private int placeTimer;
    private double movePosX;
    private double movePosY;
    private double movePosZ;

    public PlaceBlocksGoal(SmartGolemEntity golem, double speed, float minPlaceDistance) {

        directions.addAll(Arrays.asList(Move.Directions.values()));
        this.creature = golem;
        this.minPlaceDistance = minPlaceDistance;
        this.speed = speed;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {

        if (actions.isEmpty()) {
            return false;
        }

        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(this.actions.peek().blockPos);
        if (!this.creature.world.getEntitiesWithinAABBExcludingEntity(this.creature.getEntity(), axisalignedbb).isEmpty()) {
            return false;
        }

        for (int i = 0; i < Move.offsets.size(); ++i) {
            creature.getNavigator().clearPath();
            Position position = Move.offsets.get(directions.get(i));
            if (this.creature.getNavigator().canEntityStandOnPos(new BlockPos(
                    actions.peek().blockPos.getX() + position.x,
                    actions.peek().blockPos.getY() + position.y,
                    actions.peek().blockPos.getZ() + position.z))) {
                movePosX = actions.peek().blockPos.getX() + position.x;
                movePosY = actions.peek().blockPos.getY() + position.y;
                movePosZ = actions.peek().blockPos.getZ() + position.z;
                this.creature.getNavigator().tryMoveToXYZ(
                        movePosX,
                        movePosY,
                        movePosZ,
                        speed
                );
                return true;
            }
        }

        // else find a scaffold solution

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (actions.isEmpty()) {
            return false;
        }

        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(this.actions.peek().blockPos);
        if (!this.creature.world.getEntitiesWithinAABBExcludingEntity(this.creature.getEntity(), axisalignedbb).isEmpty()) {
            return false;
        }

        if (MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), this.actions.peek().blockPos) < minPlaceDistance) {
            return true;
        }

        for (int i = 0; i < Move.offsets.size(); ++i) {
            creature.getNavigator().clearPath();
            Position position = Move.offsets.get(directions.get(i));
            if (this.creature.getNavigator().canEntityStandOnPos(new BlockPos(
                    actions.peek().blockPos.getX() + position.x,
                    actions.peek().blockPos.getY() + position.y,
                    actions.peek().blockPos.getZ() + position.z))) {
                movePosX = actions.peek().blockPos.getX() + position.x;
                movePosY = actions.peek().blockPos.getY() + position.y;
                movePosZ = actions.peek().blockPos.getZ() + position.z;
                this.creature.getNavigator().tryMoveToXYZ(
                        movePosX,
                        movePosY,
                        movePosZ,
                        speed
                );
                return true;
            }
        }

        return false;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {

    }

    public void placeAnimation() {
        if (!this.creature.world.getPlayers().isEmpty()) {
            this.creature.attackEntityAsMob(this.creature.world.getPlayers().get(0));
        }
    }

    public void tick() {

        if (actions.isEmpty()) {
            return;
        }
        BlockPos blockPos = actions.peek().blockPos;

        if (MathHelpers.euclideanDistance(new BlockPos(creature.posX, creature.posY, creature.posZ), blockPos) <= minPlaceDistance) {

            this.creature.getLookController().setLookPosition(
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ(),
                    270,
                    270);

            if (this.placeTimer > 0) {
                --this.placeTimer;
                return;
            }
            this.placeTimer = 10;

            // Check placement location empty
            AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(blockPos);
            //this.creature.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb).isEmpty()
            if (this.creature.world.getEntitiesWithinAABBExcludingEntity(this.creature.getEntity(), axisalignedbb).isEmpty()) {
                // TODO: Check against a list of blocks that shouldn't be destroyed.
                if (this.creature.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
                    this.creature.world.destroyBlock(blockPos, false);
                    // Apply animation.
                    placeAnimation();

                } else if (this.creature.world.setBlockState(blockPos, actions.peek().block.getDefaultState())) {
                    // Apply rotation
                    if (actions.peek().rotation != null) {
                        this.creature.world.setBlockState(
                                blockPos,
                                this.creature.world.getBlockState(blockPos).rotate(this.creature.world,
                                        blockPos,
                                        actions.peek().rotation).getBlockState());

                    }
                    actions.poll();
                    this.creature.addVelocity(0, 0.4, 0);
                    placeAnimation();
                    return;
                }
            }
        }
        actions.peek().priority++;
    }
}