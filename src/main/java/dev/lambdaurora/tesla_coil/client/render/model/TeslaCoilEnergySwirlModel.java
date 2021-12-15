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

package dev.lambdaurora.tesla_coil.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Represents the tesla coil energy swirl model.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class TeslaCoilEnergySwirlModel extends Model {
	private final ModelPart body;

	public TeslaCoilEnergySwirlModel(ModelPart body) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.body = body;
	}

	public static TexturedModelData buildModel(int power) {
		var data = new ModelData();
		var root = data.getRoot();
		root.addChild(
				"body",
				ModelPartBuilder.create()
						.uv(16, 16)
						.cuboid(4.f, 10.f, 4.f, 8.f, 23.f + ((power - 1) * 16), 8.f),
				ModelTransform.pivot(0.f, 6.f, 0.f)
		);
		return TexturedModelData.of(data, 64, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
