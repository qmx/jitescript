package me.qmx.jitescript;

import static java.util.Arrays.asList;
import static me.qmx.jitescript.util.CodegenUtils.ci;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static org.objectweb.asm.Opcodes.H_INVOKEVIRTUAL;
import static org.objectweb.asm.Type.getMethodType;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Handle;

public class LambdaBlock {

    public static final Handle METAFACTORY = new Handle(
        H_INVOKESTATIC,
        "java/lang/invoke/LambdaMetafactory",
        "metafactory",
        "(Ljava/lang/invoke/MethodHandles$Lookup;"
            + "Ljava/lang/String;"
            + "Ljava/lang/invoke/MethodType;"
            + "Ljava/lang/invoke/MethodType;"
            + "Ljava/lang/invoke/MethodHandle;"
            + "Ljava/lang/invoke/MethodType;"
            + ")Ljava/lang/invoke/CallSite;"
    );

    /** The argument types to capture off the stack. */
    private List<String> captureArguments;
    /** Access level of the implementation method. */
    private int implementationAccess;
    /** Signature of the implementation method. */
    private String implementationSignature;
    /** Code for the implementation method. */
    private CodeBlock implementationCode;
    /** The functional interface type. */
    private String interfaceType;
    /** The functional interface method name. */
    private String interfaceMethod;
    /** The functional interface signature. */
    private String interfaceSignature;
    /** The specialized functional interface method signature. */
    private String specializedSignature;

    /**
     * Applies this lambda block to the given class and code block, inserting the associated invokedynamic instruction.
     *
     * @param jiteClass The JiteClass to define the lambda within.
     * @param block The code block referencing the lambda.
     */
    public void apply(JiteClass jiteClass, CodeBlock block) {
        int handleType = ((implementationAccess & ACC_STATIC) == ACC_STATIC) ? H_INVOKESTATIC : H_INVOKEVIRTUAL;
        String lambdaName = jiteClass.reserveLambda();
        jiteClass.defineMethod(lambdaName, implementationAccess, implementationSignature, implementationCode);
        block.invokedynamic(interfaceMethod, getCallSiteSignature(), METAFACTORY,
            getMethodType(interfaceSignature),
            new Handle(handleType, jiteClass.getClassName(), lambdaName, implementationSignature),
            getMethodType(specializedSignature == null ? interfaceSignature : specializedSignature)
        );
    }

    /**
     * Specifies the argument types to capture off the stack.
     *
     * @param captureArguments The argument types.
     * @return The lambda block.
     */
    public LambdaBlock capture(Class<?>... captureArguments) {
        this.captureArguments = new ArrayList<String>();
        for (Class<?> argType : captureArguments) {
            this.captureArguments.add(ci(argType));
        }
        return this;
    }

    /**
     * Specifies the argument types to capture off the stack.
     *
     * @param captureArguments The array of argument types.
     * @return The lambda block.
     */
    public LambdaBlock capture(String... captureArguments) {
        this.captureArguments = new ArrayList<String>(asList(captureArguments));
        return this;
    }

    /**
     * Defines the method to delegate the lambda to.
     *
     * @param implementationAccess The access level of the method.
     * @param implementationSignature The signature of the method.
     * @param implementationCode The code block for the method.
     * @return The lambda block.
     */
    public LambdaBlock delegateTo(int implementationAccess, String implementationSignature, CodeBlock implementationCode) {
        this.implementationSignature = implementationSignature;
        this.implementationAccess = implementationAccess | ACC_SYNTHETIC;
        this.implementationCode = implementationCode;
        return this;
    }

    /**
     * Declares the functional interface type, method name, and method signature for the lambda.
     *
     * @param interfaceType The interface type.
     * @param interfaceMethod The method to implement.
     * @param interfaceSignature The signature of the method.
     * @return The lambda block.
     */
    public LambdaBlock function(String interfaceType, String interfaceMethod, String interfaceSignature) {
        this.interfaceType = interfaceType;
        this.interfaceMethod = interfaceMethod;
        this.interfaceSignature = interfaceSignature;
        return this;
    }

    /**
     * Specializes the interface method type.
     *
     * @param specializedSignature The specialized method signature.
     * @return The lambda block.
     */
    public LambdaBlock specialize(String specializedSignature) {
        this.specializedSignature = specializedSignature;
        return this;
    }

    /**
     * Generates the call site signature.
     *
     * @return signature
     */
    private String getCallSiteSignature() {
        StringBuilder builder = new StringBuilder();
        for (String arg : captureArguments) {
            builder.append(arg);
        }
        return "(" + builder.toString() + ")L" + interfaceType + ";";
    }
}
