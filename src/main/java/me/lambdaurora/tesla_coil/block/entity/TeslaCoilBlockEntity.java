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
import me.lambdaurora.tesla_coil.block.TeslaPrimaryCoilBlock;
import me.lambdaurora.tesla_coil.block.TeslaSecondaryCoilBlock;
import me.lambdaurora.tesla_coil.block.WeatherableTeslaCoilPartBlock;
import me.lambdaurora.tesla_coil.entity.LightningArcEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TeslaCoilBlockEntity extends BlockEntity
{
    private final Random random = new Random();
    private int power = 0;
    private int age = 0;
    private long nextOxidation;
    private float effectiveness = 1.f;

    @Environment(EnvType.CLIENT)
    private int sideParticles = 0;
    @Environment(EnvType.CLIENT)
    private Direction smallArcDirection = null;
    @Environment(EnvType.CLIENT)
    private int smallArcCooldown = 0;

    public TeslaCoilBlockEntity(BlockPos pos, BlockState state)
    {
        super(TeslaCoilRegistry.TESLA_COIL_BLOCK_ENTITY_TYPE, pos, state);
    }

    public boolean isEnabled()
    {
        return this.power != 0;
    }

    public int getPower()
    {
        return this.power;
    }

    /**
     * Returns the effectiveness of this tesla coil. Effectiveness is a number between 0 and 1 representing a percentage.
     *
     * @return the effectiveness
     */
    public float getEffectiveness()
    {
        return this.effectiveness;
    }

    public float getPowerDamageMultiplier()
    {
        return this.getPower() <= 2 ? 1.f : 1.25f;
    }

    public float getDamageMultiplier()
    {
        return this.getPowerDamageMultiplier() * this.getEffectiveness();
    }

    public int getAge()
    {
        return this.age;
    }

    @Environment(EnvType.CLIENT)
    public @Nullable Direction getSmallArcDirection()
    {
        return this.smallArcDirection;
    }

    @Environment(EnvType.CLIENT)
    public static void clientTick(World world, BlockPos pos, BlockState state, TeslaCoilBlockEntity teslaCoil)
    {
        teslaCoil.checkStructure(world);

        if (teslaCoil.isEnabled()) {
            teslaCoil.age++;

            teslaCoil.displaySideParticles();
            teslaCoil.rollNextSmallArcDirection();
        }
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, TeslaCoilBlockEntity teslaCoil)
    {
        int lastPower = teslaCoil.getPower();
        teslaCoil.checkStructure(world);
        if (lastPower != teslaCoil.getPower())
            teslaCoil.markDirty();

        if (teslaCoil.isEnabled()) {
            if (lastPower == 0) {
                teslaCoil.pickNextOxidationTime(world);
            }

            teslaCoil.age++;

            teslaCoil.tryAttack();
            if (teslaCoil.getPower() > 1)
                teslaCoil.oxidationTick(world);
        } else {
            teslaCoil.age = 0;
        }
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        super.fromTag(tag);
        this.power = tag.getInt("power");
        this.nextOxidation = tag.getLong("next_oxidation");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        tag = super.toTag(tag);
        tag.putInt("power", this.power);
        tag.putLong("next_oxidation", this.nextOxidation);
        return tag;
    }

    protected void pickNextOxidationTime(World world)
    {
        int randomOffset = this.random.nextInt(24000 * 2);
        if (this.random.nextBoolean())
            randomOffset = -randomOffset;

        this.nextOxidation = world.getTime() + (24000 * 15) + randomOffset; // 15 Minecraft Days +/- 2 (random) Minecraft Days
        this.markDirty();
    }

    protected void checkStructure(World world)
    {
        if (world.getTime() % 80L != 0L) {
            return;
        }

        // Controller
        BlockPos.Mutable pos = new BlockPos.Mutable(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        if (!this.checkForIronBars(pos)) {
            this.power = 0;
            return;
        }

        // Primary coil
        pos.move(Direction.UP);
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof TeslaPrimaryCoilBlock) || !this.checkForIronBars(pos) || ((TeslaPrimaryCoilBlock) state.getBlock()).getWeathered() >= 3) {
            this.power = 0;
            return;
        }

        pos.move(Direction.UP);
        this.power = 1;
        while ((state = world.getBlockState(pos)).getBlock() instanceof TeslaSecondaryCoilBlock && this.checkForIronBars(pos) && power < 3) {
            if (((TeslaSecondaryCoilBlock) state.getBlock()).getWeathered() >= 3) {
                this.power = 0;
                return;
            }
            this.power++;
            pos.move(Direction.UP);
        }

        if (state.getBlock() != TeslaCoilRegistry.TESLA_COIL_TOP_LOAD_BLOCK) {
            this.power = 0;
            return;
        }

        // The tesla coil is still working. Getting its effectiveness.
        float effectiveness = 0.f;
        pos.set(this.pos).move(Direction.UP);
        for (int i = 0; i < this.power; i++) {
            state = world.getBlockState(pos);

            Block block = state.getBlock();
            if (block instanceof WeatherableTeslaCoilPartBlock) {
                WeatherableTeslaCoilPartBlock part = (WeatherableTeslaCoilPartBlock) block;

                effectiveness += ((3.f - part.getWeathered()) / 3.f) / this.getPower();
            }

            pos.move(Direction.UP);
        }
        this.effectiveness = effectiveness;
    }

    private void oxidationTick(World world)
    {
        if (world.isSkyVisible(this.pos.up(1 + this.power)) && world.isRaining()) {
            this.nextOxidation -= 5;
        }

        if ((this.nextOxidation - world.getTime()) <= 0) {
            this.oxidize(world);
            this.pickNextOxidationTime(world);
        }
    }

    private void oxidize(World world)
    {
        int yOffset = Math.max(1, this.random.nextInt(this.power + 1));
        System.out.println(yOffset);
        BlockPos pos = this.pos.up(yOffset);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof WeatherableTeslaCoilPartBlock && block instanceof Oxidizable) {
            world.setBlockState(pos, ((Oxidizable) state.getBlock()).getOxidationResult(state));
        }
    }

    private boolean checkForIronBars(BlockPos.Mutable pos)
    {
        BlockState state;
        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isHorizontal())
                continue;

            pos.move(direction);

            state = this.world.getBlockState(pos);
            if (state.getBlock() != Blocks.IRON_BARS) {
                return false;
            }

            pos.move(direction.getOpposite());
        }
        return true;
    }

    @Environment(EnvType.CLIENT)
    protected void displaySideParticles()
    {
        if (this.sideParticles > 18 || (this.power % 2 != 0 && this.sideParticles > 17)) {
            this.sideParticles = 0;
        }

        if (this.sideParticles % 2 == 0) {
            float xPos = this.pos.getX() + .5f;
            float zPos = this.pos.getZ() + .5f;
            float yOffset = this.sideParticles / 2.f / (float) (5 - this.power);
            float yPos = this.pos.getY() + yOffset;

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

    @Environment(EnvType.CLIENT)
    private void rollNextSmallArcDirection()
    {
        final int cooldown = 5;

        if (this.smallArcCooldown > 0) {
            this.smallArcCooldown--;
            return;
        }

        int dirIndex = this.random.nextInt(20);
        if (dirIndex < 6) {
            this.smallArcDirection = Direction.values()[dirIndex];
            if (this.smallArcDirection.getAxis().isVertical())
                this.smallArcDirection = null;
        } else
            this.smallArcDirection = null;

        this.smallArcCooldown = cooldown;
    }

    protected void tryAttack()
    {
        if (this.world.getTime() % (long) (20 * (1.f + (1.f - this.getEffectiveness()))) != 0L || this.random.nextBoolean())
            return;

        double x = this.getPos().getX() + 0.5;
        double y = this.getPos().getY();
        double z = this.getPos().getZ() + 0.5;

        TargetPredicate targetPredicate = new TargetPredicate();
        targetPredicate.setPredicate(entity -> entity instanceof Monster
                || (entity instanceof IronGolemEntity && entity.getHealth() < entity.getMaxHealth() - 1.f));
        int offset = (4 + this.power * 4);
        LivingEntity entity = this.world.getClosestEntity(LivingEntity.class, targetPredicate, null,
                x, y, z,
                new Box(this.pos.getX() - offset, this.pos.getY() - 4, this.pos.getZ() - offset,
                        this.pos.getX() + offset, this.pos.getY() + offset - 2, this.pos.getZ() + offset));

        if (entity != null) {
            LightningArcEntity lightningEntity = TeslaCoilRegistry.LIGHTNING_ARC_ENTITY_TYPE.create(this.world);

            if (lightningEntity == null) return;

            lightningEntity.setPos(x, y + 1.5 + this.power, z);
            lightningEntity.setTarget(entity);
            lightningEntity.setTargetPredicate(targetPredicate);

            this.world.spawnEntity(lightningEntity);

            if (this.getPower() > 1) {
                this.nextOxidation -= 100;
                this.markDirty();
            }
        }
    }
}
