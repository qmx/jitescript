/**
 *  Copyright 2012 Douglas Campos <qmx@qmx.me>
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

import java.util.ArrayList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * @author qmx
 */
public class MethodDefinition {

    private final String methodName;
    private final int modifiers;
    private final String signature;
    private final CodeBlock methodBody;

    public MethodDefinition(String methodName, int modifiers, String signature, CodeBlock methodBody) {
        this.methodName = methodName;
        this.modifiers = modifiers;
        this.signature = signature;
        this.methodBody = methodBody;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getModifiers() {
        return modifiers;
    }

    public CodeBlock getMethodBody() {
        return methodBody;
    }

    public String getSignature() {
        return signature;
    }

    public MethodNode getMethodNode() {
        MethodNode method = new MethodNode(getModifiers(), getMethodName(), getSignature(), null, null);
        method.visibleAnnotations = new ArrayList<VisibleAnnotation>();
        method.instructions.add(getMethodBody().getInstructionList());
        for (TryCatchBlockNode tryCatchBlockNode : getMethodBody().getTryCatchBlockList()) {
            method.tryCatchBlocks.add(tryCatchBlockNode);
        }
        for (LocalVariableNode localVariableNode : getMethodBody().getLocalVariableList()) {
            method.localVariables.add(localVariableNode);
        }
        for (VisibleAnnotation annotation : methodBody.getAnnotations()) {
            method.visibleAnnotations.add(annotation.getNode());
        }
        return method;
    }
}
