package com.harrykay.smartgolems.common.entity.passive;

import com.harrykay.smartgolems.common.entity.ai.goal.ExplodeNearPlayerGoal;
import com.harrykay.smartgolems.common.entity.ai.goal.FollowPlayerGoal;
import com.harrykay.smartgolems.common.entity.ai.goal.MoveToBlockPosGoal;
import com.harrykay.smartgolems.common.entity.ai.goal.PlaceBlockGoal;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.harrykay.smartgolems.SmartGolems.golems;
import static java.lang.Math.pow;

// follow owner goal
public class SmartGolemEntity extends IronGolemEntity {

    private final long ResetPriorityCounterAtAmount = (long) pow(2, 8 * 7) - 1;
    public PriorityQueue<Action> actions = new PriorityQueue<>(new ActionComparator());
    public ArrayList<Action> houseActions = new ArrayList<>();
    public HashMap<Integer, Integer> goalDependency = new HashMap<>();
    public int placeTimer = 10;
    private long priorityCounter = 0;
    private long priorityInitialOffset = 10;

    private int temp = 0;

    //TODO: require tnt and scale exposion accordingly
    public void explode() {
        if (!this.world.isRemote) {
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            int explosionRadius = 3;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) explosionRadius * 2, explosion$mode);
            spawnLingeringCloud();
            suicide();
        }
    }

    private void spawnLingeringCloud() {
        Collection<EffectInstance> collection = this.getActivePotionEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.posX, this.posY, this.posZ);
            areaeffectcloudentity.setRadius(2.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());

            for (EffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
            }

            this.world.addEntity(areaeffectcloudentity);
        }

    }

    public void doCreeperThings(PlayerEntity entity) {
        focusedPlayer = entity;
        if (!taskNameByPriority.containsValue("ExplodeNearPlayerGoal")) {
            removeAllGoals();
            insertGoal(1, new ExplodeNearPlayerGoal(this, 1.2D, 32.0F, 2.0F), "ExplodeNearPlayerGoal");
        }
    }

    public void suicide() {
        this.dead = true;
        setHealth(0);
    }

    public void initHouseActions() {
        //System.out.println("Entry");
        houseActions.clear();
        boolean isReadingY = false;
        try {
            //System.out.println("loop");
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\user\\AppData\\Roaming\\.minecraft\\assets\\blueprint.txt"));
            String line = reader.readLine();
            double y = 0;
            double z = 0;
            int priority = 0;
            while (line != null) {
                //System.out.println("Line read:");
                //System.out.println(line);
                if (line.startsWith("y")) {
                    isReadingY = true;
                    line = reader.readLine();
                    continue;
                }

                if (isReadingY) {
                    isReadingY = false;
                    y = Integer.parseInt(line) + 5;
                    z = 0;
                    line = reader.readLine();
                    continue;
                }

                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                for (int x = 0; stringTokenizer.hasMoreTokens(); ++x) {
                    switch (stringTokenizer.nextToken()) {
                        case "1":
                            houseActions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.COBBLESTONE));
                            break;
                        case "2":
                            houseActions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.OAK_WOOD));
                            break;
                        case "3":
                            houseActions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.GLASS));
                            break;
                        case "4":
                            houseActions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.DARK_OAK_DOOR));
                            break;
                        case "5":
                            houseActions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.DARK_OAK_STAIRS));
                            break;
                        default:
                            //System.out.println("nothing added");
                    }

                }
                ++z;
                ++priority;
                line = reader.readLine();
            }
            reader.close();

//            for (Action action : houseActions)
//            {
//                System.out.println("Action (x,y,z)" + action.blockPos + " block: " + action.block.getRegistryName());
//            }

        } catch (IOException e) {
            //System.out.println("test");
            e.printStackTrace();
        }


