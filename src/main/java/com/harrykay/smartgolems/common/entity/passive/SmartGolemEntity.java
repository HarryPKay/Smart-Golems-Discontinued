package com.harrykay.smartgolems.common.entity.passive;

import com.harrykay.smartgolems.common.entity.ai.goal.*;
import com.harrykay.smartgolems.file.BluePrintReader;
import net.minecraft.block.Block;
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

import java.util.Collection;
import java.util.HashMap;

import static com.harrykay.smartgolems.SmartGolems.golems;

// follow owner goal
public class SmartGolemEntity extends IronGolemEntity {

    public HashMap<Integer, Integer> goalDependency = new HashMap<>();
    public static final int maxTasks = 14;
    public HashMap<Integer, Goal> priorityToGoal = new HashMap<>();

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

    public static void actLikeIronGolems(SmartGolemEntity smartGolemEntity) {
        //smartGolemEntity.shiftGoalPriority(SmartGolemEntity.maxTasks);
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

    public void suicide() {
        this.dead = true;
        setHealth(0);
    }

    @Override
    public void onDeath(DamageSource cause) {
        golems.remove(this);
        super.onDeath(cause);
    }

    public void doCreeperThings(PlayerEntity entity) {
        removeAllGoals();
        priorityToGoal.put(1, new ExplodeNearPlayerGoal(this, 1.2D, 32.0F, 2.0F, entity));
        goalSelector.addGoal(1, priorityToGoal.get(1));
    }

    public void follow(PlayerEntity playerEntity) {
        removeAllGoals();
        priorityToGoal.put(1, new FollowPlayerGoal(this, 1D, 32.0F, 2.0F, playerEntity));
        priorityToGoal.put(2, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        goalSelector.addGoal(1, priorityToGoal.get(1));
        goalSelector.addGoal(2, priorityToGoal.get(2));
    }

    public SmartGolemEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);
    }

    public void moveTo(BlockPos blockPos) {
        removeAllGoals();
        MoveToPositionGoal moveToPositionGoal = new MoveToPositionGoal(this, 1D, 0.17F);
        moveToPositionGoal.positions.add(new Action(0, blockPos, null, null, null));
        goalSelector.addGoal(1, moveToPositionGoal);
        priorityToGoal.put(1, moveToPositionGoal);
    }

    public void placeBlocks(BlockPos blockPos, Block block) {
        removeAllGoals();
        PlaceBlocksGoal placeBlocksGoal = new PlaceBlocksGoal(this, 2D, 7F);
        placeBlocksGoal.actions.add(new Action(0, blockPos, block, null, null));
        goalSelector.addGoal(1, placeBlocksGoal);
        priorityToGoal.put(1, placeBlocksGoal);
    }

    //public BlockPos targetBlockPos = null;

    public void placeBlocks(String bluePrintName) {
        removeAllGoals();
        PlaceBlocksGoal placeBlocksGoal = new PlaceBlocksGoal(this, 2D, 7F);
        WaterAvoidingRandomWalkingGoal waterAvoidingRandomWalkingGoal = new WaterAvoidingRandomWalkingGoal(this, 2D, 100F);
        placeBlocksGoal.actions = BluePrintReader.read(bluePrintName);
        goalSelector.addGoal(1, placeBlocksGoal);
        goalSelector.addGoal(2, waterAvoidingRandomWalkingGoal);
        priorityToGoal.put(1, placeBlocksGoal);
        priorityToGoal.put(2, waterAvoidingRandomWalkingGoal);
    }

    public void removeAllGoals() {
        for (Integer priority : priorityToGoal.keySet()) {
            priorityToGoal.remove(priority);
        }
        priorityToGoal.clear();
    }

    public void moveTo(PlayerEntity playerEntity) {
        moveTo(new BlockPos(playerEntity.posX, playerEntity.posY, playerEntity.posZ));
    }

//    public void swapGoal(int priorityLeft, int priorityRight) {
//        net.minecraft.entity.ai.goal.Goal taskLeft = tasks.get(priorityLeft);
//        String nameLeft = taskNameByPriority.get(priorityLeft);
//        net.minecraft.entity.ai.goal.Goal taskRight = tasks.get(priorityRight);
//        String nameRight = taskNameByPriority.get(priorityRight);
//
//        removeGoal(priorityLeft);
//        removeGoal(priorityRight);
//
//        insertGoal(priorityLeft, taskRight, nameRight);
//        insertGoal(priorityRight, taskLeft, nameLeft);
//    }



    //TODO fix up removal and display of commands, etc.
//    public void shiftGoalPriority(Integer amount) {
//
//        HashMap<Integer, String> tempNameByPriority = new HashMap<>();
//        HashMap<Integer, net.minecraft.entity.ai.goal.Goal> tempTasks = new HashMap<>();
//
//        // Remove all goals from goalSelector and remember them
//        for (Integer priority : taskNameByPriority.keySet()) {
//            if (priority + amount <= maxTasks && priority + amount > 0) {
//                tempNameByPriority.put(priority + amount, taskNameByPriority.get(priority));
//                tempTasks.put(priority + amount, tasks.get(priority));
//            }
//            removeGoal(priority);
//        }
//
//        // Re-insert with the shifted priorities.
//        for (Integer priority : tempTasks.keySet()) {
//            insertGoal(priority, tempTasks.get(priority), tempNameByPriority.get(priority));
//        }
//    }

//    public void moveGoal(int priority, int newPriority) {
//        net.minecraft.entity.ai.goal.Goal task = tasks.get(priority);
//        String name = taskNameByPriority.get(priority);
//        removeGoal(priority);
//        insertGoal(newPriority, task);
//    }

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
        if (!priorityToGoal.containsKey(priority)) {
            return false;
        }
        goalSelector.removeGoal(priorityToGoal.get(priority));
        priorityToGoal.remove(priority);
        return true;
    }


    protected void registerAttributes() {
        super.registerAttributes();
    }
}
