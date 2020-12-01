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

package me.lambdaurora.tesla_coil.client;

import me.lambdaurora.tesla_coil.TeslaCoilMod;
import me.lambdaurora.tesla_coil.TeslaCoilRegistry;
import me.lambdaurora.tesla_coil.client.render.LightningArcEntityRenderer;
import me.lambdaurora.tesla_coil.client.render.TeslaCoilBlockEntityRenderer;
import me.lambdaurora.tesla_coil.mixin.client.EntityModelLayersAccessor;
import me.lambdaurora.tesla_coil.mixin.client.RenderPhaseAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Represents the Tesla Coils client mod.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class TeslaCoilClientMod implements ClientModInitializer
{
    public static final RenderPhase.Alpha ELECTRIC_ARC_ALPHA = new RenderPhase.Alpha(0.1f);

    public static final RenderLayer SMALL_ELECTRIC_ARC_RENDER_LAYER = RenderLayer.of("tesla_coil_electric_arc", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS, 256, false, true,
            RenderLayer.MultiPhaseParameters.builder()
                    .texture(new RenderPhase.Texture(new Identifier(TeslaCoilMod.NAMESPACE, "textures/entity/electric_arc/random_small.png"), false, false))
                    .fog(RenderPhaseAccessor.getBlackFog())
                    .transparency(RenderPhaseAccessor.getTranslucentTransparency())
                    .diffuseLighting(RenderPhaseAccessor.getEnableDiffuseLighting())
                    .alpha(TeslaCoilClientMod.ELECTRIC_ARC_ALPHA)
                    .cull(RenderPhaseAccessor.getDisableCulling())
                    .lightmap(RenderPhaseAccessor.getEnableLightmap())
                    .overlay(RenderPhaseAccessor.getEnableOverlayColor())
                    .build(false));

    @Override
    public void onInitializeClient()
    {
        BlockEntityRendererRegistry.INSTANCE.register(TeslaCoilRegistry.TESLA_COIL_BLOCK_ENTITY_TYPE, TeslaCoilBlockEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TeslaCoilRegistry.LIGHTNING_ARC_ENTITY_TYPE, LightningArcEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), TeslaCoilRegistry.TESLA_COIL_CONTROLLER_BLOCK);
    }
}
