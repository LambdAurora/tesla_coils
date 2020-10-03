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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class TeslaCoilBlockEntity extends BlockEntity implements Tickable
{
    private final Random random = new Random();
    private boolean enabled = false;
    private int sideParticles = 0;

    public TeslaCoilBlockEntity()
    {
        super(TeslaCoilRegistry.TESLA_COIL_BLOCK_ENTITY_TYPE);
    }

    public boolean isEnabled()
    {
        return this.enabled;
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

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        this.enabled = tag.getBoolean("enabled");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        tag = super.toTag(tag);
        tag.putBoolean("enabled", this.enabled);
        return tag;
    }

    protected void checkStructure()
    {
        if (this.world.getTime() % 80L != 0L) {
            return;
        }

        BlockPos.Mutable pos = new BlockPos.Mutable(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ());

        for (int i = 0; i < 4; i++) {
            BlockState state = this.world.getBlockState(pos);

            if (i < 3) {
                if (i < 2) {
                    if (state.getBlock() != TeslaCoilRegistry.TESLA_SECONDARY_COIL_BLOCK) {
                        this.enabled = false;
                        return;
                    }
                } else if (state.getBlock() != Blocks.REDSTONE_BLOCK) {
                    this.enabled = false;
                    return;
                }

                for (Direction direction : Direction.values()) {
                    if (!direction.getAxis().isHorizontal())
                        continue;

                    pos.move(direction.getOffsetX(), 0, direction.getOffsetZ());

                    state = this.world.getBlockState(pos);
                    if (state.getBlock() != Blocks.IRON_BARS) {
                        this.enabled = false;
                        return;
                    }

                    pos.move(-direction.getOffsetX(), 0, -direction.getOffsetZ());
                }
            } else {
                if (state.getBlock() != Blocks.EMERALD_BLOCK) {
                    this.enabled = false;
                    return;
                }
            }

            pos.move(0, -1, 0);
        }

        this.enabled = true;
    }

    protected void displaySideParticles()
    {
        if (this.sideParticles > 18) {
            this.sideParticles = 0;
        }

        if (this.sideParticles % 2 == 0) {
            float xPos = this.pos.getX() + .5f;
            float zPos = this.pos.getZ() + .5f;
            float yOffset = this.sideParticles / 2.f / 3.f;
            float yPos = this.pos.getY() - 3 + yOffset;

            for (Direction direction : Direction.values()) {
                if (direction.getAxis().isHorizontal()) {
                    this.world.addParticle(ParticleTypes.CRIT,
                            xPos + direction.getOffsetX(), yPos, zPos + direction.getOffsetZ(),
                            0, 0.025, 0);
                }
            }
        }

        this.sideParticles++;
    }

    protected void tryAttack()
    {
        if (this.world.getTime() % 20L != 0L || this.random.nextBoolean())
            return;

        double x = this.getPos().getX() + 0.5;
        double y = this.getPos().getY();
        double z = this.getPos().getZ() + 0.5;

        TargetPredicate targetPredicate = new TargetPredicate();
        targetPredicate.setPredicate(entity -> entity instanceof HostileEntity);
        LivingEntity entity = this.world.getClosestEntity(HostileEntity.class, targetPredicate, null,
                x, y, z,
                new Box(this.pos.getX() - 10, this.pos.getY() - 8, this.pos.getZ() - 10, this.pos.getX() + 10, this.pos.getY() + 5, this.pos.getZ() + 10));

        if (entity != null) {
            LightningArcEntity lightningEntity = TeslaCoilRegistry.LIGHTNING_ARC_ENTITY_TYPE.create(this.world);

            if (lightningEntity == null) return;

            lightningEntity.setPos(x, y + 0.5, z);
            lightningEntity.setTarget(entity.getBlockPos());
            lightningEntity.setTargetPredicate(targetPredicate);

            this.world.spawnEntity(lightningEntity);

            //entity.damage(DamageSource.LIGHTNING_BOLT, 1);
        }
    }
}
