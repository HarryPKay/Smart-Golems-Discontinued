package com.harrykay.smartgolems.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

// follow owner goal
public class SmartGolemEntity extends IronGolemEntity {

    public static void createProfile(HashMap<Integer, Goal> tasks, HashMap<Integer, String> taskNameByPriority, SupportedTasks... supportedTasks) {

    }

    public enum SupportedTasks {
        // Custom

        LookAtPlayer,
        MoveTowardsBlockPosGoal,
        MoveTowardsPlayerGoal,
        PlaceBlockGoal,

        // Vanilla

        // IronGolemEntity Default

        MeleeAttackGoal,
        MoveTowardsTargetGoal,
        MoveTowardsVillageGoal,
        MoveThroughVillageGoal,
        ShowVillagerFlowerGoal,
        WaterAvoidingRandomWalkingGoal,
        LookAtGoal,
        LookRandomlyGoal
    }

    public static final int maxTasks = 14;
    public HashMap<Integer, String> taskNameByPriority = new HashMap<>();
    private HashMap<Integer, Goal> tasks = new HashMap<>();

    public PlayerEntity focusedPlayer = null;

    public void shiftGoalPriority(Integer amount) {
        HashMap<Integer, String> tempNameByPriority = new HashMap<>();
        HashMap<Integer, Goal> tempTasks = new HashMap<>();
        for (Integer priority : taskNameByPriority.keySet()) {
            if (priority + amount <= maxTasks && priority + amount >= 0) {
                tempNameByPriority.put(priority + amount, taskNameByPriority.get(priority));
                tempTasks.put(priority + amount, tasks.get(priority));
            } else {
                removeGoal(priority);
            }
        }

        taskNameByPriority = tempNameByPriority;
        tasks = tempTasks;
    }

    public void swapGoal(int priorityLeft, int priorityRight) {
        Goal taskLeft = tasks.get(priorityLeft);
        String nameLeft = taskNameByPriority.get(priorityLeft);
        Goal taskRight = tasks.get(priorityRight);
        String nameRight = taskNameByPriority.get(priorityRight);

        removeGoal(priorityLeft);
        removeGoal(priorityRight);

        addGoal(priorityLeft, taskRight, nameRight);
        addGoal(priorityRight, taskLeft, nameLeft);
    }

    public void moveGoal(int priority, int newPriority) {
        Goal task = tasks.get(priority);
        String name = taskNameByPriority.get(priority);
        removeGoal(priority);
        addGoal(newPriority, task, name);
    }

    public void addGoal(int priority, Goal task, String name) {
        tasks.put(priority, task);
        taskNameByPriority.put(priority, name);
        goalSelector.addGoal(priority, tasks.get(priority));
    }

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
        taskNameByPriority = new HashMap<>();
        tasks = new HashMap<>();
    }

//    public void showGoals(PlayerEntity playerEntity)
//    {
//        for (Integer priority : goals.keySet())
//        {
//            goalSelector.removeGoal(goals.get(priority));
//
//        }
//    }

    //    public MoveTowardsBlockPos(CreatureEntity creature, double speedIn, float targetMaxDistance) {
//        this.creature = creature;
//        this.speed = speedIn;
//        this.maxTargetDistance = targetMaxDistance;
//        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
//    }


//    private ArrayList<BlockPos> positions = new ArrayList<>();
//    private ArrayList<Block> currentState = new ArrayList<>();
//    private ArrayList<Block> desiredState = new ArrayList<>();
//
//    private static class BuildAction
//    {
//        public BuildAction(BlockPos blockPos, Block block)
//        {
//            this.blockPos = blockPos;
//            this.block = block;
//        }
//
//        public BlockPos blockPos;
//        public Block block;
//    }

    //private ArrayList<BuildAction> placements = new ArrayList<>();


    //HashMap<BlockPos, Block> placements = new HashMap<>();

    private boolean isBuilding = false;
    private int followTimer;

    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);

