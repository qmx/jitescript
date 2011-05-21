/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.qmx.jitescript;

/**
 *
 * @author qmx
 */
class MethodDefinition {

    private final String methodName;
    private final int modifiers;
    private final String signature;
    private final MethodBody methodBody;

    public MethodDefinition(String methodName, int modifiers, String signature, MethodBody methodBody) {
        this.methodName = methodName;
        this.modifiers = modifiers;
        this.signature = signature;
        this.methodBody = methodBody;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getModifiers() {
        return modifiers;
    }

    public MethodBody getMethodBody() {
        return methodBody;
    }

    public String getSignature() {
        return signature;
    }
}
