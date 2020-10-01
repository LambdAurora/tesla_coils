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
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TeslaPrimaryCoilBlock extends Block implements BlockEntityProvider
{
    private static final VoxelShape COIL_SHAPE      = Block.createCuboidShape(0, 3, 0, 16, 13, 16);
    private static final VoxelShape SECONDARY_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 3, 10);
    private static final VoxelShape TOP_SHAPE       = Block.createCuboidShape(7, 13, 7, 9, 14, 9);
    private static final VoxelShape BASE_SHAPE      = VoxelShapes.union(COIL_SHAPE, SECONDARY_SHAPE, TOP_SHAPE);

    public TeslaPrimaryCoilBlock(Settings settings)
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
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof TeslaCoilBlockEntity))
            return;

        if (((TeslaCoilBlockEntity) blockEntity).isEnabled()) {
            entity.damage(DamageSource.LIGHTNING_BOLT, 1.0F);
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world)
    {
        return new TeslaCoilBlockEntity();
    }
}
