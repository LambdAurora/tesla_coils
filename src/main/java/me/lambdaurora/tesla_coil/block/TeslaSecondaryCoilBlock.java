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

package me.lambdaurora.tesla_coil.block;

import me.lambdaurora.tesla_coil.block.entity.TeslaCoilBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TeslaSecondaryCoilBlock extends Block
{
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(5, 0, 5, 11, 16, 11);

    public TeslaSecondaryCoilBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos)
    {
        return BASE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return BASE_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return BASE_SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
    {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(pos.getX(), pos.getY() + 1, pos.getZ());
        for (int i = 0; i < 2; i++) {
            BlockEntity blockEntity = world.getBlockEntity(mutablePos);
            if (!(blockEntity instanceof TeslaCoilBlockEntity))
                return;

            if (((TeslaCoilBlockEntity) blockEntity).isEnabled()) {
                entity.damage(DamageSource.LIGHTNING_BOLT, 1.0F);
                return;
            }

            mutablePos.move(0, 1, 0);
        }
    }
}
