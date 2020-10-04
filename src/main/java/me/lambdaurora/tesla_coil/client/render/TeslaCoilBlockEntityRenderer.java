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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

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
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(ENERGY_SWIRL_TEXTURE, this.getEnergySwirlX(partialAge), partialAge * 0.01f));
        this.model.render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 0.5f, 0.5f, 0.5f, 1.0f);
    }

    protected float getEnergySwirlX(float partialAge)
    {
        return partialAge * 0.01f;
    }
}
