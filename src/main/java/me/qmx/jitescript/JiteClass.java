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

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

/**
 * Represents a Java Class
 *
 * @author qmx
 */
public class JiteClass implements Opcodes {

    public static final String[] INTERFACES = new String[]{};
    private final List<MethodDefinition> methods = new ArrayList<MethodDefinition>();
    private final List<FieldDefinition> fields = new ArrayList<FieldDefinition>();
    private final List<String> interfaces = new ArrayList<String>();
    private final String className;
    private final String superClassName;

    /**
     * Creates a new class representation
     *
     * @param className the desired class name
     */
    public JiteClass(String className) {
        this(className, INTERFACES);
    }

    /**
     * Creates a new class representation
     *
     * @param className  the desired class name
     * @param interfaces the desired java interfaces this class will implement
     */
    public JiteClass(String className, String[] interfaces) {
        this(className, p((Class) Object.class), interfaces);
    }

    /**
     * Creates a new class representation
     *
     * @param className      the desired class name
     * @param superClassName the desired parent class
     * @param interfaces     the desired java interfaces this class will implement
     */
    public JiteClass(String className, String superClassName, String[] interfaces) {
        this.className = className;
        this.superClassName = superClassName;
        for (String anInterface : interfaces) {
            this.interfaces.add(anInterface);
        }
    }

    public String getClassName() {
        return className;
    }

    /**
     * Defines a new method on the target class
     *
     * @param methodName the method name
     * @param modifiers  the modifier bitmask, made by OR'ing constants from ASM's {@link Opcodes} interface
     * @param signature  the method signature, on standard JVM notation
     * @param methodBody the method body
     */
    public void defineMethod(String methodName, int modifiers, String signature, CodeBlock methodBody) {
        this.methods.add(new MethodDefinition(methodName, modifiers, signature, methodBody));
    }

    /**
     * Defines a new field on the target class
     *
     * @param fieldName the field name
     * @param modifiers  the modifier bitmask, made by OR'ing constants from ASM's {@link Opcodes} interface
     * @param signature  the field signature, on standard JVM notation
     * @param value the default value (null for JVM default)
     */
    public void defineField(String fieldName, int modifiers, String signature, Object value) {
        this.fields.add(new FieldDefinition(fieldName, modifiers, signature, value));
    }

    /**
     * Defines a default constructor on the target class
     */
    public void defineDefaultConstructor() {
        defineMethod("<init>", ACC_PUBLIC, sig(void.class),
                newCodeBlock()
                        .aload(0)
                        .invokespecial(p(Object.class), "<init>", sig(void.class))
                        .voidreturn()
        );
    }

    /**
     * Convert this class representation to JDK bytecode
     *
     * @return the bytecode representation
     */
    public byte[] toBytes() {
        return toBytes(JDKVersion.V1_6);
    }

    /**
     * Convert this class representation to JDK bytecode
     *
     * @param version the desired JDK version
     * @return the bytecode representation of this class
     */
    public byte[] toBytes(JDKVersion version) {
        ClassNode node = new ClassNode();
        node.version = version.getVer();
        node.access = ACC_PUBLIC | ACC_SUPER;
        node.name = this.className;
        node.superName = this.superClassName;
        if (!this.interfaces.isEmpty()) {
            node.interfaces.addAll(this.interfaces);
        }

        for (MethodDefinition def : methods) {
            node.methods.add(def.getMethodNode());
        }

        for (FieldDefinition def : fields) {
            node.fields.add(def.getFieldNode());
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(cw);
        return cw.toByteArray();
    }

}
