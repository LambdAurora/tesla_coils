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

package me.lambdaurora.tesla_coil.mixin.client;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor
{
    @Accessor("ADDITIVE_TRANSPARENCY")
    static RenderPhase.Transparency getAdditiveTransparency()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("LIGHTNING_TRANSPARENCY")
    static RenderPhase.Transparency getLightningTransparency()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("HALF_ALPHA")
    static RenderPhase.Alpha getHalfAlpha()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("SMOOTH_SHADE_MODEL")
    static RenderPhase.ShadeModel getSmoothShadeModel()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("ENABLE_LIGHTMAP")
    static RenderPhase.Lightmap getEnableLightmap()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("ENABLE_OVERLAY_COLOR")
    static RenderPhase.Overlay getEnableOverlayColor()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("ENABLE_DIFFUSE_LIGHTING")
    static RenderPhase.DiffuseLighting getEnableDiffuseLighting()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("DISABLE_CULLING")
    static RenderPhase.Cull getDisableCulling()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("ALL_MASK")
    static RenderPhase.WriteMaskState getAllMask()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("BLACK_FOG")
    static RenderPhase.Fog getBlackFog()
    {
        throw new IllegalStateException("Injection failed.");
    }

    @Accessor("WEATHER_TARGET")
    static RenderPhase.Target getWeatherTarget()
    {
        throw new IllegalStateException("Injection failed.");
    }
}