//        houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(0, 4, 0), Blocks.COBBLESTONE));
//        houseActions.add(new Action(2, ActionType.PLACE_BLOCK, new BlockPos(1, 4, 0), Blocks.COBBLESTONE));
//        houseActions.add(new Action(3, ActionType.PLACE_BLOCK, new BlockPos(2, 4, 0), Blocks.COBBLESTONE));
//        houseActions.add(new Action(4, ActionType.PLACE_BLOCK, new BlockPos(0, 4, 1), Blocks.COBBLESTONE));
//        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,0,1), Blocks.COBBLESTONE));
//        houseActions.add(new Action(5, ActionType.PLACE_BLOCK, new BlockPos(2, 4, 1), Blocks.COBBLESTONE));
//        houseActions.add(new Action(6, ActionType.PLACE_BLOCK, new BlockPos(0, 4, 2), Blocks.COBBLESTONE));
//        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,0,2), Blocks.COBBLESTONE));
//        houseActions.add(new Action(7, ActionType.PLACE_BLOCK, new BlockPos(2, 4, 2), Blocks.COBBLESTONE));
//        houseActions.add(new Action(8, ActionType.PLACE_BLOCK, new BlockPos(0, 5, 0), Blocks.COBBLESTONE));
//        houseActions.add(new Action(9, ActionType.PLACE_BLOCK, new BlockPos(1, 5, 0), Blocks.COBBLESTONE));
//        houseActions.add(new Action(10, ActionType.PLACE_BLOCK, new BlockPos(2, 5, 0), Blocks.COBBLESTONE));
//        houseActions.add(new Action(11, ActionType.PLACE_BLOCK, new BlockPos(0, 5, 1), Blocks.COBBLESTONE));
//        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,1,1), Blocks.COBBLESTONE));
//        houseActions.add(new Action(12, ActionType.PLACE_BLOCK, new BlockPos(2, 5, 1), Blocks.COBBLESTONE));
//        houseActions.add(new Action(13, ActionType.PLACE_BLOCK, new BlockPos(0, 5, 2), Blocks.COBBLESTONE));
//        //houseActions.add(new Action(1, ActionType.PLACE_BLOCK, new BlockPos(1,1,2), Blocks.COBBLESTONE));
//        houseActions.add(new Action(14, ActionType.PLACE_BLOCK, new BlockPos(2, 5, 2), Blocks.COBBLESTONE));
    }

    public void buildHouse() {
        initHouseActions();
        for (Action action : houseActions) {
            PlaceBlock(action.blockPos, action.block);
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        golems.remove(this);
        super.onDeath(cause);
    }

    public enum tasks {
        Follow
    }



    public void follow(PlayerEntity playerEntity) {

        focusedPlayer = playerEntity;
        if (!taskNameByPriority.containsValue("FollowPlayer") || !taskNameByPriority.containsValue("LookAtPlayer")) {
            removeAllGoals();
            insertGoal(1, new FollowPlayerGoal(this, 1D, 32.0F, 2.0F), "followPlayer");
            insertGoal(2, new LookAtGoal(this, PlayerEntity.class, 6.0F), "LookAt");
        }
    }

    public void moveTo(BlockPos blockPos) {
        if (!taskNameByPriority.containsValue("MoveToBlockPos")) {
            removeAllGoals();
            insertGoal(1, new MoveToBlockPosGoal(this, 1D, 0.17F), "MoveToBlockPos");
        }
        actions.add(new Action(temp++, ActionType.MOVE_TO, blockPos, null));
    }

    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);

    }

    public void PlaceBlock(BlockPos blockPos, Block block) {
        // Check if command exists, then just chance target block?

        if (!taskNameByPriority.containsValue("MoveToBlockPos") || !taskNameByPriority.containsValue("PlaceBlock")) {

            goalDependency.put(1, 2);
            goalDependency.put(2, 3);
            insertGoal(1, new MoveToBlockPosGoal(this, 1D, 6.5F), "MoveToBlockPos");
            insertGoal(2, new PlaceBlockGoal(this, 7F), "PlaceBlock");
            insertGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1D, 1F), "WaterAvoidingRandomWalkingGoal");
            // third goal move randomly if ontop?
        }
        actions.add(new Action(temp++, ActionType.PLACE_BLOCK, blockPos, block));
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
        actions.clear();
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
        WAY_POINT,
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

    //TODO fix up removal and display of commands, etc.
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

    @Override
    protected void registerGoals() {
        // Purposefully don't call register super.

    }

//    @Override
//    public void livingTick() {
//    super.livingTick();
//      if (this.placeTimer > 0)
//        --this.placeTimer;
//    }



    public boolean removeGoal(int priority) {
        if (!tasks.containsKey(priority)) {
            return false;
        }
        goalSelector.removeGoal(tasks.get(priority));
        tasks.remove(priority);
        taskNameByPriority.remove(priority);
        actions.clear();
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
