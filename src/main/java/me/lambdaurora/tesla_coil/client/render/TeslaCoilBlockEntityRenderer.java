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

package me.lambdaurora.tesla_coil.client.render;

import me.lambdaurora.tesla_coil.block.entity.TeslaCoilBlockEntity;
import me.lambdaurora.tesla_coil.client.render.model.TeslaCoilEnergySwirlModel;
import me.lambdaurora.tesla_coil.mixin.client.RenderPhaseAccessor;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

/**
 * Represents the tesla coil block entity renderer.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class TeslaCoilBlockEntityRenderer extends BlockEntityRenderer<TeslaCoilBlockEntity>
{
    private static final Identifier ENERGY_SWIRL_TEXTURE = new Identifier("textures/entity/creeper/creeper_armor.png");
    private final TeslaCoilEnergySwirlModel model = new TeslaCoilEnergySwirlModel();

    public TeslaCoilBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(TeslaCoilBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        if (!entity.isEnabled()) return;

        float partialAge = entity.getAge() + tickDelta;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getEnergySwirl(ENERGY_SWIRL_TEXTURE, this.getEnergySwirlX(partialAge), partialAge * 0.01f));
        this.model.render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 0.5f, 0.5f, 0.5f, 1.f);

        Matrix4f matrix = matrices.peek().getModel();
        this.renderSmallArc(vertexConsumer, matrix, 0.75f, 0.5f, 1.5f, 0.5f);
    }

    protected void renderSmallArc(VertexConsumer vertexConsumer, Matrix4f matrix, float x1, float z1, float x2, float z2)
    {
        if (x1 > x2) {
            float tmp = x2;
            x2 = x1;
            x1 = tmp;
        }
        if (z1 > z2) {
            float tmp = z2;
            z2 = z1;
            z1 = tmp;
        }

        float[] vertices = {
                x1, 0.5f, z1,  16, 16,
                x1, 0.f, z1,   16, 32,
                x2, 0.f, z2,   32, 32,
                x2, 0.5f, z2,  32, 16
        };

        for (int i = 0; i < 4; i++) {
            int start = i * 5;
            Vector4f vec = new Vector4f(vertices[start], vertices[start + 1], vertices[start + 2], 1.f);
            vec.transform(matrix);

            vertexConsumer.vertex(vec.getX(), vec.getY(), vec.getZ(),
                    0.5f, 0.5f, 0.5f, 1.f,
                    vertices[start + 3], vertices[start + 4],
                    OverlayTexture.DEFAULT_UV,
                    15728880,
                    0.f, 0.f, 1.f);
        }
    }

    protected float getEnergySwirlX(float partialAge)
    {
        return partialAge * 0.01f;
    }

    public static RenderLayer getEnergySwirl(@NotNull Identifier texture, float x, float y)
    {
        return RenderLayer.of("tesla_coil_energy_swirl", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                GL11.GL_QUADS, 256, false, true,
                RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false, false))
                        .texturing(new RenderPhase.OffsetTexturing(x, y))
                        .fog(RenderPhaseAccessor.getBlackFog())
                        .transparency(RenderPhaseAccessor.getAdditiveTransparency())
                        .diffuseLighting(RenderPhaseAccessor.getEnableDiffuseLighting())
                        .alpha(new RenderPhase.Alpha(0.75f)) // @TODO constant
                        .cull(RenderPhaseAccessor.getDisableCulling())
                        .lightmap(RenderPhaseAccessor.getEnableLightmap())
                        .overlay(RenderPhaseAccessor.getEnableOverlayColor())
                        .build(false));
    }
}
