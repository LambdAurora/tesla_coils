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

package me.lambdaurora.tesla_coil;

import me.lambdaurora.tesla_coil.block.entity.TeslaCoilBlockEntity;
import me.lambdaurora.tesla_coil.entity.damage.TeslaCoilDamageSource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class TeslaCoilMod implements ModInitializer
{
    public static final String NAMESPACE = "tesla_coil";
    public static final float TESLA_COIL_COMPONENTS_ACTIVE_DAMAGE = 4.5f;
    public static final Tag<Item> TESLA_COIL_IMMUNE_ITEMS = TagRegistry.item(new Identifier(NAMESPACE, "tesla_coil_immune"));
    public static final DamageSource TESLA_COIL_DAMAGE = new TeslaCoilDamageSource();

    @Override
    public void onInitialize()
    {
        TeslaCoilRegistry.init();
    }

    public static void onTeslaCoilEntityCollision(TeslaCoilBlockEntity blockEntity, Entity entity)
    {
        if (blockEntity.isEnabled()) {
            if (entity instanceof ItemEntity && ((ItemEntity) entity).getStack().isIn(TeslaCoilMod.TESLA_COIL_IMMUNE_ITEMS))
                return;
            entity.damage(TeslaCoilMod.TESLA_COIL_DAMAGE, TeslaCoilMod.TESLA_COIL_COMPONENTS_ACTIVE_DAMAGE);
        }
    }
}