//        for (int i = 0; i < 9; ++i)
//        {
//            desiredState.add(Blocks.AIR);
//        }
    }

    @Override
    protected void registerGoals() {

    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
        setPlayerCreated(true);
    }

    private void aStarSearch() {

    }

    private void store() {

    }

//    // Timer and check if has path?
//    public void scanEnvironment()
//    {
//        currentState = new ArrayList<>();
//        positions = new ArrayList<>();
//        for (int x =  -1; x <= 1; ++x)
//        {
//            for (int z = -1; z <= 1; ++z)
//            {
//                BlockPos blockPos = new BlockPos(posX + x, posY - 1, posZ + z);
//                positions.add(blockPos);
//                currentState.add(world.getBlockState(blockPos).getBlock());
////                positions.set((x + 1) * 3 + ((z + 1) % 3), blockPos);
////                currentState.set((x + 1) * 3 + ((z + 1) % 3), world.getBlockState(blockPos).getBlock());
//                //System.out.println(world.getBlockState(blockPos).getBlock().getRegistryName());
//            }
//        }
//
//        // Get what block is underneath that entity
//        //BlockPos blockPos = new BlockPos(event.getPlayer().posX, event.getPlayer().posY - 1, event.getPlayer().posZ);
//        //System.out.println(event.getPlayer().world.getBlockState(blockPos).getBlock().getRegistryName());
//        //event.getPlayer().world.getBlockState(blockPos).getBlock().asItem();
//
//
//        isBuilding = false;
//        if (currentState.size() < 9 || desiredState.size() < 9 || positions.size() < 9)
//        {
//            System.out.println(currentState.size() );
//            System.out.println(desiredState.size());
//            System.out.println(positions.size());
//        }
//
//        placements = new ArrayList<>();
//        //placements = new HashMap<>();
//        for (int i = 0; i < desiredState.size(); ++i)
//        {
//
//            System.out.println(currentState.get(i).getRegistryName() + " " +  desiredState.get(i).getRegistryName());
//            if (currentState.get(i).getRegistryName() != desiredState.get(i).getRegistryName() )
//            {
//                isBuilding = true;
//                placements.add(new BuildAction(positions.get(i), desiredState.get(i)));
//                //placements.put(positions.get(i), desiredState.get(i));
//                //world.destroyBlock(positions.get(i),true);
//
//            }
//        }
//    }

    private void pickUp() {

    }

    private boolean placeBlock(BlockPos blockPos, Block block) {
//        if (!isBuilding)
//        {
//            return false;
//        }
//
//        if (posX == blockPos.getX() - 1 && posY == blockPos.getY() && posZ == blockPos.getZ())
//        {
//            System.out.println("Placing block");
//            BlockState state = block.getDefaultState();
//            swingArm(getActiveHand());
//            world.destroyBlock(blockPos,true);
//            world.setBlockState(blockPos, state);
//        }
//
//        //if (posX == blockPos.getX() && posX)
//
//        // Retry count?
//        if (!getNavigator().tryMoveToXYZ(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ(), 2D))
//        {
//            System.out.println("Can't set path: placeblock()");
//        }
        return true;
    }

    @Override
    public void livingTick() {
        super.livingTick();

//        if (this > 0) {
//            --this.attackTimer;
//        }


//        if (!placements.isEmpty())
//        {
//            if (placeBlock(placements.get(0).blockPos, placements.get(0).block))
//            {
//                placements.remove(0);
//            }
//        }


//        PlayerEntity player =  world.getClosestPlayer(this.posX, this.posY, this.posZ, 100,true);
//
//        if (player != null)
//        {
//            if (this.getNavigator().tryMoveToXYZ(player.posX, player.posY, player.posZ, 2))
//            {
////                System.out.println(getCustomName() + " is chasing " + player.posX + " "  + player.posY + " " + player.posZ);
////                System.out.println(getType());
//            }
//            else
//            {
////                System.out.println(getCustomName() + " is not chasing");
//            }
//        }
    }

    // Has inventory?

    // Lookahead

    // Actions:
    // Place block
    // Retrieve block

}
