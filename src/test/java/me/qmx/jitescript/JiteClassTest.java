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

import org.junit.Assert;
import org.junit.Test;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author qmx
 */
public class JiteClassTest {

    public static class DynamicClassLoader extends ClassLoader {
        public Class<?> define(JiteClass jiteClass) {
            byte[] classBytes = jiteClass.toBytes();
            return super.defineClass(jiteClass.getClassName(), classBytes, 0, classBytes.length);
        }
    }

    @Test
    public void testDSL() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final String className = "helloTest";
        JiteClass jiteClass = new JiteClass(className) {{
            // you can use the pre-constructor style
            defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(void.class, String[].class), new CodeBlock() {{
                ldc("helloWorld");
                getstatic(p(System.class), "out", ci(PrintStream.class));
                swap();
                invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class));
                voidreturn();
            }});
            // or use chained api
            defineMethod("hello", ACC_PUBLIC | ACC_STATIC, sig(String.class),
                    newCodeBlock()
                            .ldc("helloWorld")
                            .areturn()
            );

        }};

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Method helloMethod = clazz.getMethod("hello");
        Object result = helloMethod.invoke(null);
        Assert.assertEquals("helloWorld", result);

        Method mainMethod = clazz.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) new String[]{});

    }

    @Test
    public void testInterfaceImpl() throws IllegalAccessException, InstantiationException {
        String className = "Test";
        JiteClass jiteClass = new JiteClass(className, new String[]{p(Runnable.class)}) {{

            defineDefaultConstructor();

            defineMethod("run", ACC_PUBLIC, sig(void.class),
                    newCodeBlock()
                            .ldc("Test")
                            .aprintln()
                            .voidreturn()
            );
        }};

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Object o = clazz.newInstance();
        assertTrue(o instanceof Runnable);
    }

    public static class LOL {

    }

    @Test
    public void generateClassWithSuperclasses() throws IllegalAccessException, InstantiationException {
        String className = "Teste";
        String superClass = p(LOL.class);
        JiteClass jiteClass = new JiteClass(className, superClass, new String[]{}) {{
            defineDefaultConstructor();
        }};
        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Object o = clazz.newInstance();
        assertTrue(o instanceof LOL);
    }

    @Test
    public void testFields() throws Exception {
        JiteClass jiteClass = new JiteClass("testFields", p(Object.class), new String[0]) {{
            defineField("foo", ACC_PUBLIC | ACC_STATIC, ci(String.class), "bar");
        }};

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Field foo = clazz.getDeclaredField("foo");
        
        assertEquals("foo field was not a string", String.class, foo.getType());
        assertEquals("foo field was not set to 'bar'", "bar", foo.get(null));
    }
}
