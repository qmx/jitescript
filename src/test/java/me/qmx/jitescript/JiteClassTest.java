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

import org.junit.Assert;
import org.junit.Test;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static me.qmx.jitescript.util.CodegenUtils.*;

/**
 * @author qmx
 */
public class JiteClassTest {

    public static class DynamicClassLoader extends ClassLoader {

        public Class<?> define(String className, byte[] bytecode) {
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }
    }

    ;

    @Test
    public void testDSL() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final String className = "helloTest";
        JiteClass jiteClass = new JiteClass(className) {

            {
                defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(void.class, String[].class), new MethodBody() {
                    {
                        ldc("helloWorld");
                        getstatic(p(System.class), "out", ci(PrintStream.class));
                        swap();
                        invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class));
                        voidreturn();
                    }
                });
                defineMethod("hello", ACC_PUBLIC | ACC_STATIC, sig(String.class), new MethodBody() {{
                    ldc("helloWorld");
                    areturn();
                }});
            }
        };

        byte[] classBytes = jiteClass.toBytes();

        DynamicClassLoader loader = new DynamicClassLoader();
        Class<?> clazz = loader.define(className, classBytes);
        Method helloMethod = clazz.getMethod("hello");
        Object result = helloMethod.invoke(null);
        Assert.assertEquals("helloWorld", result);

        Method mainMethod = clazz.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) new String[]{});

    }
}
