package me.qmx.jitescript;

import org.objectweb.asm.AnnotationVisitor;

public class AnnotationArrayValue {

    private final String name;
    private final AnnotationVisitor node;

    public AnnotationArrayValue(String name, AnnotationVisitor node) {
        this.name = name;
        this.node = node;
    }

    public void add(Object value) {
        node.visit(name, value);
    }
}
