/**
 *  Copyright 2011 Douglas Campos <qmx@qmx.me>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.qmx.jitescript;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qmx
 */
public class JiteClass implements Opcodes {

    private final List<MethodDefinition> methods = new ArrayList<MethodDefinition>();
    private final String className;

    public JiteClass(String className) {
        this.className = className;
    }

    public void defineMethod(String methodName, int modifiers, String signature, MethodBody methodBody) {
        this.methods.add(new MethodDefinition(methodName, modifiers, signature, methodBody));
    }

    byte[] toBytes() {
        ClassNode node = new ClassNode();
        node.version = V1_7;
        node.access = ACC_PUBLIC + ACC_SUPER;
        node.name = this.className;
        node.superName = Type.getInternalName(Object.class);

        for (MethodDefinition def : methods) {
            MethodNode method = new MethodNode(def.getModifiers(), def.getMethodName(), def.getSignature(), null, null);
            method.instructions.add(def.getMethodBody().getInstructionList());
            for (TryCatchBlockNode tryCatchBlockNode : def.getMethodBody().getTryCatchBlockList()) {
                method.tryCatchBlocks.add(tryCatchBlockNode);
            }
            for (LocalVariableNode localVariableNode : def.getMethodBody().getLocalVariableList()) {
                method.localVariables.add(localVariableNode);
            }

            node.methods.add(method);
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(cw);
        return cw.toByteArray();
    }

}
