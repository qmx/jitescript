package me.qmx.jitescript;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.FieldNode;

public class FieldDefinition {

    private final String fieldName;
    private final int modifiers;
    private final String signature;
    private final Object value;
    private final List<VisibleAnnotation> annotations;

    public FieldDefinition(String fieldName, int modifiers, String signature, Object value) {
        this.fieldName = fieldName;
        this.modifiers = modifiers;
        this.signature = signature;
        this.value = value;
        this.annotations = new ArrayList<VisibleAnnotation>();
    }

    public FieldNode getFieldNode() {
        FieldNode node = new FieldNode(modifiers, fieldName, signature, null, value);
        node.visibleAnnotations = new ArrayList<VisibleAnnotation>();
        for (VisibleAnnotation annotation : annotations) {
            node.visibleAnnotations.add(annotation.getNode());
        }
        return node;
    }

    public FieldDefinition addAnnotation(VisibleAnnotation annotation) {
        annotations.add(annotation);
        return this;
    }
}
