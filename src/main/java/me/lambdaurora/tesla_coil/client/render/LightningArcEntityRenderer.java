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
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
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
            GL11.GL_TRIANGLES,
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

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(ELECTRIC_ARC);
        Matrix4f matrix = matrices.peek().getModel();

        Vec2f normalVec = new Vec2f((1 / targetX) * THICKNESS, -(1 / targetZ) * THICKNESS);

        segment(matrix, vertexConsumer, normalVec, 0.f, 0.f, 0.f, targetX, targetY, targetZ);
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
    private static void segment(Matrix4f matrix, VertexConsumer vertexConsumer, Vec2f normalVec, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final float thickness = .1f;

        /*v - 1.000000 - 1.000000 1.000000
        v - 1.000000 - 0.401463 1.000000
        v - 1.000000 - 1.000000 0.313960
        v - 1.000000 - 0.430328 0.323077
        v 1.000000 0.515876 - 0.356360
        v 1.000000 1.000000 - 0.357512
        v 1.000000 0.513666 - 1.000000
        v 1.000000 1.000000 - 1.000000 */
        float[] vertices = {
                x1 - normalVec.x, y2 - THICKNESS, z1 - normalVec.y,
                x1 + normalVec.x, y2 - THICKNESS, z1 + normalVec.y,
                x1 - normalVec.x, y2 + THICKNESS, z1 - normalVec.y,
                x1 + normalVec.x, y2 + THICKNESS, z1 + normalVec.y,
        };

        int[] indices = {
                0, 1, 2,
                1, 3, 2
        };

        for (int i = 0; i < indices.length; i++) {
            int index = indices[i] * 3;
            vertex(vertexConsumer, matrix, vertices[index], vertices[index + 1], vertices[index + 2] + 1);
        }

        /*float hx1 = x1 + thickness;
        float hy1 = y1 + thickness;
        float hz1 = z1 + thickness;

        float hx2 = x2 + thickness;
        float hy2 = y2 + thickness;
        float hz2 = z2 + thickness;

        vertex(vertexConsumer, matrix, x2, y2 + thickness, z2);
        vertex(vertexConsumer, matrix, x1, y1 - thickness, z1);
        vertex(vertexConsumer, matrix, x2, y2 - thickness, z2);

        vertex(vertexConsumer, matrix, x2, y2 + thickness, z2);
        vertex(vertexConsumer, matrix, x1, y1 + thickness, z1);
        vertex(vertexConsumer, matrix, x1, y1 - thickness, z1);*/

        /*
         ModelPart.Vertex vertex =  new ModelPart.Vertex(x, y, z, 0.0F, 0.0F); a
         ModelPart.Vertex vertex2 = new ModelPart.Vertex(f, y, z, 0.0F, 8.0F); a
         ModelPart.Vertex vertex3 = new ModelPart.Vertex(f, g, z, 8.0F, 8.0F); a
         ModelPart.Vertex vertex4 = new ModelPart.Vertex(x, g, z, 8.0F, 0.0F); a
         ModelPart.Vertex vertex5 = new ModelPart.Vertex(x, y, h, 0.0F, 0.0F);
         ModelPart.Vertex vertex6 = new ModelPart.Vertex(f, y, h, 0.0F, 8.0F);
         ModelPart.Vertex vertex7 = new ModelPart.Vertex(f, g, h, 8.0F, 8.0F);
         ModelPart.Vertex vertex8 = new ModelPart.Vertex(x, g, h, 8.0F, 0.0F); */

        /*vertex(vertexConsumer, matrix, lx1, ly1, lz1); // 1
        vertex(vertexConsumer, matrix, hx2, lx1, lz1); // 2
        vertex(vertexConsumer, matrix, hx2, hx2, lz1); // 3
        vertex(vertexConsumer, matrix, lx1, hx2, lz1); // 4
        vertex(vertexConsumer, matrix, lx1, ly1, hz2); // 5
        vertex(vertexConsumer, matrix, hx2, lx1, hz2); // 6
        vertex(vertexConsumer, matrix, hx2, hx2, hz2); // 7
        vertex(vertexConsumer, matrix, lx1, hx2, hz2); // 8*/

        // Side 0
        /*vertex(vertexConsumer, matrix, hx2, ly1, hz2);
        vertex(vertexConsumer, matrix, hx2, ly1, lz1);
        vertex(vertexConsumer, matrix, hx2, hy2, lz1);
        vertex(vertexConsumer, matrix, hx2, hy2, hz2);

        // Side 1
        vertex(vertexConsumer, matrix, lx1, ly1, lz1);
        vertex(vertexConsumer, matrix, lx1, ly1, hz2);
        vertex(vertexConsumer, matrix, lx1, hy2, hz2);
        vertex(vertexConsumer, matrix, lx1, hy2, lz1);

        // Side 2
        vertex(vertexConsumer, matrix, hx2, ly1, hz2);
        vertex(vertexConsumer, matrix, lx1, ly1, hz2);
        vertex(vertexConsumer, matrix, lx1, ly1, lz1);
        vertex(vertexConsumer, matrix, hx2, ly1, lz1);

        // Side 3
        vertex(vertexConsumer, matrix, hx2, hy2, lz1);
        vertex(vertexConsumer, matrix, lx1, hy2, lz1);
        vertex(vertexConsumer, matrix, lx1, hy2, hz2);
        vertex(vertexConsumer, matrix, hx2, hy2, hz2);

        // Side 4
        vertex(vertexConsumer, matrix, hx2, ly1, lz1);
        vertex(vertexConsumer, matrix, lx1, ly1, lz1);
        vertex(vertexConsumer, matrix, lx1, hy2, lz1);
        vertex(vertexConsumer, matrix, hx2, hy2, lz1);

        // Side 5
        vertex(vertexConsumer, matrix, lx1, ly1, hz2); // 5
        vertex(vertexConsumer, matrix, hx2, ly1, hz2); // 6
        vertex(vertexConsumer, matrix, hx2, hy2, hz2); // 7
        vertex(vertexConsumer, matrix, lx1, hy2, hz2); // 8

        //verticalFace(matrix, vertexConsumer, lx1, lz1, lx2, lz2, ly1, hy2, ly2, hy2);
        //verticalFace(matrix, vertexConsumer, hx1, hz1, hx2, hz2, ly1, hy2, ly2, hy2);*/
    }

    private static void verticalFace(Matrix4f matrix, VertexConsumer vertexConsumer,
                                     float x1, float z1, float x2, float z2,
                                     float ly1, float hy1, float ly2, float hy2)
    {
        vertexConsumer.vertex(matrix, x2, ly2, z2).color(RED, GREEN, BLUE, OPACITY).next();
        vertexConsumer.vertex(matrix, x2, hy2, z2).color(RED, GREEN, BLUE, OPACITY).next();
        vertexConsumer.vertex(matrix, x1, hy1, z1).color(RED, GREEN, BLUE, OPACITY).next();
        vertexConsumer.vertex(matrix, x1, ly1, z1).color(RED, GREEN, BLUE, OPACITY).next();
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
