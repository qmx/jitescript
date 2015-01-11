package me.qmx.jitescript;

import static me.qmx.jitescript.util.CodegenUtils.ci;

import org.objectweb.asm.AnnotationVisitor;

public class AnnotationArrayValue {

    private final String name;
    private final AnnotationVisitor node;

    public AnnotationArrayValue(String name, AnnotationVisitor node) {
        this.name = name;
        this.node = node;
    }

    public AnnotationArrayValue add(Object value) {
        if (value instanceof AnnotationData) {
            add(((AnnotationData) value).getNode());
        } else {
            node.visit(name, value);
        }
        return this;
    }

    public AnnotationArrayValue addEnum(Enum<?> value) {
        addEnum(ci(value.getDeclaringClass()), value.name());
        return this;
    }

    public AnnotationArrayValue addEnum(String desc, String value) {
        node.visitEnum(null, desc, value);
        return this;
    }
}
