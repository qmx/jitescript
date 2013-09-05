package me.qmx.jitescript;

import static me.qmx.jitescript.util.CodegenUtils.ci;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.tree.AnnotationNode;

public class VisibleAnnotation {

    private final AnnotationVisitor node;

    public VisibleAnnotation(Class<?> type) {
        this(ci(type));
    }

    public VisibleAnnotation(String desc) {
        this.node = new AnnotationNode(desc);
    }

    private VisibleAnnotation(AnnotationVisitor node) {
        this.node = node;
    }

    public AnnotationNode getNode() {
        return (AnnotationNode) node;
    }

    public VisibleAnnotation value(String name, Object value) {
        node.visit(name, value);
        return this;
    }

    public VisibleAnnotation enumValue(String name, Enum value) {
        enumValue(name, ci(value.getClass()), value.name());
        return this;
    }

    public VisibleAnnotation enumValue(String name, String desc, String value) {
        node.visitEnum(name, desc, value);
        return this;
    }

    public VisibleAnnotation annotationValue(String name, Class<?> type) {
        return annotationValue(name, ci(type));
    }

    public VisibleAnnotation annotationValue(String name, String desc) {
        return new VisibleAnnotation(node.visitAnnotation(name, desc));
    }

    public AnnotationArrayValue arrayValue(String name) {
        return new AnnotationArrayValue(name, node.visitArray(name));
    }

    public AnnotationArrayValue arrayValue(String name, Object... values) {
        AnnotationArrayValue array = arrayValue(name);
        for (Object value : values) {
            array.add(value);
        }
        return array;
    }
}
