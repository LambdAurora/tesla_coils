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

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.*;

/**
 * @author Gegy
 */
final class OxidizableClassVisitor extends ClassVisitor {
    private static final String OXIDIZABLE = "net.minecraft.class_5955";
    private static final String CREATE_OXIDIZE_MAP = "method_34740";
    private static final String CREATE_OXIDIZE_MAP_DESC = "()Lcom/google/common/collect/BiMap;";

    private static final String MAP_BUILDER = "com/google/common/collect/ImmutableBiMap$Builder";
    private static final String HOOK_DESC = "(L" + MAP_BUILDER + ";)L" + MAP_BUILDER + ";";

    private final String createOxidizeMap;

    public OxidizableClassVisitor(ClassVisitor classVisitor, String createOxidizeMap) {
        super(Opcodes.ASM9, classVisitor);
        this.createOxidizeMap = createOxidizeMap;
    }

    static TransformerMixinPlugin.ClassTransformer createTransformer() {
        FabricLoader loader = FabricLoader.getInstance();
        MappingResolver mappings = loader.getMappingResolver();

        String oxidizable = mappings.mapClassName("intermediary", OXIDIZABLE);
        String createOxidizeMap = mappings.mapMethodName("intermediary", OXIDIZABLE, CREATE_OXIDIZE_MAP, CREATE_OXIDIZE_MAP_DESC);

        return (name, transformedName, bytes) -> {
            if (name.equals(oxidizable)) {
                ClassReader reader = new ClassReader(bytes);
                ClassWriter writer = new ClassWriter(0);

                ClassVisitor visitor = new OxidizableClassVisitor(writer, createOxidizeMap);
                reader.accept(visitor, 0);

                return writer.toByteArray();
            }

            return bytes;
        };
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(this.createOxidizeMap) && descriptor.equals(CREATE_OXIDIZE_MAP_DESC)) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                    if (name.equals("build") && owner.equals(MAP_BUILDER)) {
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, "dev/lambdaurora/tesla_coil/TeslaCoilRegistry", "hookOxidize", HOOK_DESC,
                                false);
                    }
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
