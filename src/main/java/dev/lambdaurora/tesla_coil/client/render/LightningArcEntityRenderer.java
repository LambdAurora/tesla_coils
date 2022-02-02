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

package dev.lambdaurora.tesla_coil.client.render;

import dev.lambdaurora.tesla_coil.TeslaCoilMod;
import dev.lambdaurora.tesla_coil.entity.LightningArcEntity;
import dev.lambdaurora.tesla_coil.mixin.client.RenderPhaseAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

/**
 * Represents the lightning electric arc renderer.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class LightningArcEntityRenderer extends EntityRenderer<LightningArcEntity> {
	private static final RenderLayer ELECTRIC_ARC = RenderLayer.of(
			TeslaCoilMod.NAMESPACE + "__lightning_arc",
			VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.TRIANGLES,
			256,
			false,
			true,
			RenderLayer.MultiPhaseParameters.builder()
					.writeMaskState(RenderPhaseAccessor.getAllMask())
					.transparency(RenderPhaseAccessor.getLightningTransparency())
					.target(RenderPhaseAccessor.getWeatherTarget())
					.shader(RenderPhaseAccessor.getLightningShader())
					.build(false)
	);
	private static final float RED = 0.f;
	private static final float GREEN = .45f;
	private static final float BLUE = .9f;
	private static final float OPACITY = .75f;
	private static final float THICKNESS = .1f;
	private static final int[] INDICES = {
			/* BOTTOM FACE */
			0, 2, 3,
			0, 1, 2,
			/* END FACE */
			6, 2, 1,
			1, 5, 6,
			/* EAST FACE */
			4, 5, 0,
			5, 1, 0,
			/* START FACE */
			0, 3, 7,
			7, 4, 0,
			/* WEST FACE */
			3, 6, 7,
			3, 2, 6,
			/* TOP FACE */
			7, 6, 4,
			6, 5, 4
	};

	public LightningArcEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(LightningArcEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		Vec3d target = entity.getTarget();
		if (target == null) return;

		var random = new Random(entity.getId() + entity.age / 2);

		float targetX = (float) target.getX();
		float targetY = (float) target.getY();
		float targetZ = (float) target.getZ();

		matrices.push();

		var vertexConsumer = vertexConsumers.getBuffer(ELECTRIC_ARC);
		var matrix = matrices.peek().getModel();

		float distance = MathHelper.sqrt(targetX * targetX + targetZ * targetZ);
		float deltaY = Math.abs(targetY);

		double angle = Math.atan2(targetX, targetZ) - (Math.PI / 2.0);
		matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float) angle));

		if (targetY < 0)
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.f));

		int steps = 1;
		if (distance > 1.f) steps = (int) Math.floor(Math.max(distance - 1, 2));
		float zOffset = 0;
		float start = 0.f;
		float currentY = 0.f;
		final float step = distance / steps;
		final float yStep = deltaY / steps;
		for (int i = 0; i < steps; i++) {
			float previousZOffset = zOffset;
			boolean end = i == steps - 1;
			if (end)
				zOffset = 0.f;
			else
				zOffset = MathHelper.clamp((random.nextBoolean() ? 1.f : -1.f) * random.nextFloat(), -.5f, .5f);
			float nextY = deltaY;
			if (!end)
				nextY = (i + 1) * yStep + MathHelper.clamp((random.nextBoolean() ? 1.f : -1.f) * random.nextFloat(), -.35f, .35f);
			segment(matrix, vertexConsumer, start, step, currentY, nextY, previousZOffset, zOffset, i == 0, end);
			start += step;
			currentY = nextY;
		}

		matrices.pop();
	}

	/**
	 * Draws a segment of the lightning arc.
	 *
	 * @param matrix the matrix
	 * @param vertexConsumer the vertex consumer
	 * @param y1 the first Y-coordinate of the segment
	 * @param y2 the second Y-coordinate of the segment
	 */
	private static void segment(Matrix4f matrix, VertexConsumer vertexConsumer, float start, float distance, float y1, float y2, float startZOffset, float endZOffset,
	                            boolean drawStart, boolean drawEnd) {
		float x2 = start + distance;
		float[] vertices = {
				/* BOTTOM */
				start, y1 - THICKNESS, startZOffset - THICKNESS, // 0
				x2, y2 - THICKNESS, endZOffset - THICKNESS, // 1
				x2, y2 - THICKNESS, endZOffset + THICKNESS, // 2
				start, y1 - THICKNESS, startZOffset + THICKNESS, // 3
				/* TOP */
				start, y1 + THICKNESS, startZOffset - THICKNESS, // 4
				x2, y2 + THICKNESS, endZOffset - THICKNESS, // 5
				x2, y2 + THICKNESS, endZOffset + THICKNESS, // 6
				start, y1 + THICKNESS, startZOffset + THICKNESS // 7
		};

		for (int iIndex = 0; iIndex < INDICES.length; iIndex++) {
			if ((!drawStart && iIndex >= 18 && iIndex <= 23) || (!drawEnd && iIndex >= 6 && iIndex <= 11))
				continue;
			int i = INDICES[iIndex] * 3;
			vertex(vertexConsumer, matrix, vertices[i], vertices[i + 1], vertices[i + 2]);
		}
	}

	private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float z) {
		vertexConsumer.vertex(matrix, x, y, z).color(RED, GREEN, BLUE, OPACITY).next();
	}

	@Override
	public Identifier getTexture(LightningArcEntity entity) {
		return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
	}
}
