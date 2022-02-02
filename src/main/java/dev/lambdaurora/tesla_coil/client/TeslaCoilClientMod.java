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

package dev.lambdaurora.tesla_coil.client;

import dev.lambdaurora.tesla_coil.TeslaCoilRegistry;
import dev.lambdaurora.tesla_coil.client.render.LightningArcEntityRenderer;
import dev.lambdaurora.tesla_coil.client.render.TeslaCoilBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

/**
 * Represents the Tesla Coils client mod.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class TeslaCoilClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(TeslaCoilRegistry.TESLA_COIL_BLOCK_ENTITY_TYPE, TeslaCoilBlockEntityRenderer::new);
		EntityRendererRegistry.register(TeslaCoilRegistry.LIGHTNING_ARC_ENTITY_TYPE, LightningArcEntityRenderer::new);
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), TeslaCoilRegistry.TESLA_COIL_CONTROLLER_BLOCK);
	}
}
