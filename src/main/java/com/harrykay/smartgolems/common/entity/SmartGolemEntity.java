package com.harrykay.smartgolems.common.entity;

import com.harrykay.smartgolems.common.entity.ai.FollowPlayerGoal;
import com.harrykay.smartgolems.common.entity.ai.MoveToBlockPosGoal;
import com.harrykay.smartgolems.common.entity.ai.PlaceBlockGoal;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

// follow owner goal
public class SmartGolemEntity extends IronGolemEntity {

    public PriorityQueue<Action> actions = new PriorityQueue<>(new ActionComparator());
    public ArrayList<Action> houseActions = new ArrayList<>();
    private int temp = 0;

    public void initHouseActions() {
        houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(0, 4, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(2, ActionType.PLACE_BLOCK, new BlockPos(1, 4, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(3, ActionType.PLACE_BLOCK, new BlockPos(2, 4, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(4, ActionType.PLACE_BLOCK, new BlockPos(0, 4, 1), Blocks.COBBLESTONE));
        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,0,1), Blocks.COBBLESTONE));
        houseActions.add(new Action(5, ActionType.PLACE_BLOCK, new BlockPos(2, 4, 1), Blocks.COBBLESTONE));
        houseActions.add(new Action(6, ActionType.PLACE_BLOCK, new BlockPos(0, 4, 2), Blocks.COBBLESTONE));
        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,0,2), Blocks.COBBLESTONE));
        houseActions.add(new Action(7, ActionType.PLACE_BLOCK, new BlockPos(2, 4, 2), Blocks.COBBLESTONE));
        houseActions.add(new Action(8, ActionType.PLACE_BLOCK, new BlockPos(0, 5, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(9, ActionType.PLACE_BLOCK, new BlockPos(1, 5, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(10, ActionType.PLACE_BLOCK, new BlockPos(2, 5, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(11, ActionType.PLACE_BLOCK, new BlockPos(0, 5, 1), Blocks.COBBLESTONE));
        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,1,1), Blocks.COBBLESTONE));
        houseActions.add(new Action(12, ActionType.PLACE_BLOCK, new BlockPos(2, 5, 1), Blocks.COBBLESTONE));
        houseActions.add(new Action(13, ActionType.PLACE_BLOCK, new BlockPos(0, 5, 2), Blocks.COBBLESTONE));
        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,1,2), Blocks.COBBLESTONE));
        houseActions.add(new Action(14, ActionType.PLACE_BLOCK, new BlockPos(2, 5, 2), Blocks.COBBLESTONE));
        houseActions.add(new Action(15, ActionType.PLACE_BLOCK, new BlockPos(0, 6, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(16, ActionType.PLACE_BLOCK, new BlockPos(1, 6, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(17, ActionType.PLACE_BLOCK, new BlockPos(2, 6, 0), Blocks.COBBLESTONE));
        houseActions.add(new Action(18, ActionType.PLACE_BLOCK, new BlockPos(0, 6, 1), Blocks.COBBLESTONE));
        houseActions.add(new Action(19, ActionType.PLACE_BLOCK, new BlockPos(1, 6, 1), Blocks.COBBLESTONE));
        houseActions.add(new Action(20, ActionType.PLACE_BLOCK, new BlockPos(2, 6, 1), Blocks.COBBLESTONE));
        houseActions.add(new Action(21, ActionType.PLACE_BLOCK, new BlockPos(0, 6, 2), Blocks.COBBLESTONE));
        houseActions.add(new Action(22, ActionType.PLACE_BLOCK, new BlockPos(1, 6, 2), Blocks.COBBLESTONE));
        houseActions.add(new Action(23, ActionType.PLACE_BLOCK, new BlockPos(2, 6, 2), Blocks.COBBLESTONE));

    }

    public void buildHouse() {
        initHouseActions();
        for (Action action : houseActions) {
            PlaceBlock(action.blockPos, action.block);
        }
    }

    public void follow(PlayerEntity playerEntity) {

        focusedPlayer = playerEntity;
        removeAllGoals();
        if (!taskNameByPriority.containsValue("FollowPlayer")) {
            insertGoal(1, new FollowPlayerGoal(this, 0.6D, 32.0F, 2.0F), "followPlayer");
        }
        if (!taskNameByPriority.containsValue("LookAtPlayer")) {
            insertGoal(2, new LookAtGoal(this, PlayerEntity.class, 6.0F), "LookAtPlayer");
        }
    }

    public void moveTo(BlockPos blockPos) {
        actions.add(new Action(temp++, ActionType.MOVE_TO, blockPos, null));
        removeAllGoals();
        if (!taskNameByPriority.containsValue("MoveToBlockPos")) {
            insertGoal(1, new MoveToBlockPosGoal(this, 1D, 1F), "MoveToBlockPos");
        }
    }

    public void PlaceBlock(BlockPos blockPos, Block block) {
        // Check if command exists, then just chance target block?
        actions.add(new Action(temp++, ActionType.PLACE_BLOCK, blockPos, block));
        //removeAllGoals();
        if (!taskNameByPriority.containsValue("MoveToBlockPos")) {
            insertGoal(1, new MoveToBlockPosGoal(this, 1D, 4F), "MoveToBlockPos");
        }

        if (!taskNameByPriority.containsValue("PlaceBlock")) {
            insertGoal(2, new PlaceBlockGoal(this, 5F, 3F), "PlaceBlock");
        }
    }


    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);
    }

    //public BlockPos targetBlockPos = null;

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

    public void removeAllGoals() {
        for (Integer priority : tasks.keySet()) {
            tasks.remove(priority);
        }
        tasks.clear();
        taskNameByPriority.clear();
    }

    public void moveTo(PlayerEntity playerEntity) {
        moveTo(new BlockPos(playerEntity.posX, playerEntity.posY, playerEntity.posZ));
    }

    // Neccessary for coordinating with different goals that rely on positions
    // TODO: extend so that multiple same type of goals can work together.
    public enum ActionType {
        MOVE_TO,
        MOVE_AND_STAY,
        PLACE_BLOCK
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

    public static class ActionComparator implements Comparator<Action> {

        // ascending order.
        public int compare(Action s1, Action s2) {
            if (s1.priority > s2.priority)
                return 1;
            else if (s1.priority < s2.priority)
                return -1;
            return 0;
        }
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

    public static class Action {
        public int priority;
        public ActionType actionType;
        public BlockPos blockPos = null;
        public Block block = null;

        Action(int priority, ActionType actionType, BlockPos blockPos, Block block) {
            this.priority = priority;
            this.actionType = actionType;
            this.blockPos = blockPos;
            this.block = block;
        }
    }

    protected void registerAttributes() {
        super.registerAttributes();
    }
}
