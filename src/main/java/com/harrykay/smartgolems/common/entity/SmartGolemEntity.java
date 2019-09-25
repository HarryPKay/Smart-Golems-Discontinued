package com.harrykay.smartgolems.common.entity;

import com.harrykay.smartgolems.common.entity.ai.MoveTowardsPlayerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.HashMap;

// follow owner goal
public class SmartGolemEntity extends IronGolemEntity {

    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);
    }

    public void insertGoal(Integer priority, SurvivalSupportedTasks supportedTask) {
        switch (supportedTask) {
            case LookAtPlayer:
                insertGoal(priority, new LookAtGoal(this, PlayerEntity.class, 6.0F), supportedTask.name());
                return;
            case MoveTowardsBlockPosGoal:
                insertGoal(priority, null, supportedTask.name());
                return;
            case MoveTowardsPlayerGoal:
                insertGoal(priority, new MoveTowardsPlayerGoal(this, 0.03D, 32.0F, 2.0F), supportedTask.name());
                return;
            case PlaceBlockGoal:
                insertGoal(priority, null, supportedTask.name());
                return;
            case MeleeAttackGoal:
                insertGoal(priority, new MeleeAttackGoal(this, 1.0D, true), supportedTask.name());
                return;
            case MoveTowardsTargetGoal:
                insertGoal(priority, new MoveTowardsTargetGoal(this, 0.9D, 32.0F), supportedTask.name());
                return;
            case MoveTowardsVillageGoal:
                insertGoal(priority, new MoveTowardsVillageGoal(this, 0.6D), supportedTask.name());
                return;
            case MoveThroughVillageGoal:
                insertGoal(priority, new MoveThroughVillageGoal(this, 0.6D, false, 4, () -> {
                    return false;
                }), supportedTask.name());
                return;
            case ShowVillagerFlowerGoal:
                insertGoal(priority, new ShowVillagerFlowerGoal(this), supportedTask.name());
                return;
            case WaterAvoidingRandomWalkingGoal:
                insertGoal(priority, new WaterAvoidingRandomWalkingGoal(this, 0.6D), supportedTask.name());
                return;
            case LookRandomlyGoal:
                insertGoal(priority, new LookRandomlyGoal(this), supportedTask.name());
                return;
        }
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

//    public static boolean createProfile(SmartGolemEntity golem, HashMap<Integer, Goal> tasks, HashMap<Integer, String> taskNameByPriority, SupportedTasks... supportedTasks)
//    {
//        tasks = new HashMap<>();
//        taskNameByPriority = new HashMap<>();
//
//        int priority = 0;
//        for (SupportedTasks supportedTask : supportedTasks)
//        {
//            Goal task = parseSupportedTasks(golem, supportedTask);
//            if (task == null)
//            {
//                return false;
//            }
//            tasks.put(priority, task);
//            taskNameByPriority.put(priority, supportedTask.name());
//            ++priority;
//        }
//
//        return true;
//    }

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

    @Override
    protected void registerGoals() {
        // Purposefully don't call register super.
        this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
        //TODO: target player/other golems?
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_213619_0_) -> {
            return p_213619_0_ instanceof IMob && !(p_213619_0_ instanceof CreeperEntity);
        }));
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

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);

        // temp
        setPlayerCreated(true);
    }

    public enum SurvivalSupportedTasks {
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

//    private boolean placeBlock(BlockPos blockPos, Block block) {
////        if (!isBuilding)
////        {
////            return false;
////        }
////
////        if (posX == blockPos.getX() - 1 && posY == blockPos.getY() && posZ == blockPos.getZ())
////        {
////            System.out.println("Placing block");
////            BlockState state = block.getDefaultState();
////            swingArm(getActiveHand());
////            world.destroyBlock(blockPos,true);
////            world.setBlockState(blockPos, state);
////        }
////
////        //if (posX == blockPos.getX() && posX)
////
////        // Retry count?
////        if (!getNavigator().tryMoveToXYZ(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ(), 2D))
////        {
////            System.out.println("Can't set path: placeblock()");
////        }
//        return true;
//    }

    @Override
    public void livingTick() {
        super.livingTick();
    }
}
