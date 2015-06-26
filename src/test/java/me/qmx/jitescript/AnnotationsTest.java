package me.qmx.jitescript;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import me.qmx.jitescript.JiteClassTest.DynamicClassLoader;
import org.junit.Test;

public class AnnotationsTest {

    @Test
    public void testAnnotationWithScalarValue() throws Exception {
        JiteClass jiteClass = new JiteClass("AnnotatedClass", p(Object.class), new String[0]) {
            {
                defineDefaultConstructor();
                VisibleAnnotation annotation = new VisibleAnnotation(ScalarAnnotation.class);
                annotation.value("breakfastItem", "Waffles!");
                addAnnotation(annotation);
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        ScalarAnnotation annotation = clazz.getAnnotation(ScalarAnnotation.class);

        assertNotNull("ScalarAnnotation was not on class", annotation);
        assertEquals(annotation.breakfastItem(), "Waffles!");
    }

    @Test
    public void testAnnotationWithArrayValue() throws Exception {
        JiteClass jiteClass = new JiteClass("AnnotatedClass", p(Object.class), new String[0]) {
            {
                defineDefaultConstructor();
                VisibleAnnotation annotation = new VisibleAnnotation(AnnotationWithArray.class);
                annotation.arrayValue("favoriteColors", "pink", "purple", "green");
                addAnnotation(annotation);
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        AnnotationWithArray annotation = clazz.getAnnotation(AnnotationWithArray.class);

        assertNotNull("AnnotationWithArray was not on class", annotation);
        assertTrue(Arrays.equals(annotation.favoriteColors(), new String[]{"pink", "purple", "green"}));
    }

    @Test
    public void testAnnotationWithAnnotationValue() throws Exception {
        JiteClass jiteClass = new JiteClass("AnnotatedClass", p(Object.class), new String[0]) {
            {
                defineDefaultConstructor();
                VisibleAnnotation annotation = new VisibleAnnotation(AnnotationWithAnnotation.class);
                annotation.annotationValue("element", ScalarAnnotation.class).value("breakfastItem", "Pancakes!");
                addAnnotation(annotation);
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        AnnotationWithAnnotation annotation = clazz.getAnnotation(AnnotationWithAnnotation.class);

        assertNotNull("AnnotationWithAnnotation was not on class", annotation);
        assertEquals(annotation.element().breakfastItem(), "Pancakes!");
    }

    @Test
    public void testAnnotationWithEnumValue() throws Exception {
        JiteClass jiteClass = new JiteClass("AnnotatedClass", p(Object.class), new String[0]) {
            {
                defineDefaultConstructor();
                VisibleAnnotation annotation = new VisibleAnnotation(AnnotationWithEnum.class);
                annotation.enumValue("color", Colors.PINK);
                addAnnotation(annotation);
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        AnnotationWithEnum annotation = clazz.getAnnotation(AnnotationWithEnum.class);

        assertNotNull("AnnotationWithEnum was not on class", annotation);
        assertEquals(annotation.color(), Colors.PINK);
    }

    @Test
    public void testAnnotatedMethod() throws Exception {
        JiteClass jiteClass = new JiteClass("AnnotatedClass", p(Object.class), new String[0]) {
            {
                defineDefaultConstructor();
                defineMethod("annotatedMethod", ACC_PUBLIC, sig(String.class), new CodeBlock() {
                    {
                        ldc("Sausages!");
                        areturn();
                        annotation(ScalarAnnotation.class).value("breakfastItem", "Sausages!");
                    }
                });
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Method method = clazz.getMethod("annotatedMethod");
        ScalarAnnotation annotation = method.getAnnotation(ScalarAnnotation.class);

        assertNotNull("ScalarAnnotation was not on method", annotation);
        assertEquals(annotation.breakfastItem(), "Sausages!");
    }

    @Test
    public void testAnnotatedField() throws Exception {
        JiteClass jiteClass = new JiteClass("AnnotatedClass", p(Object.class), new String[0]) {
            {
                defineDefaultConstructor();
                defineField("annotatedField", ACC_PUBLIC, ci(String.class), null).addAnnotation(
                        new VisibleAnnotation(ScalarAnnotation.class).value("breakfastItem", "Toast!"));
            }
        };

        Class<?> clazz = new DynamicClassLoader().define(jiteClass);
        Field field = clazz.getField("annotatedField");
        ScalarAnnotation annotation = field.getAnnotation(ScalarAnnotation.class);

        assertNotNull("ScalarAnnotation was not on field", annotation);
        assertEquals(annotation.breakfastItem(), "Toast!");
    }

    @Target({TYPE, METHOD, FIELD})
    @Retention(RUNTIME)
    public @interface ScalarAnnotation {

        String breakfastItem();
    }

    @Target({TYPE, METHOD, FIELD})
    @Retention(RUNTIME)
    public @interface AnnotationWithArray {

        String[] favoriteColors();
    }

    @Target({TYPE, METHOD, FIELD})
    @Retention(RUNTIME)
    public @interface AnnotationWithAnnotation {

        ScalarAnnotation element();
    }

    @Target({TYPE, METHOD, FIELD})
    @Retention(RUNTIME)
    public @interface AnnotationWithEnum {

        Colors color();
    }

    public enum Colors {
        PINK,
        GREEN,
        PURPLE
    }
}
