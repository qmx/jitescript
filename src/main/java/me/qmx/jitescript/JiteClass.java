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
