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

import java.util.ArrayList;
import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

/**
 * @author qmx
 */
public class JiteClass implements Opcodes {

    private final List<MethodDefinition> methods = new ArrayList<MethodDefinition>();
    private final List<String> interfaces = new ArrayList<String>();
    private final String className;

    public JiteClass(String className) {
        this.className = className;
    }

    public JiteClass(String className, String[] interfaces) {
        this.className = className;
        for (String anInterface : interfaces) {
            this.interfaces.add(anInterface);
        }
    }

    public void defineMethod(String methodName, int modifiers, String signature, CodeBlock methodBody) {
        this.methods.add(new MethodDefinition(methodName, modifiers, signature, methodBody));
    }

    public void defineDefaultConstructor() {
        defineMethod("<init>", ACC_PUBLIC, sig(void.class),
                newCodeBlock()
                        .aload(0)
                        .invokespecial(p(Object.class), "<init>", sig(void.class))
                        .voidreturn()
        );
    }

    public byte[] toBytes() {
        ClassNode node = new ClassNode();
        node.version = V1_7;
        node.access = ACC_PUBLIC | ACC_SUPER;
        node.name = this.className;
        node.superName = Type.getInternalName(Object.class);
        if (!this.interfaces.isEmpty()) {
            node.interfaces.addAll(this.interfaces);
        }

        for (MethodDefinition def : methods) {
            node.methods.add(def.getMethodNode());
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(cw);
        return cw.toByteArray();
    }

}
