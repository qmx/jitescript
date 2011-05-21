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
