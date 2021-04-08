/*
 * Copyright (c) 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.lambdaurora.tesla_coil.entity;

import dev.lambdaurora.tesla_coil.mixin.LightningEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Represents the lightning arc entity.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class LightningArcEntity extends LightningEntity {
    private static final TrackedData<Float> TARGET_X = DataTracker.registerData(LightningArcEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> TARGET_Y = DataTracker.registerData(LightningArcEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> TARGET_Z = DataTracker.registerData(LightningArcEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> TARGET_ENTITY = DataTracker.registerData(LightningArcEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private TargetPredicate targetPredicate;

    public LightningArcEntity(EntityType<? extends LightningArcEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TARGET_X, 0.f);
        this.dataTracker.startTracking(TARGET_Y, 0.f);
        this.dataTracker.startTracking(TARGET_Z, 0.f);
        this.dataTracker.startTracking(TARGET_ENTITY, this.getId());
    }

    public TargetPredicate getTargetPredicate() {
        return this.targetPredicate;
    }

    public void setTargetPredicate(TargetPredicate targetPredicate) {
        this.targetPredicate = targetPredicate;
    }

    /**
     * Returns the relative target position.
     *
     * @return the relative target position
     */
    public Vec3d getTarget() {
        int targetEntityId = this.dataTracker.get(TARGET_ENTITY);
        if (targetEntityId != this.getId()) {
            Entity targetEntity = this.getEntityWorld().getEntityById(targetEntityId);
            if (targetEntity != null)
                return targetEntity.getPos().subtract(this.getPos());
        }
        return new Vec3d(this.dataTracker.get(TARGET_X), this.dataTracker.get(TARGET_Y), this.dataTracker.get(TARGET_Z));
    }

    public void setTarget(Entity entity) {
        this.dataTracker.set(TARGET_ENTITY, entity.getId());
        this.setTarget(entity.getPos());
    }

    public void setTarget(Vec3d target) {
        target = target.subtract(this.getPos());
        this.dataTracker.set(TARGET_X, (float) target.getX());
        this.dataTracker.set(TARGET_Y, (float) target.getY());
        this.dataTracker.set(TARGET_Z, (float) target.getZ());
    }

    @Override
    public void tick() {
        this.baseTick();

        Vec3d target = this.getTarget();
        if (target == null) {
            this.discard();
            return;
        }
        target = target.add(this.getPos());

        int ambientTick = ((LightningEntityAccessor) this).getAmbientTick();
        if (ambientTick == 2) {
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER,
                    1.f, .8f + this.random.nextFloat() * .2f);
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER,
                    .5f, .5f + this.random.nextFloat() * .2f);
        }

        --ambientTick;
        if (ambientTick < 0) {
            int remainingActions = ((LightningEntityAccessor) this).getRemainingActions();
            if (remainingActions == 0) {
                this.discard();
            } else if (ambientTick < -this.random.nextInt(10)) {
                ((LightningEntityAccessor) this).setRemainingActions(remainingActions - 1);
                ambientTick = 1;
                this.seed = this.random.nextLong();
            }
        }

        if (ambientTick >= 0 && !this.world.isClient()) {
            double d = 3.0D;
            List<Entity> list = this.world.getOtherEntities(this,
                    new Box(target.getX() - d, target.getY() - d, target.getZ() - d,
                            target.getX() + d, target.getY() + 6.0D + d, target.getZ() + d),
                    Entity::isAlive);

            for (Entity entity : list) {
                if (entity instanceof LivingEntity && this.targetPredicate.test(null, (LivingEntity) entity)) {
                    if (entity instanceof IronGolemEntity) {
                        ((IronGolemEntity) entity).heal(1.f);
                    } else {
                        entity.onStruckByLightning((ServerWorld) this.world, this);
                    }
                }
            }
        }

        ((LightningEntityAccessor) this).setAmbientTick(ambientTick);
    }
}
