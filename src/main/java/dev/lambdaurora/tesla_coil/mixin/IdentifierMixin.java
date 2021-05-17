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

import com.google.common.collect.ImmutableMap;
import dev.lambdaurora.tesla_coil.TeslaCoilMod;
import dev.lambdaurora.tesla_coil.TeslaCoilRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Identifier.class)
public class IdentifierMixin {
    @Shadow
    @Final
    @Mutable
    protected String path;

    private static final Map<String, String> teslacoil$ID_DATAFIX = new ImmutableMap.Builder<String, String>()
            .put("lightly_weathered_tesla_primary_coil", "exposed_tesla_primary_coil")
            .put("semi_weathered_tesla_primary_coil", "weathered_tesla_primary_coil")
            .put("lightly_weathered_tesla_secondary_coil", "exposed_tesla_secondary_coil")
            .put("semi_weathered_tesla_secondary_coil", "weathered_tesla_secondary_coil")
            .put("waxed_lightly_weathered_tesla_primary_coil", "waxed_exposed_tesla_primary_coil")
            .put("waxed_semi_weathered_tesla_primary_coil", "waxed_weathered_tesla_primary_coil")
            .put("waxed_lightly_weathered_tesla_secondary_coil", "waxed_exposed_tesla_secondary_coil")
            .put("waxed_semi_weathered_tesla_secondary_coil", "waxed_weathered_tesla_secondary_coil")
            .build();

    @Inject(method = "<init>([Ljava/lang/String;)V", at = @At("RETURN"))
    private void onInit(String[] id, CallbackInfo ci) {
        if (id[0].equals(TeslaCoilMod.NAMESPACE)) {
            var replacement = teslacoil$ID_DATAFIX.get(id[1]);
            if (replacement != null)
                this.path = replacement;
        }
    }
}
