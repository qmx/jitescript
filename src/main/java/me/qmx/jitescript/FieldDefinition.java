package me.qmx.jitescript;

import org.objectweb.asm.tree.FieldNode;

public class FieldDefinition {

    private final String fieldName;
    private final int modifiers;
    private final String signature;
    private final Object value;

    public FieldDefinition(String fieldName, int modifiers, String signature, Object value) {
        this.fieldName = fieldName;
        this.modifiers = modifiers;
        this.signature = signature;
        this.value = value;
    }

    public FieldNode getFieldNode() {
        return new FieldNode(modifiers, fieldName, signature, null, value);
    }
}
