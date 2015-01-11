package me.qmx.jitescript;

import static me.qmx.jitescript.util.CodegenUtils.ci;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.tree.AnnotationNode;

public class AnnotationData {

    private final AnnotationVisitor node;

    public AnnotationData(Class<?> type) {
        this(ci(type));
    }

    public AnnotationData(String desc) {
        this.node = new AnnotationNode(desc);
    }

    private AnnotationData(AnnotationVisitor node) {
        this.node = node;
    }

    public AnnotationNode getNode() {
        return (AnnotationNode) node;
    }

    public AnnotationData value(String name, Object value) {
        node.visit(name, value);
        return this;
    }

    public AnnotationData enumValue(String name, Enum value) {
        enumValue(name, ci(value.getClass()), value.name());
        return this;
    }

    public AnnotationData enumValue(String name, String desc, String value) {
        node.visitEnum(name, desc, value);
        return this;
    }

    public AnnotationData annotationValue(String name, Class<?> type) {
        return annotationValue(name, ci(type));
    }

    public AnnotationData annotationValue(String name, String desc) {
        return new AnnotationData(node.visitAnnotation(name, desc));
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
