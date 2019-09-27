package com.harrykay.smartgolems.common.entity.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

import java.util.EnumSet;

public class LookAtBlockPos extends Goal {
    protected final MobEntity entity;
    protected final float maxDistance;
    protected final Class<? extends LivingEntity> watchedClass;
    protected final EntityPredicate entityPredicate;
    private final float chance;
    protected Entity closestEntity;
    private int lookTime;

    public LookAtBlockPos(MobEntity entityIn, Class<? extends LivingEntity> watchTargetClass, float maxDistance, float chanceIn) {
        this.entity = entityIn;
        this.watchedClass = watchTargetClass;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        if (watchTargetClass == PlayerEntity.class) {
            this.entityPredicate = (new EntityPredicate()).setDistance(maxDistance).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks().setCustomPredicate((p_220715_1_) -> {
                return EntityPredicates.notRiding(entityIn).test(p_220715_1_);
            });
        } else {
            this.entityPredicate = (new EntityPredicate()).setDistance(maxDistance).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks();
        }

    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.entity.getRNG().nextFloat() >= this.chance) {
            return false;
        } else {
            if (this.entity.getAttackTarget() != null) {
                this.closestEntity = this.entity.getAttackTarget();
            }

            if (this.watchedClass == PlayerEntity.class) {
                this.closestEntity = this.entity.world.getClosestPlayer(this.entityPredicate, this.entity, this.entity.posX, this.entity.posY + (double) this.entity.getEyeHeight(), this.entity.posZ);
            } else {
                this.closestEntity = this.entity.world.func_225318_b(this.watchedClass, this.entityPredicate, this.entity, this.entity.posX, this.entity.posY + (double) this.entity.getEyeHeight(), this.entity.posZ, this.entity.getBoundingBox().grow(this.maxDistance, 3.0D, this.maxDistance));
            }

            return this.closestEntity != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (!this.closestEntity.isAlive()) {
            return false;
        } else if (this.entity.getDistanceSq(this.closestEntity) > (double) (this.maxDistance * this.maxDistance)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.lookTime = 40 + this.entity.getRNG().nextInt(40);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.closestEntity = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {

        this.entity.getLookController().func_220679_a(this.closestEntity.posX, this.closestEntity.posY + (double) this.closestEntity.getEyeHeight(), this.closestEntity.posZ);
        --this.lookTime;
    }
}
