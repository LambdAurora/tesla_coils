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

package dev.lambdaurora.tesla_coil.block;

import dev.lambdaurora.tesla_coil.TeslaCoilMod;
import dev.lambdaurora.tesla_coil.block.entity.TeslaCoilBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * Represents the tesla coil top load block.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class TeslaCoilTopLoadBlock extends Block {
    private static final VoxelShape LOAD_SHAPE = Block.createCuboidShape(0, 5, 0, 16, 11, 16);
    private static final VoxelShape COIL_SHAPE = Block.createCuboidShape(5, 0, 5, 11, 5, 11);
    private static final VoxelShape BASE_SHAPE = VoxelShapes.union(LOAD_SHAPE, COIL_SHAPE);

    public TeslaCoilTopLoadBlock(Settings settings) {
        super(settings);
    }

    /* Shapes */

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return BASE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BASE_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BASE_SHAPE;
    }

    /* Interaction */

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        int offset = 2;
        BlockEntity blockEntity = world.getBlockEntity(pos.down(offset));
        while (!(blockEntity instanceof TeslaCoilBlockEntity) && offset <= 4) {
            offset++;
            blockEntity = world.getBlockEntity(pos.down(offset));
        }

        if (!(blockEntity instanceof TeslaCoilBlockEntity))
            return;

        TeslaCoilMod.onTeslaCoilEntityCollision((TeslaCoilBlockEntity) blockEntity, entity);
    }
}
