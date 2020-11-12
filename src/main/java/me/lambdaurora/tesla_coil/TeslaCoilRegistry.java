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

package me.lambdaurora.tesla_coil;

import me.lambdaurora.tesla_coil.block.TeslaCoilControllerBlock;
import me.lambdaurora.tesla_coil.block.TeslaCoilTopLoadBlock;
import me.lambdaurora.tesla_coil.block.TeslaPrimaryCoilBlock;
import me.lambdaurora.tesla_coil.block.TeslaSecondaryCoilBlock;
import me.lambdaurora.tesla_coil.block.entity.TeslaCoilBlockEntity;
import me.lambdaurora.tesla_coil.entity.LightningArcEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the mod registry.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public final class TeslaCoilRegistry
{
    public static final TeslaCoilTopLoadBlock TESLA_COIL_TOP_LOAD_BLOCK = register("tesla_coil_top_load",
            new TeslaCoilTopLoadBlock(FabricBlockSettings.of(Material.METAL, MapColor.GOLD)
                    .sounds(BlockSoundGroup.COPPER)
                    .strength(3.f)));
    public static final TeslaSecondaryCoilBlock TESLA_SECONDARY_COIL_BLOCK = register("tesla_coil_secondary",
            new TeslaSecondaryCoilBlock(FabricBlockSettings.of(Material.METAL, MapColor.GOLD)
                    .sounds(BlockSoundGroup.COPPER)
                    .strength(2.5f)));
    public static final TeslaPrimaryCoilBlock TESLA_PRIMARY_COIL_BLOCK = register("tesla_coil_primary",
            new TeslaPrimaryCoilBlock(FabricBlockSettings.of(Material.METAL, MapColor.GOLD)
                    .sounds(BlockSoundGroup.COPPER)
                    .strength(2.5f)));
    public static final TeslaCoilControllerBlock TESLA_COIL_CONTROLLER_BLOCK = register("tesla_coil_controller",
            new TeslaCoilControllerBlock(FabricBlockSettings.of(Material.METAL, MapColor.RED)
                    .sounds(BlockSoundGroup.COPPER)
                    .strength(3.f)));

    public static final BlockItem TESLA_COIL_TOP_LOAD_ITEM = register("tesla_coil_top_load", new BlockItem(TESLA_COIL_TOP_LOAD_BLOCK,
            new FabricItemSettings().group(ItemGroup.REDSTONE)));
    public static final BlockItem TESLA_SECONDARY_COIL_ITEM = register("tesla_coil_secondary",
            new BlockItem(TESLA_SECONDARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    public static final BlockItem TESLA_PRIMARY_COIL_ITEM = register("tesla_coil_primary",
            new BlockItem(TESLA_PRIMARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    public static final BlockItem TESLA_COIL_CONTROLLER_ITEM = register("tesla_coil_controller",
            new BlockItem(TESLA_COIL_CONTROLLER_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));

    public static final BlockEntityType<TeslaCoilBlockEntity> TESLA_COIL_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
            new Identifier("tesla_coil", "tesla_coil"),
            FabricBlockEntityTypeBuilder.create(TeslaCoilBlockEntity::new, TESLA_COIL_CONTROLLER_BLOCK).build(null));

    public static final EntityType<LightningArcEntity> LIGHTNING_ARC_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(TeslaCoilMod.NAMESPACE, "lightning_arc"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LightningArcEntity::new)
                    .disableSaving()
                    .disableSummon()
                    .trackRangeChunks(16)
                    .dimensions(EntityDimensions.fixed(0.f, 0.f))
                    .trackedUpdateRate(Integer.MAX_VALUE)
                    .build());

    protected static <T extends Block> T register(@NotNull String name, @NotNull T block)
    {
        Registry.register(Registry.BLOCK, new Identifier(TeslaCoilMod.NAMESPACE, name), block);
        return block;
    }

    protected static <T extends Item> T register(@NotNull String name, @NotNull T item)
    {
        Registry.register(Registry.ITEM, new Identifier(TeslaCoilMod.NAMESPACE, name), item);
        return item;
    }

    public static void init()
    {
    }
}
