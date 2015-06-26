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

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.c;
import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author qmx
 */
public class JiteClassTest {

    public static class DynamicClassLoader extends ClassLoader {
        public Class<?> define(JiteClass jiteClass) {
            byte[] classBytes = jiteClass.toBytes();
            return super.defineClass(c(jiteClass.getClassName()), classBytes, 0, classBytes.length);
        }
    }

    @Test
    public void testDSL() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final String className = "helloTest";
        JiteClass jiteClass = new JiteClass(className) {
            {
                // you can use the pre-constructor style
                defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(void.class, String[].class), new CodeBlock() {
                    {
                        ldc("helloWorld");
                        getstatic(p(System.class), "out", ci(PrintStream.class));
                        swap();
                        invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class));
                        voidreturn();
                    }
                });
                // or use chained api
                defineMethod("hello", ACC_PUBLIC | ACC_STATIC, sig(String.class), newCodeBlock().ldc("helloWorld").areturn());

            }
        };

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
        JiteClass jiteClass = new JiteClass(className, new String[]{p(Runnable.class)}) {
            {

                defineDefaultConstructor();

                defineMethod("run", ACC_PUBLIC, sig(void.class), newCodeBlock().ldc("Test").aprintln().voidreturn());
            }
        };

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
        JiteClass jiteClass = new JiteClass(className, superClass, new String[]{}) {
            {
                defineDefaultConstructor();
            }
        };
        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Object o = clazz.newInstance();
        assertTrue(o instanceof LOL);
    }

    public static class NondefaultConstructor {
        public String foo = "hello";
    }

    @Test
    public void superclassHashNondefaultConstructor() throws IllegalAccessException, InstantiationException {
        String className = "Sub";
        String superClass = p(NondefaultConstructor.class);
        JiteClass jiteClass = new JiteClass(className, superClass, new String[]{}) {
            {
                defineDefaultConstructor();
            }
        };
        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        NondefaultConstructor o = (NondefaultConstructor) clazz.newInstance();

        assertEquals("hello", o.foo);
    }

    @Test
    public void testFields() throws Exception {
        JiteClass jiteClass = new JiteClass("testFields", p(Object.class), new String[0]) {
            {
                defineField("foo", ACC_PUBLIC | ACC_STATIC, ci(String.class), "bar");
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Field foo = clazz.getDeclaredField("foo");

        assertEquals("foo field was not a string", String.class, foo.getType());
        assertEquals("foo field was not set to 'bar'", "bar", foo.get(null));
    }

    @Test(expected = IllegalAccessException.class)
    public void testPrivateClass() throws Exception {
        JiteClass jiteClass = new JiteClass("Test", new String[0]) {
            {
                setAccess(ACC_PRIVATE);
                defineDefaultConstructor();
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);

        clazz.newInstance();
    }

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws Exception {
        JiteClass jiteClass = new JiteClass("Test", new String[0]) {
            {
                defineDefaultConstructor(ACC_PRIVATE);
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);

        clazz.newInstance();
    }

    @Test
    public void testPrivateInnerClass() throws Exception {
        JiteClass parent = new JiteClass("test/Parent") {
            {
                setAccess(ACC_PUBLIC);
                defineDefaultConstructor();
                addChildClass(new JiteClass(getClassName() + "$Child") {
                    {
                        setAccess(ACC_PRIVATE);
                        defineDefaultConstructor();
                    }
                });
            }
        };

        DynamicClassLoader classLoader = new DynamicClassLoader();
        Class<?> parentClazz = classLoader.define(parent);

        for (JiteClass child : parent.getChildClasses()) {
            Class<?> childClazz = classLoader.define(child);
            assertFalse(childClazz.getConstructor(new Class[0]).isAccessible());
        }
    }
}
