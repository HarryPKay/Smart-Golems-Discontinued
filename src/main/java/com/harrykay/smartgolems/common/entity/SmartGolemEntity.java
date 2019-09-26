package com.harrykay.smartgolems.common.entity;

import com.harrykay.smartgolems.common.entity.ai.BreakBlockPosGoal;
import com.harrykay.smartgolems.common.entity.ai.MoveTowardsBlockPosGoal;
import com.harrykay.smartgolems.common.entity.ai.MoveTowardsPlayerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

// follow owner goal
public class SmartGolemEntity extends IronGolemEntity {

    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);
    }

    public BlockPos targetBlockPos = null;

    public static void actLikeIronGolems(SmartGolemEntity smartGolemEntity) {
        smartGolemEntity.shiftGoalPriority(SmartGolemEntity.maxTasks);


        smartGolemEntity.goalSelector.addGoal(1, new MeleeAttackGoal(smartGolemEntity, 1.0D, true));
        smartGolemEntity.goalSelector.addGoal(2, new MoveTowardsTargetGoal(smartGolemEntity, 0.9D, 32.0F));
        smartGolemEntity.goalSelector.addGoal(2, new MoveTowardsVillageGoal(smartGolemEntity, 0.6D));
        smartGolemEntity.goalSelector.addGoal(3, new MoveThroughVillageGoal(smartGolemEntity, 0.6D, false, 4, () -> {
            return false;
        }));
        smartGolemEntity.goalSelector.addGoal(5, new ShowVillagerFlowerGoal(smartGolemEntity));
        smartGolemEntity.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(smartGolemEntity, 0.6D));
        smartGolemEntity.goalSelector.addGoal(7, new LookAtGoal(smartGolemEntity, PlayerEntity.class, 6.0F));
        smartGolemEntity.goalSelector.addGoal(8, new LookRandomlyGoal(smartGolemEntity));
        smartGolemEntity.targetSelector.addGoal(1, new DefendVillageTargetGoal(smartGolemEntity));
        smartGolemEntity.targetSelector.addGoal(2, new HurtByTargetGoal(smartGolemEntity));
        smartGolemEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(smartGolemEntity, MobEntity.class, 5, false, false, (p_213619_0_) -> {
            return p_213619_0_ instanceof IMob && !(p_213619_0_ instanceof CreeperEntity);
        }));
    }

    public void follow(PlayerEntity playerEntity) {
        focusedPlayer = playerEntity;
        shiftGoalPriority(2);
        insertGoal(1, new MoveTowardsPlayerGoal(this, 0.6D, 32.0F, 2.0F), "follow " + playerEntity.getDisplayName().getString());
        insertGoal(2, new LookAtGoal(this, PlayerEntity.class, 6.0F), "look at " + playerEntity.getDisplayName().getString());
    }

    public void moveTo(PlayerEntity playerEntity) {
        moveTo(new BlockPos(playerEntity.posX, playerEntity.posY, playerEntity.posZ));
    }

    public void moveTo(BlockPos blockPos) {
        targetBlockPos = blockPos;
        shiftGoalPriority(1);
        insertGoal(1, new MoveTowardsBlockPosGoal(this, 1D), "move " + blockPos.toString());
    }

    public void swapGoal(int priorityLeft, int priorityRight) {
        Goal taskLeft = tasks.get(priorityLeft);
        String nameLeft = taskNameByPriority.get(priorityLeft);
        Goal taskRight = tasks.get(priorityRight);
        String nameRight = taskNameByPriority.get(priorityRight);

        removeGoal(priorityLeft);
        removeGoal(priorityRight);

        insertGoal(priorityLeft, taskRight, nameRight);
        insertGoal(priorityRight, taskLeft, nameLeft);
    }

    public static final int maxTasks = 14;
    public HashMap<Integer, String> taskNameByPriority = new HashMap<>();
    private HashMap<Integer, Goal> tasks = new HashMap<>();

    public PlayerEntity focusedPlayer = null;

    public void breakBlock(BlockPos blockPos) {
        // Check if command exists, then just chance target block?
        targetBlockPos = blockPos;
        shiftGoalPriority(2);
        insertGoal(1, new MoveTowardsBlockPosGoal(this, 1D), "move " + blockPos.toString());
        insertGoal(2, new BreakBlockPosGoal(this, 1D), "break " + blockPos.toString());
    }

    public void shiftGoalPriority(Integer amount) {

        HashMap<Integer, String> tempNameByPriority = new HashMap<>();
        HashMap<Integer, Goal> tempTasks = new HashMap<>();

        // Remove all goals from goalSelector and remember them
        for (Integer priority : taskNameByPriority.keySet()) {
            if (priority + amount <= maxTasks && priority + amount > 0) {
                tempNameByPriority.put(priority + amount, taskNameByPriority.get(priority));
                tempTasks.put(priority + amount, tasks.get(priority));
            }
            removeGoal(priority);
        }

        // Re-insert with the shifted priorities.
        for (Integer priority : tempTasks.keySet()) {
            insertGoal(priority, tempTasks.get(priority), tempNameByPriority.get(priority));
        }
    }

    public void moveGoal(int priority, int newPriority) {
        Goal task = tasks.get(priority);
        String name = taskNameByPriority.get(priority);
        removeGoal(priority);
        insertGoal(newPriority, task, name);
    }

    public void insertGoal(int priority, Goal task, String name) {

        if (tasks.containsKey(priority)) {
            removeGoal(priority);
        }
        tasks.put(priority, task);
        taskNameByPriority.put(priority, name);
        goalSelector.addGoal(priority, task);
    }

//    @Override
//    protected void registerGoals() {
//        // Purposefully don't call register super.
//
//
//        this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
//        //TODO: target player/other golems?
//        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
//        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_213619_0_) -> {
//            return p_213619_0_ instanceof IMob && !(p_213619_0_ instanceof CreeperEntity);
//        }));
//    }

    public boolean removeGoal(int priority) {
        if (!tasks.containsKey(priority)) {
            return false;
        }
        goalSelector.removeGoal(tasks.get(priority));
        tasks.remove(priority);
        taskNameByPriority.remove(priority);
        return true;
    }

    public void removeAllGoals() {
        for (Integer priority : tasks.keySet()) {
            goalSelector.removeGoal(tasks.get(priority));
        }
    }

    protected void registerAttributes() {
        super.registerAttributes();
    }
}
