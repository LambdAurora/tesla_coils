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

package me.lambdaurora.tesla_coil.entity;

import me.lambdaurora.tesla_coil.mixin.LightningEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class LightningArcEntity extends LightningEntity
{
    public LightningArcEntity(World world)
    {
        super(EntityType.LIGHTNING_BOLT, world);
    }

    @Override
    public void tick()
    {
        this.baseTick();

        int ambientTick = ((LightningEntityAccessor) this).getAmbientTick();
        if (ambientTick == 2) {
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER,
                    10000.f, .8f + this.random.nextFloat() * .2f);
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER,
                    2.f, .5f + this.random.nextFloat() * .2f);
        }

        --ambientTick;
        if (ambientTick < 0) {
            int remainingActions = ((LightningEntityAccessor) this).getRemainingActions();
            if (remainingActions == 0) {
                this.remove();
            } else if (ambientTick < -this.random.nextInt(10)) {
                ((LightningEntityAccessor) this).setRemainingActions(remainingActions - 1);
                ambientTick = 1;
                this.seed = this.random.nextLong();
            }
        }

        if (ambientTick >= 0) {
            if (!(this.world instanceof ServerWorld)) {
                this.world.setLightningTicksLeft(2);
            } else {
                double d = 3.0D;
                List<Entity> list = this.world.getOtherEntities(this, new Box(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                for (Entity entity : list) {
                    entity.onStruckByLightning((ServerWorld) this.world, this);
                }
            }
        }

        ((LightningEntityAccessor) this).setAmbientTick(ambientTick);
    }
}
