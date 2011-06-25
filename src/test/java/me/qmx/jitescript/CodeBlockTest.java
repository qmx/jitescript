package me.qmx.jitescript;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;

import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class CodeBlockTest {

    @Test
    public void localVariableTest() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        final JiteClass jiteClass = new JiteClass("LOL") {{
            defineMethod("test", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, sig(Object.class),
                    new CodeBlock() {{
                        newobj(p(Object.class));
                        dup();
                        invokespecial(p(Object.class), "<init>", "()V");
                        localVariable("foo", ci(Object.class));
                        aload(0);
                        areturn();
                    }});
        }};
        final byte[] classBytes = jiteClass.toBytes();
        final JiteClassTest.DynamicClassLoader dynamicClassLoader = new JiteClassTest.DynamicClassLoader();
        final Class<?> lol = dynamicClassLoader.define("LOL", classBytes);
        final Object result = lol.getDeclaredMethod("test").invoke(null);
        System.out.println(result);
    }
}
