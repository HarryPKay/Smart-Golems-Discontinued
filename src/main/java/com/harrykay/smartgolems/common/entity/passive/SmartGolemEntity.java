package com.harrykay.smartgolems.common.entity.passive;

import com.harrykay.smartgolems.common.entity.ai.goal.*;
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
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

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
        actions.clear();

        //TODO: move into its own class
        //TODO: read into an array first and add scaffolding
        //TODO: look into json
        try {
            //System.out.println("loop");
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\user\\AppData\\Roaming\\.minecraft\\assets\\blueprint.txt"));
            String line = reader.readLine();
            double y = 0;
            double z = 0;
            int priority = 0;
            boolean isReadingY = false;
            boolean isMapping = false;
            Direction direction = Direction.NORTH;
            HashMap<String, Block> numberToBlock = new HashMap<>();

            Rotation rotation = Rotation.NONE;
            String modid = "minecraft";
            while (line != null) {
                //System.out.println("Line read:");
                //System.out.println(line);
                if (line.startsWith("y")) {
                    isReadingY = true;
                    line = reader.readLine();
                    continue;
                }

                if (line.startsWith("m")) {
                    isMapping = !isMapping;
                    line = reader.readLine();
                    continue;
                }

                if (isMapping) {
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                    String number = stringTokenizer.nextToken();
                    String blockName = stringTokenizer.nextToken();
                    for (final Block block : ForgeRegistries.BLOCKS.getValues()) {
                        if (Objects.requireNonNull(block.getRegistryName()).toString().equals(modid + ":" + blockName)) {
                            System.out.println("mapping " + number + " to " + block.getRegistryName());
                            numberToBlock.put(number, block);
                        }
                    }
                    line = reader.readLine();
                    continue;
                }

                if (isReadingY) {
                    isReadingY = false;
                    y = Integer.parseInt(line) + 4;
                    z = 0;
                    line = reader.readLine();
                    continue;
                }

                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                for (int x = 0; stringTokenizer.hasMoreTokens(); ++x) {
                    String token = stringTokenizer.nextToken();
                    switch (token) {
                        case "n":
                            --x;
                            //direction = Direction.NORTH;
                            rotation = Rotation.NONE;
                            break;
                        case "e":
                            --x;
                            //direction = Direction.EAST;
                            rotation = Rotation.CLOCKWISE_90;
                            break;
                        case "w":
                            --x;
                            //direction = Direction.WEST;
                            rotation = Rotation.COUNTERCLOCKWISE_90;
                            break;
                        case "s":
                            --x;
                            //direction = Direction.SOUTH;
                            rotation = Rotation.CLOCKWISE_180;
                            break;
//                        case "1":
//                            actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.COBBLESTONE, rotation, direction));
//                            break;
//                        case "2":
//                            actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.OAK_WOOD, rotation, direction));
//                            break;
//                        case "3":
//                            actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.GLASS, rotation, direction));
//                            break;
//                        case "4":
//                            actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.DARK_OAK_DOOR, rotation, direction));
//                            break;
//                        case "5":
//                            actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.DARK_OAK_STAIRS, rotation, direction));
//                            break;
//                        case "6":
//                            actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), Blocks.DIRT, rotation, direction));
//                            break;
                        default:
                            // TODO
                            if (numberToBlock.containsKey(token) && numberToBlock.get(token) != Blocks.AIR) {
                                System.out.println("adding " + token);
                                actions.add(new Action(priority, ActionType.PLACE_BLOCK, new BlockPos(x, y, z), numberToBlock.get(token), rotation, direction));
                            } else {
                                System.out.println("Unrecognized value given: " + token);
                            }
                    }
                }
                ++z;
                ++priority;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            //System.out.println("test");
            e.printStackTrace();
        }
    }

    public void buildHouse() {
        initHouseActions();
        PlaceBlock();
//        for (Action action : houseActions) {
//            PlaceBlock(action.blockPos, action.block);
//        }
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
        actions.add(new Action(temp++, ActionType.MOVE_TO, blockPos, null, null, null));
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
            insertGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1D, 50F), "WaterAvoidingRandomWalkingGoal");
            // third goal move randomly if ontop?
        }
        actions.add(new Action(temp++, ActionType.PLACE_BLOCK, blockPos, block, null, null));
    }

    public void PlaceBlock() {
        if (!taskNameByPriority.containsValue("MoveToBlockPos") || !taskNameByPriority.containsValue("PlaceBlock")) {
            goalDependency.put(1, 2);
            goalDependency.put(2, 3);
            insertGoal(1, new MoveToBlockPosGoal(this, 1D, 6.5F), "MoveToBlockPos");
            insertGoal(2, new PlaceBlockGoal(this, 7F), "PlaceBlock");
            insertGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1D, 100F), "WaterAvoidingRandomWalkingGoal");
            // third goal move randomly if ontop?
        }
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


    protected void registerAttributes() {
        super.registerAttributes();
    }
}
