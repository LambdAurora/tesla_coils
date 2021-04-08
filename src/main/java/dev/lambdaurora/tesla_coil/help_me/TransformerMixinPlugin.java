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

package dev.lambdaurora.tesla_coil.help_me;

import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.FabricMixinTransformerProxy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Thanks Gegy for helping me commit crimes.
 * Be gay do crimes
 *
 * @author Gegy
 */
public final class TransformerMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogManager.getLogger(TransformerMixinPlugin.class);

    @Override
    public void onLoad(String mixinPackage) {
        ClassTransformer transformer = OxidizableClassVisitor.createTransformer();

        try {
            this.applyClassTransformer(transformer);
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Failed to apply class transformer!", e);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return ImmutableList.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private void applyClassTransformer(ClassTransformer transformer) throws ReflectiveOperationException {
        ClassLoader loader = this.getClass().getClassLoader();

        Class<?> knotClassLoader = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader");
        Class<?> knotClassDelegate = Class.forName("net.fabricmc.loader.launch.knot.KnotClassDelegate");
        Class<?> mixinEnvironment = MixinEnvironment.class;

        Field delegateField = knotClassLoader.getDeclaredField("delegate");
        delegateField.setAccessible(true);

        Field mixinTransformerField = knotClassDelegate.getDeclaredField("mixinTransformer");
        mixinTransformerField.setAccessible(true);

        Field activeEnvTransformerField = mixinEnvironment.getDeclaredField("transformer");
        activeEnvTransformerField.setAccessible(true);

        Object delegate = delegateField.get(loader);
        Object mixinTransformer = mixinTransformerField.get(delegate);

        if (mixinTransformer == null) {
            throw new IllegalStateException("mixin transformer not yet initialized!");
        }

        Object lastTransformer = activeEnvTransformerField.get(null);

        try {
            activeEnvTransformerField.set(null, null);
            mixinTransformerField.set(delegate, new HookedProxy((FabricMixinTransformerProxy) mixinTransformer, transformer));
        } finally {
            activeEnvTransformerField.set(null, lastTransformer);
        }
    }

    private static class HookedProxy extends FabricMixinTransformerProxy {
        private final FabricMixinTransformerProxy parent;
        private final ClassTransformer transformer;

        HookedProxy(FabricMixinTransformerProxy parent, ClassTransformer transformer) {
            this.parent = parent;
            this.transformer = transformer;
        }

        @Override
        public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
            byte[] bytes = this.parent.transformClassBytes(name, transformedName, basicClass);
            return this.transformer.transform(name, transformedName, bytes);
        }
    }

    interface ClassTransformer {
        byte[] transform(String name, String transformedName, byte[] bytes);
    }
}
