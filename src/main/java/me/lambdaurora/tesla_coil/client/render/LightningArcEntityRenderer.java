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

import me.lambdaurora.tesla_coil.entity.LightningArcEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

/**
 * Represents the lightning electric arc renderer.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class LightningArcEntityRenderer extends EntityRenderer<LightningArcEntity>
{
    private static final float RED = 0.f;
    private static final float GREEN = .45f;
    private static final float BLUE = .9f;
    private static final float OPACITY = .5f;

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

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLightning());
        Matrix4f matrix = matrices.peek().getModel();

        segment(matrix, vertexConsumer, 0.f, 0.f, 0.f, targetX, targetY, targetZ);
    }

    /**
     * Draws a segment of the lightning arc.
     *
     * @param matrix The matrix.
     * @param vertexConsumer The vertex consumer.
     * @param x1 The first X-coordinate of the segment.
     * @param y1 The first Y-coordinate of the segment.
     * @param z1 The first Z-coordinate of the segment.
     * @param x2 The second X-coordinate of the segment.
     * @param y2 The second Y-coordinate of the segment.
     * @param z2 The second Z-coordinate of the segment.
     */
    private static void segment(Matrix4f matrix, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final float thickness = .05f;
        float ly1 = y1 - thickness;
        float hy1 = y1 + thickness;
        float ly2 = y2 - thickness;
        float hy2 = y2 + thickness;

        float lx1 = x1 - thickness;
        float hx1 = x1 + thickness;
        float lx2 = x2 - thickness;
        float hx2 = x2 + thickness;

        float lz1 = z1 - thickness;
        float hz1 = z1 + thickness;
        float lz2 = z2 - thickness;
        float hz2 = z2 + thickness;

        verticalFace(matrix, vertexConsumer, lx1, lz1, lx2, lz2, ly1, hy2, ly2, hy2);
        //verticalFace(matrix, vertexConsumer, hx1, hz1, hx2, hz2, ly1, hy2, ly2, hy2);
    }

    private static void verticalFace(Matrix4f matrix, VertexConsumer vertexConsumer,
                                     float x1, float z1, float x2, float z2,
                                     float ly1, float hy1, float ly2, float hy2)
    {
        vertexConsumer.vertex(matrix, x1, hy1, z1).color(RED, GREEN, BLUE, OPACITY).next();
        vertexConsumer.vertex(matrix, x1, ly1, z1).color(RED, GREEN, BLUE, OPACITY).next();
        vertexConsumer.vertex(matrix, x2, ly2, z2).color(RED, GREEN, BLUE, OPACITY).next();
        vertexConsumer.vertex(matrix, x2, hy2, z2).color(RED, GREEN, BLUE, OPACITY).next();
    }

    @Override
    public Identifier getTexture(LightningArcEntity entity)
    {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
