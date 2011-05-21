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

import org.objectweb.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import static me.qmx.jitescript.util.CodegenUtils.*;

/**
 *
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
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, this.className, null, "java/lang/Object", null);
        for(MethodDefinition def : methods){
            MethodVisitor mv = cw.visitMethod(def.getModifiers(), def.getMethodName(), def.getSignature(), null, null);
            mv.visitCode();
            MethodBody methodBody = def.getMethodBody();
            methodBody.setMethodVisitor(mv);
            methodBody.executableMethodBody(mv);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

}
