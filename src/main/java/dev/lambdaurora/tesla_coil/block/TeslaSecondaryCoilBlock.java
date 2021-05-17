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
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TeslaSecondaryCoilBlock extends WeatherableTeslaCoilPartBlock {
    public static final VoxelShape BASE_SHAPE = createCuboidShape(5, 0, 5, 11, 16, 11);

    public TeslaSecondaryCoilBlock(Settings settings, int weathered) {
        super(settings, weathered);
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
        var controllerPos = pos.offset(Direction.DOWN, 2);
        var blockEntity = world.getBlockEntity(controllerPos);
        if (!(blockEntity instanceof TeslaCoilBlockEntity))
            return;

        TeslaCoilMod.onTeslaCoilEntityCollision((TeslaCoilBlockEntity) blockEntity, entity);
    }
}
