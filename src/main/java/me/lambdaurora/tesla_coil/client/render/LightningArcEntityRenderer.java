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

import me.lambdaurora.tesla_coil.TeslaCoilMod;
import me.lambdaurora.tesla_coil.entity.LightningArcEntity;
import me.lambdaurora.tesla_coil.mixin.client.RenderPhaseAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * Represents the lightning electric arc renderer.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class LightningArcEntityRenderer extends EntityRenderer<LightningArcEntity>
{
    private static final RenderLayer ELECTRIC_ARC = RenderLayer.of(
            TeslaCoilMod.NAMESPACE + "__lightning_arc",
            VertexFormats.POSITION_COLOR,
            GL11.GL_QUADS,
            256,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .writeMaskState(RenderPhaseAccessor.getAllMask())
                    .transparency(RenderPhaseAccessor.getLightningTransparency())
                    .target(RenderPhaseAccessor.getWeatherTarget())
                    .shadeModel(RenderPhaseAccessor.getSmoothShadeModel())
                    .build(false)
    );
    private static final float RED = 0.f;
    private static final float GREEN = .45f;
    private static final float BLUE = .9f;
    private static final float OPACITY = .75f;
    private static final float THICKNESS = .1f;

    public LightningArcEntityRenderer(EntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(LightningArcEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
    {
        BlockPos target = entity.getTarget();
        if (target == null) return;

        float targetX = (float) (target.getX() + .5 - entity.getX());
        float targetY = (float) (target.getY() + .5 - entity.getY());
        float targetZ = (float) (target.getZ() + .5 - entity.getZ());

        matrices.push();

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(ELECTRIC_ARC);
        Matrix4f matrix = matrices.peek().getModel();

        segment(matrix, vertexConsumer, 0.f, 0.f, 0.f, MathHelper.abs(targetX), targetY, MathHelper.abs(targetZ));

        matrices.pop();
    }

    /**
     * Draws a segment of the lightning arc.
     *
     * @param matrix The matrix.
     * @param vertexConsumer The vertex consumer.
     * @param x1 The first X-coordinate of the segment.
     * @param y1 The first Y-coordinate of the segment.
     * @param x2 The second X-coordinate of the segment.
     * @param y2 The second Y-coordinate of the segment.
     */
    private static void segment(Matrix4f matrix, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        float[] vertices = {
                x1, y1 - THICKNESS, z1 - THICKNESS, // 0
                x2, y2 - THICKNESS, z2 - THICKNESS, // 1
                x2, y2 + THICKNESS, z2 - THICKNESS, // 2
                x1, y1 + THICKNESS, z1 - THICKNESS, // 3
                x1, y1 - THICKNESS, z1 + THICKNESS, // 4
                x2, y2 - THICKNESS, z2 + THICKNESS, // 5
                x2, y2 + THICKNESS, z2 + THICKNESS, // 6
                x1, y1 + THICKNESS, z1 + THICKNESS // 7
        };

        int[] indices = {
                5, 1, 2, 6,
                0, 4, 7, 3,
                5, 4, 0, 2,
                2, 3, 7, 6,
                1, 0, 3, 2,
                4, 5, 6, 7
        };

        for (int i : indices) {
            i *= 3;
            vertex(vertexConsumer, matrix, vertices[i], vertices[i + 1], vertices[i + 2]);
        }
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float z)
    {
        vertexConsumer.vertex(matrix, x, y, z).color(RED, GREEN, BLUE, OPACITY).next();
    }

    @Override
    public Identifier getTexture(LightningArcEntity entity)
    {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
