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

package me.lambdaurora.tesla_coil.block.entity;

import me.lambdaurora.tesla_coil.TeslaCoilRegistry;
import me.lambdaurora.tesla_coil.entity.LightningArcEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class TeslaCoilBlockEntity extends BlockEntity implements Tickable
{
    private final Random  random        = new Random();
    private       boolean enabled       = true;
    private       int     sideParticles = 0;

    public TeslaCoilBlockEntity()
    {
        super(TeslaCoilRegistry.TESLA_COIL_BLOCK_ENTITY_TYPE);
    }

    @Override
    public void tick()
    {
        this.checkStructure();

        if (this.enabled) {
            if (this.world.isClient()) {
                this.displaySideParticles();
            } else {
                this.tryAttack();
            }
        }
    }

    public void checkStructure()
    {

    }

    public void displaySideParticles()
    {
        if (this.sideParticles > 12) {
            this.sideParticles = 0;
        }

        float xPos = this.pos.getX() + .5f;
        float zPos = this.pos.getZ() + .5f;
        float yOffset = this.sideParticles / 3.f;
        float yPos = this.pos.getY() - 3 + yOffset;

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                this.world.addParticle(ParticleTypes.CRIT, xPos, yPos, zPos, 0, 1, 0);
            }
        }
    }

    public void tryAttack()
    {
        if (this.world.getTime() % 40L != 0L || this.random.nextBoolean())
            return;

        TargetPredicate targetPredicate = new TargetPredicate();
        targetPredicate.setPredicate(entity -> !(entity instanceof PlayerEntity));
        LivingEntity entity = this.world.getClosestEntity(HostileEntity.class, targetPredicate, null,
                this.pos.getX() + 0.5, this.pos.getY(), this.pos.getZ() + 0.5,
                new Box(this.pos.getX() - 10, this.pos.getY() - 8, this.pos.getZ() - 10, this.pos.getX() + 10, this.pos.getY() + 5, this.pos.getZ() + 10));

        if (entity != null) {
            LightningArcEntity lightningEntity = new LightningArcEntity(this.world);

            lightningEntity.setPos(entity.getX(), entity.getY(), entity.getZ());

            this.world.spawnEntity(lightningEntity);

            entity.damage(DamageSource.LIGHTNING_BOLT, 1);
        }
    }
}
