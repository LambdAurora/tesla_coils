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

package dev.lambdaurora.tesla_coil;

import dev.lambdaurora.tesla_coil.block.*;
import dev.lambdaurora.tesla_coil.block.entity.TeslaCoilBlockEntity;
import dev.lambdaurora.tesla_coil.entity.LightningArcEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.List;

/**
 * Represents the mod registry.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public final class TeslaCoilRegistry {
	public static final TeslaCoilTopLoadBlock TESLA_COIL_TOP_LOAD_BLOCK = registerWithItem("tesla_coil_top_load",
			new TeslaCoilTopLoadBlock(QuiltBlockSettings.of(Material.METAL, MapColor.GOLD)
					.sounds(BlockSoundGroup.COPPER)
					.strength(3.f)), ItemGroup.REDSTONE);
	public static final TeslaCoilControllerBlock TESLA_COIL_CONTROLLER_BLOCK = registerWithItem("tesla_coil_controller",
			new TeslaCoilControllerBlock(QuiltBlockSettings.of(Material.METAL, MapColor.RED)
					.sounds(BlockSoundGroup.COPPER)
					.strength(3.f)), ItemGroup.REDSTONE);

	/* Tesla Primary Coil */

	public static final OxidizableTeslaPrimaryCoilBlock OXIDIZED_TESLA_PRIMARY_COIL_BLOCK = register("oxidized_tesla_primary_coil",
			new OxidizableTeslaPrimaryCoilBlock(Oxidizable.OxidizationLevel.OXIDIZED, QuiltBlockSettings.of(Material.METAL, MapColor.TEAL)
					.sounds(BlockSoundGroup.COPPER)
					.strength(2.5f)));
	public static final OxidizableTeslaPrimaryCoilBlock WEATHERED_TESLA_PRIMARY_COIL_BLOCK = register("weathered_tesla_primary_coil",
			new OxidizableTeslaPrimaryCoilBlock(Oxidizable.OxidizationLevel.WEATHERED, QuiltBlockSettings.copy(OXIDIZED_TESLA_PRIMARY_COIL_BLOCK)
					.mapColor(MapColor.LIGHT_GRAY)));
	public static final OxidizableTeslaPrimaryCoilBlock EXPOSED_TESLA_PRIMARY_COIL_BLOCK = register("exposed_tesla_primary_coil",
			new OxidizableTeslaPrimaryCoilBlock(Oxidizable.OxidizationLevel.EXPOSED, QuiltBlockSettings.copy(OXIDIZED_TESLA_PRIMARY_COIL_BLOCK)
					.mapColor(MapColor.LIGHT_GRAY)));
	public static final OxidizableTeslaPrimaryCoilBlock TESLA_PRIMARY_COIL_BLOCK = register("tesla_primary_coil",
			new OxidizableTeslaPrimaryCoilBlock(Oxidizable.OxidizationLevel.UNAFFECTED, QuiltBlockSettings.copy(OXIDIZED_TESLA_PRIMARY_COIL_BLOCK)
					.mapColor(MapColor.ORANGE)));

	public static final BlockItem TESLA_PRIMARY_COIL_ITEM = register("tesla_primary_coil",
			new BlockItem(TESLA_PRIMARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
	public static final BlockItem EXPOSED_TESLA_PRIMARY_COIL_ITEM = register("exposed_tesla_primary_coil",
			new BlockItem(EXPOSED_TESLA_PRIMARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
	public static final BlockItem WEATHERED_TESLA_PRIMARY_COIL_ITEM = register("weathered_tesla_primary_coil",
			new BlockItem(WEATHERED_TESLA_PRIMARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
	public static final BlockItem OXIDIZED_TESLA_PRIMARY_COIL_ITEM = register("oxidized_tesla_primary_coil",
			new BlockItem(OXIDIZED_TESLA_PRIMARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));

	public static final TeslaPrimaryCoilBlock WAXED_TESLA_PRIMARY_COIL_BLOCK = registerWithItem("waxed_tesla_primary_coil",
			new TeslaPrimaryCoilBlock(QuiltBlockSettings.copy(TESLA_PRIMARY_COIL_BLOCK), 0), ItemGroup.REDSTONE);
	public static final TeslaPrimaryCoilBlock WAXED_EXPOSED_TESLA_PRIMARY_COIL_BLOCK = registerWithItem("waxed_exposed_tesla_primary_coil",
			new TeslaPrimaryCoilBlock(QuiltBlockSettings.copy(EXPOSED_TESLA_PRIMARY_COIL_BLOCK), 1), ItemGroup.REDSTONE);
	public static final TeslaPrimaryCoilBlock WAXED_WEATHERED_TESLA_PRIMARY_COIL_BLOCK = registerWithItem("waxed_weathered_tesla_primary_coil",
			new TeslaPrimaryCoilBlock(QuiltBlockSettings.copy(WEATHERED_TESLA_PRIMARY_COIL_BLOCK), 2), ItemGroup.REDSTONE);
	public static final TeslaPrimaryCoilBlock WAXED_OXIDIZED_TESLA_PRIMARY_COIL_BLOCK = registerWithItem("waxed_oxidized_tesla_primary_coil",
			new TeslaPrimaryCoilBlock(QuiltBlockSettings.copy(WEATHERED_TESLA_PRIMARY_COIL_BLOCK), 3), ItemGroup.REDSTONE);

	/* Tesla Secondary Coil */

	public static final OxidizableTeslaSecondaryCoilBlock OXIDIZED_TESLA_SECONDARY_COIL_BLOCK = register("oxidized_tesla_secondary_coil",
			new OxidizableTeslaSecondaryCoilBlock(Oxidizable.OxidizationLevel.OXIDIZED, QuiltBlockSettings.copy(OXIDIZED_TESLA_PRIMARY_COIL_BLOCK)
					.mapColor(MapColor.TEAL)));
	public static final OxidizableTeslaSecondaryCoilBlock WEATHERED_TESLA_SECONDARY_COIL_BLOCK = register("weathered_tesla_secondary_coil",
			new OxidizableTeslaSecondaryCoilBlock(Oxidizable.OxidizationLevel.WEATHERED, QuiltBlockSettings.copy(OXIDIZED_TESLA_SECONDARY_COIL_BLOCK)
					.mapColor(MapColor.DARK_AQUA)));
	public static final OxidizableTeslaSecondaryCoilBlock EXPOSED_TESLA_SECONDARY_COIL_BLOCK = register("exposed_tesla_secondary_coil",
			new OxidizableTeslaSecondaryCoilBlock(Oxidizable.OxidizationLevel.EXPOSED, QuiltBlockSettings.copy(OXIDIZED_TESLA_SECONDARY_COIL_BLOCK)
					.mapColor(MapColor.LIGHT_GRAY)));
	public static final OxidizableTeslaSecondaryCoilBlock TESLA_SECONDARY_COIL_BLOCK = register("tesla_secondary_coil",
			new OxidizableTeslaSecondaryCoilBlock(Oxidizable.OxidizationLevel.UNAFFECTED, QuiltBlockSettings.copy(OXIDIZED_TESLA_SECONDARY_COIL_BLOCK)
					.mapColor(MapColor.ORANGE)));

	public static final BlockItem TESLA_SECONDARY_COIL_ITEM = register("tesla_secondary_coil",
			new BlockItem(TESLA_SECONDARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
	public static final BlockItem EXPOSED_TESLA_SECONDARY_COIL_ITEM = register("exposed_tesla_secondary_coil",
			new BlockItem(EXPOSED_TESLA_SECONDARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
	public static final BlockItem WEATHERED_TESLA_SECONDARY_COIL_ITEM = register("weathered_tesla_secondary_coil",
			new BlockItem(WEATHERED_TESLA_SECONDARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
	public static final BlockItem OXIDIZED_TESLA_SECONDARY_COIL_ITEM = register("oxidized_tesla_secondary_coil",
			new BlockItem(OXIDIZED_TESLA_SECONDARY_COIL_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));

	public static final TeslaSecondaryCoilBlock WAXED_TESLA_SECONDARY_COIL_BLOCK = registerWithItem("waxed_tesla_secondary_coil",
			new TeslaSecondaryCoilBlock(QuiltBlockSettings.copy(TESLA_SECONDARY_COIL_BLOCK), 0), ItemGroup.REDSTONE);
	public static final TeslaSecondaryCoilBlock WAXED_EXPOSED_TESLA_SECONDARY_COIL_BLOCK = registerWithItem("waxed_exposed_tesla_secondary_coil",
			new TeslaSecondaryCoilBlock(QuiltBlockSettings.copy(EXPOSED_TESLA_SECONDARY_COIL_BLOCK), 1), ItemGroup.REDSTONE);
	public static final TeslaSecondaryCoilBlock WAXED_WEATHERED_TESLA_SECONDARY_COIL_BLOCK = registerWithItem("waxed_weathered_tesla_secondary_coil",
			new TeslaSecondaryCoilBlock(QuiltBlockSettings.copy(WEATHERED_TESLA_SECONDARY_COIL_BLOCK), 2), ItemGroup.REDSTONE);
	public static final TeslaSecondaryCoilBlock WAXED_OXIDIZED_TESLA_SECONDARY_COIL_BLOCK = registerWithItem("waxed_oxidized_tesla_secondary_coil",
			new TeslaSecondaryCoilBlock(QuiltBlockSettings.copy(TESLA_SECONDARY_COIL_BLOCK), 3), ItemGroup.REDSTONE);

	public static final BlockEntityType<TeslaCoilBlockEntity> TESLA_COIL_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
			TeslaCoilMod.id("tesla_coil"),
			FabricBlockEntityTypeBuilder.create(TeslaCoilBlockEntity::new, TESLA_COIL_CONTROLLER_BLOCK).build(null));

	public static final EntityType<LightningArcEntity> LIGHTNING_ARC_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
			TeslaCoilMod.id("lightning_arc"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, LightningArcEntity::new)
					.disableSaving()
					.disableSummon()
					.trackRangeChunks(16)
					.dimensions(EntityDimensions.fixed(0.f, 0.f))
					.trackedUpdateRate(Integer.MAX_VALUE)
					.build());

	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, TeslaCoilMod.id(name), block);
		return block;
	}

	private static <T extends Block> T registerWithItem(String name, T block, ItemGroup group) {
		Registry.register(Registry.BLOCK, TeslaCoilMod.id(name), block);
		register(name, new BlockItem(block, new FabricItemSettings().group(group)));
		return block;
	}

	private static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, TeslaCoilMod.id(name), item);
		return item;
	}

	public static void init() {
		registerOxidizableStuff(List.of(
				new CoilStagePair(TESLA_PRIMARY_COIL_BLOCK, WAXED_TESLA_PRIMARY_COIL_BLOCK),
				new CoilStagePair(EXPOSED_TESLA_PRIMARY_COIL_BLOCK, WAXED_EXPOSED_TESLA_PRIMARY_COIL_BLOCK),
				new CoilStagePair(WEATHERED_TESLA_PRIMARY_COIL_BLOCK, WAXED_WEATHERED_TESLA_PRIMARY_COIL_BLOCK),
				new CoilStagePair(OXIDIZED_TESLA_PRIMARY_COIL_BLOCK, WAXED_OXIDIZED_TESLA_PRIMARY_COIL_BLOCK)
		));
		registerOxidizableStuff(List.of(
				new CoilStagePair(TESLA_SECONDARY_COIL_BLOCK, WAXED_TESLA_SECONDARY_COIL_BLOCK),
				new CoilStagePair(EXPOSED_TESLA_SECONDARY_COIL_BLOCK, WAXED_EXPOSED_TESLA_SECONDARY_COIL_BLOCK),
				new CoilStagePair(WEATHERED_TESLA_SECONDARY_COIL_BLOCK, WAXED_WEATHERED_TESLA_SECONDARY_COIL_BLOCK),
				new CoilStagePair(OXIDIZED_TESLA_SECONDARY_COIL_BLOCK, WAXED_OXIDIZED_TESLA_SECONDARY_COIL_BLOCK)
		));
	}

	private static void registerOxidizableStuff(List<CoilStagePair> stages) {
		for (int i = 0; i < stages.size() - 1; i++) {
			var first = stages.get(i);
			var second = stages.get(i + 1);
			OxidizableBlocksRegistry.registerOxidizableBlockPair(first.normal(), second.normal());
			OxidizableBlocksRegistry.registerWaxableBlockPair(first.normal(), first.waxed());
		}

		var lastPair = stages.get(stages.size() - 1);
		OxidizableBlocksRegistry.registerWaxableBlockPair(lastPair.normal(), lastPair.waxed());
	}

	record CoilStagePair(Block normal, Block waxed) {
	}
}
