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

package dev.lambdaurora.tesla_coil.mixin;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import dev.lambdaurora.tesla_coil.TeslaCoilRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Oxidizable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Why is this mixin here and not in the mixin manifest?
 * <p>
 * It's pretty simple, mixin doesn't allow private methods in interface while it's required for this mixin.
 * Now that it can actually be written, now this only waits on a fix in mixin.
 */
@Mixin(Oxidizable.class)
public interface OxidizableMixin {
    /*
    @Dynamic
    @Inject(method = "method_34740()Lcom/google/common/collect/BiMap;", at = @At("RETURN"), remap = false, cancellable = true)
    private static void onBuildLevelIncreasesMap(CallbackInfoReturnable<BiMap<Block, Block>> cir) {
        var val = cir.getReturnValue();
        var newMap = ImmutableBiMap.<Block, Block>builder();
        newMap.putAll(val);
        cir.setReturnValue(TeslaCoilRegistry.hookOxidize(newMap).build());
    }*/
}
