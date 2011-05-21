package me.qmx.jitescript;

import java.io.PrintStream;
import static me.qmx.jitescript.util.CodegenUtils.*;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author qmx
 */
public class JiteClassTest {

    @Test
    public void testDSL() {
        JiteClass jiteClass = new JiteClass("invokedynamic") {
    public static class DynamicClassLoader extends ClassLoader {

        public Class<?> define(String className, byte[] bytecode) {
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }
    };


            {
                defineMethod("main", ACC_PUBLIC + ACC_STATIC, sig(void.class, String[].class), new MethodBody() {

                    public void executableMethodBody(MethodVisitor mv) {
                        ldc("helloWorld");
                        getstatic(ci(System.class),
                                "out",
                                ci(PrintStream.class));
                        swap();
                        invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class));
                        voidreturn();
                    }
                });
            }
        };

        byte[] classBytes = jiteClass.toBytes();


    }
}
