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

package me.qmx.jitescript;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.*;

/**
 * @author qmx
 */
public class CodeBlock implements Opcodes {

    private boolean DEBUG = false;
    private InsnList instructionList = new InsnList();
    private List<TryCatchBlockNode> tryCatchBlockList = new ArrayList<TryCatchBlockNode>();
    private List<LocalVariableNode> localVariableList = new ArrayList<LocalVariableNode>();

    /**
     * Short-hand for specifying a set of aloads
     *
     * @param args list of aloads you want
     */
    public void aloadMany(int... args) {
        for (int arg : args) {
            aload(arg);
        }
    }

    public void aload(int arg0) {
        this.instructionList.add(new VarInsnNode(ALOAD, arg0));
    }

    public void iload(int arg0) {
        this.instructionList.add(new VarInsnNode(ILOAD, arg0));
    }

    public void lload(int arg0) {
        this.instructionList.add(new VarInsnNode(LLOAD, arg0));
    }

    public void fload(int arg0) {
        this.instructionList.add(new VarInsnNode(FLOAD, arg0));
    }

    public void dload(int arg0) {
        this.instructionList.add(new VarInsnNode(DLOAD, arg0));
    }

    public void astore(int arg0) {
        this.instructionList.add(new VarInsnNode(ASTORE, arg0));
    }

    public void istore(int arg0) {
        this.instructionList.add(new VarInsnNode(ISTORE, arg0));
    }

    public void lstore(int arg0) {
        this.instructionList.add(new VarInsnNode(LSTORE, arg0));
    }

    public void fstore(int arg0) {
        this.instructionList.add(new VarInsnNode(FSTORE, arg0));
    }

    public void dstore(int arg0) {
        this.instructionList.add(new VarInsnNode(DSTORE, arg0));
    }

    public void ldc(Object arg0) {
        this.instructionList.add(new LdcInsnNode(arg0));
    }

    public void bipush(int arg) {
        this.instructionList.add(new IntInsnNode(BIPUSH, arg));
    }

    public void sipush(int arg) {
        this.instructionList.add(new IntInsnNode(SIPUSH, arg));
    }

    public void pushInt(int value) {
        if (value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE) {
            switch (value) {
                case -1:
                    iconst_m1();
                    break;
                case 0:
                    iconst_0();
                    break;
                case 1:
                    iconst_1();
                    break;
                case 2:
                    iconst_2();
                    break;
                case 3:
                    iconst_3();
                    break;
                case 4:
                    iconst_4();
                    break;
                case 5:
                    iconst_5();
                    break;
                default:
                    bipush(value);
                    break;
            }
        } else if (value <= Short.MAX_VALUE && value >= Short.MIN_VALUE) {
            sipush(value);
        } else {
            ldc(value);
        }
    }

    public void pushBoolean(boolean bool) {
        if (bool) {
            iconst_1();
        } else {
            iconst_0();
        }
    }

    public void invokestatic(String arg1, String arg2, String arg3) {
        this.instructionList.add(new MethodInsnNode(INVOKESTATIC, arg1, arg2, arg3));
    }

    public void invokespecial(String arg1, String arg2, String arg3) {
        this.instructionList.add(new MethodInsnNode(INVOKESPECIAL, arg1, arg2, arg3));
    }

    public void invokevirtual(String arg1, String arg2, String arg3) {
        this.instructionList.add(new MethodInsnNode(INVOKEVIRTUAL, arg1, arg2, arg3));
    }

    public void invokeinterface(String arg1, String arg2, String arg3) {
        this.instructionList.add(new MethodInsnNode(INVOKEINTERFACE, arg1, arg2, arg3));
    }

    public void invokedynamic(String arg0, String arg1, MethodHandle arg2, Object... arg3) {
        this.instructionList.add(new InvokeDynamicInsnNode(arg0, arg1, arg2, arg3));
    }

    public void aprintln() {
        dup();
        getstatic(p(System.class), "out", ci(PrintStream.class));
        swap();
        invokevirtual(p(PrintStream.class), "println", sig(void.class, params(Object.class)));
    }

    public void iprintln() {
        dup();
        getstatic(p(System.class), "out", ci(PrintStream.class));
        swap();
        invokevirtual(p(PrintStream.class), "println", sig(void.class, params(int.class)));
    }

    public void areturn() {
        this.instructionList.add(new InsnNode(ARETURN));
    }

    public void ireturn() {
        this.instructionList.add(new InsnNode(IRETURN));
    }

    public void freturn() {
        this.instructionList.add(new InsnNode(FRETURN));
    }

    public void lreturn() {
        this.instructionList.add(new InsnNode(LRETURN));
    }

    public void dreturn() {
        this.instructionList.add(new InsnNode(DRETURN));
    }

    public void newobj(String arg0) {
        this.instructionList.add(new TypeInsnNode(NEW, arg0));
    }

    public void dup() {
        this.instructionList.add(new InsnNode(DUP));
    }

    public void swap() {
        this.instructionList.add(new InsnNode(SWAP));
    }

    public void swap2() {
        dup2_x2();
        pop2();
    }

    public void getstatic(String arg1, String arg2, String arg3) {
        this.instructionList.add(new FieldInsnNode(GETSTATIC, arg1, arg2, arg3));
    }

    public void putstatic(String arg1, String arg2, String arg3) {
        this.instructionList.add(new FieldInsnNode(PUTSTATIC, arg1, arg2, arg3));
    }

    public void getfield(String arg1, String arg2, String arg3) {
        this.instructionList.add(new FieldInsnNode(GETFIELD, arg1, arg2, arg3));
    }

    public void putfield(String arg1, String arg2, String arg3) {
        this.instructionList.add(new FieldInsnNode(PUTFIELD, arg1, arg2, arg3));
    }

    public void voidreturn() {
        this.instructionList.add(new InsnNode(RETURN));
    }

    public void anewarray(String arg0) {
        this.instructionList.add(new TypeInsnNode(ANEWARRAY, arg0));
    }

    public void multianewarray(String arg0, int dims) {
        this.instructionList.add(new MultiANewArrayInsnNode(arg0, dims));
    }

    public void newarray(int arg0) {
        this.instructionList.add(new IntInsnNode(NEWARRAY, arg0));
    }

    public void iconst_m1() {
        this.instructionList.add(new InsnNode(ICONST_M1));
    }

    public void iconst_0() {
        this.instructionList.add(new InsnNode(ICONST_0));
    }

    public void iconst_1() {
        this.instructionList.add(new InsnNode(ICONST_1));
    }

    public void iconst_2() {
        this.instructionList.add(new InsnNode(ICONST_2));
    }

    public void iconst_3() {
        this.instructionList.add(new InsnNode(ICONST_3));
    }

    public void iconst_4() {
        this.instructionList.add(new InsnNode(ICONST_4));
    }

    public void iconst_5() {
        this.instructionList.add(new InsnNode(ICONST_5));
    }

    public void lconst_0() {
        this.instructionList.add(new InsnNode(LCONST_0));
    }

    public void aconst_null() {
        this.instructionList.add(new InsnNode(ACONST_NULL));
    }

    public void label(LabelNode labelNode) {
        this.instructionList.add(labelNode);
    }

    public void nop() {
        this.instructionList.add(new InsnNode(NOP));
    }

    public void pop() {
        this.instructionList.add(new InsnNode(POP));
    }

    public void pop2() {
        this.instructionList.add(new InsnNode(POP2));
    }

    public void arrayload() {
        this.instructionList.add(new InsnNode(AALOAD));
    }

    public void arraystore() {
        this.instructionList.add(new InsnNode(AASTORE));
    }

    public void iarrayload() {
        this.instructionList.add(new InsnNode(IALOAD));
    }

    public void barrayload() {
        this.instructionList.add(new InsnNode(BALOAD));
    }

    public void barraystore() {
        this.instructionList.add(new InsnNode(BASTORE));
    }

    public void aaload() {
        this.instructionList.add(new InsnNode(AALOAD));
    }

    public void aastore() {
        this.instructionList.add(new InsnNode(AASTORE));
    }

    public void iaload() {
        this.instructionList.add(new InsnNode(IALOAD));
    }

    public void iastore() {
        this.instructionList.add(new InsnNode(IASTORE));
    }

    public void laload() {
        this.instructionList.add(new InsnNode(LALOAD));
    }

    public void lastore() {
        this.instructionList.add(new InsnNode(LASTORE));
    }

    public void baload() {
        this.instructionList.add(new InsnNode(BALOAD));
    }

    public void bastore() {
        this.instructionList.add(new InsnNode(BASTORE));
    }

    public void saload() {
        this.instructionList.add(new InsnNode(SALOAD));
    }

    public void sastore() {
        this.instructionList.add(new InsnNode(SASTORE));
    }

    public void caload() {
        this.instructionList.add(new InsnNode(CALOAD));
    }

    public void castore() {
        this.instructionList.add(new InsnNode(CASTORE));
    }

    public void faload() {
        this.instructionList.add(new InsnNode(FALOAD));
    }

    public void fastore() {
        this.instructionList.add(new InsnNode(FASTORE));
    }

    public void daload() {
        this.instructionList.add(new InsnNode(DALOAD));
    }

    public void dastore() {
        this.instructionList.add(new InsnNode(DASTORE));
    }

    public void fcmpl() {
        this.instructionList.add(new InsnNode(FCMPL));
    }

    public void fcmpg() {
        this.instructionList.add(new InsnNode(FCMPG));
    }

    public void dcmpl() {
        this.instructionList.add(new InsnNode(DCMPL));
    }

    public void dcmpg() {
        this.instructionList.add(new InsnNode(DCMPG));
    }

    public void dup_x2() {
        this.instructionList.add(new InsnNode(DUP_X2));
    }

    public void dup_x1() {
        this.instructionList.add(new InsnNode(DUP_X1));
    }

    public void dup2_x2() {
        this.instructionList.add(new InsnNode(DUP2_X2));
    }

    public void dup2_x1() {
        this.instructionList.add(new InsnNode(DUP2_X1));
    }

    public void dup2() {
        this.instructionList.add(new InsnNode(DUP2));
    }

    public void trycatch(LabelNode arg0, LabelNode arg1, LabelNode arg2,
                         String arg3) {
        this.tryCatchBlockList.add(new TryCatchBlockNode(arg0, arg1, arg2, arg3));
    }

    public void trycatch(String type, Runnable body, Runnable catchBody) {
        LabelNode before = new LabelNode();
        LabelNode after = new LabelNode();
        LabelNode catchStart = new LabelNode();
        LabelNode done = new LabelNode();

        trycatch(before, after, catchStart, type);
        label(before);
        body.run();
        label(after);
        go_to(done);
        if (catchBody != null) {
            label(catchStart);
            catchBody.run();
        }
        label(done);
    }

    public void go_to(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(GOTO, arg0));
    }

    public void lookupswitch(LabelNode arg0, int[] arg1, LabelNode[] arg2) {
        this.instructionList.add(new LookupSwitchInsnNode(arg0, arg1, arg2));
    }

    public void athrow() {
        this.instructionList.add(new InsnNode(ATHROW));
    }

    public void instance_of(String arg0) {
        this.instructionList.add(new TypeInsnNode(INSTANCEOF, arg0));
    }

    public void ifeq(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFEQ, arg0));
    }

    public void iffalse(LabelNode arg0) {
        ifeq(arg0);
    }

    public void ifne(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFNE, arg0));
    }

    public void iftrue(LabelNode arg0) {
        ifne(arg0);
    }

    public void if_acmpne(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ACMPNE, arg0));
    }

    public void if_acmpeq(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ACMPEQ, arg0));
    }

    public void if_icmple(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ICMPLE, arg0));
    }

    public void if_icmpgt(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ICMPGT, arg0));
    }

    public void if_icmplt(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ICMPLT, arg0));
    }

    public void if_icmpne(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ICMPNE, arg0));
    }

    public void if_icmpeq(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IF_ICMPEQ, arg0));
    }

    public void checkcast(String arg0) {
        this.instructionList.add(new TypeInsnNode(CHECKCAST, arg0));
    }

    public void line(int line) {
        visitLineNumber(line, new LabelNode());
    }

    public void line(int line, LabelNode label) {
        visitLineNumber(line, label);
    }

    public void ifnonnull(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFNONNULL, arg0));
    }

    public void ifnull(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFNULL, arg0));
    }

    public void iflt(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFLT, arg0));
    }

    public void ifle(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFLE, arg0));
    }

    public void ifgt(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFGT, arg0));
    }

    public void ifge(LabelNode arg0) {
        this.instructionList.add(new JumpInsnNode(IFGE, arg0));
    }

    public void arraylength() {
        this.instructionList.add(new InsnNode(ARRAYLENGTH));
    }

    public void ishr() {
        this.instructionList.add(new InsnNode(ISHR));
    }

    public void ishl() {
        this.instructionList.add(new InsnNode(ISHL));
    }

    public void iushr() {
        this.instructionList.add(new InsnNode(IUSHR));
    }

    public void lshr() {
        this.instructionList.add(new InsnNode(LSHR));
    }

    public void lshl() {
        this.instructionList.add(new InsnNode(LSHL));
    }

    public void lushr() {
        this.instructionList.add(new InsnNode(LUSHR));
    }

    public void lcmp() {
        this.instructionList.add(new InsnNode(LCMP));
    }

    public void iand() {
        this.instructionList.add(new InsnNode(IAND));
    }

    public void ior() {
        this.instructionList.add(new InsnNode(IOR));
    }

    public void ixor() {
        this.instructionList.add(new InsnNode(IXOR));
    }

    public void land() {
        this.instructionList.add(new InsnNode(LAND));
    }

    public void lor() {
        this.instructionList.add(new InsnNode(LOR));
    }

    public void lxor() {
        this.instructionList.add(new InsnNode(LXOR));
    }

    public void iadd() {
        this.instructionList.add(new InsnNode(IADD));
    }

    public void ladd() {
        this.instructionList.add(new InsnNode(LADD));
    }

    public void fadd() {
        this.instructionList.add(new InsnNode(FADD));
    }

    public void dadd() {
        this.instructionList.add(new InsnNode(DADD));
    }

    public void isub() {
        this.instructionList.add(new InsnNode(ISUB));
    }

    public void lsub() {
        this.instructionList.add(new InsnNode(LSUB));
    }

    public void fsub() {
        this.instructionList.add(new InsnNode(FSUB));
    }

    public void dsub() {
        this.instructionList.add(new InsnNode(DSUB));
    }

    public void idiv() {
        this.instructionList.add(new InsnNode(IDIV));
    }

    public void irem() {
        this.instructionList.add(new InsnNode(IREM));
    }

    public void ineg() {
        this.instructionList.add(new InsnNode(INEG));
    }

    public void i2d() {
        this.instructionList.add(new InsnNode(I2D));
    }

    public void i2l() {
        this.instructionList.add(new InsnNode(I2L));
    }

    public void i2f() {
        this.instructionList.add(new InsnNode(I2F));
    }

    public void i2s() {
        this.instructionList.add(new InsnNode(I2S));
    }

    public void i2c() {
        this.instructionList.add(new InsnNode(I2C));
    }

    public void i2b() {
        this.instructionList.add(new InsnNode(I2B));
    }

    public void ldiv() {
        this.instructionList.add(new InsnNode(LDIV));
    }

    public void lrem() {
        this.instructionList.add(new InsnNode(LREM));
    }

    public void lneg() {
        this.instructionList.add(new InsnNode(LNEG));
    }

    public void l2d() {
        this.instructionList.add(new InsnNode(L2D));
    }

    public void l2i() {
        this.instructionList.add(new InsnNode(L2I));
    }

    public void l2f() {
        this.instructionList.add(new InsnNode(L2F));
    }

    public void fdiv() {
        this.instructionList.add(new InsnNode(FDIV));
    }

    public void frem() {
        this.instructionList.add(new InsnNode(FREM));
    }

    public void fneg() {
        this.instructionList.add(new InsnNode(FNEG));
    }

    public void f2d() {
        this.instructionList.add(new InsnNode(F2D));
    }

    public void f2i() {
        this.instructionList.add(new InsnNode(F2D));
    }

    public void f2l() {
        this.instructionList.add(new InsnNode(F2L));
    }

    public void ddiv() {
        this.instructionList.add(new InsnNode(DDIV));
    }

    public void drem() {
        this.instructionList.add(new InsnNode(DREM));
    }

    public void dneg() {
        this.instructionList.add(new InsnNode(DNEG));
    }

    public void d2f() {
        this.instructionList.add(new InsnNode(D2F));
    }

    public void d2i() {
        this.instructionList.add(new InsnNode(D2I));
    }

    public void d2l() {
        this.instructionList.add(new InsnNode(D2L));
    }

    public void imul() {
        this.instructionList.add(new InsnNode(IMUL));
    }

    public void lmul() {
        this.instructionList.add(new InsnNode(LMUL));
    }

    public void fmul() {
        this.instructionList.add(new InsnNode(FMUL));
    }

    public void dmul() {
        this.instructionList.add(new InsnNode(DMUL));
    }

    public void iinc(int arg0, int arg1) {
        this.instructionList.add(new IincInsnNode(arg0, arg1));
    }

    public void monitorenter() {
        this.instructionList.add(new InsnNode(MONITORENTER));
    }

    public void monitorexit() {
        this.instructionList.add(new InsnNode(MONITOREXIT));
    }

    public void jsr(LabelNode branch) {
        this.instructionList.add(new JumpInsnNode(JSR, branch));
    }

    public void ret(int arg0) {
        this.instructionList.add(new IntInsnNode(RET, arg0));
    }

    public void visitInsn(int arg0) {
        this.instructionList.add(new InsnNode(arg0));
    }

    public void visitIntInsn(int arg0, int arg1) {
        this.instructionList.add(new IntInsnNode(arg0, arg1));
    }

    public void visitInsnNode(int arg0, int arg1) {
        this.instructionList.add(new IntInsnNode(arg0, arg1));
    }

    public void visitTypeInsn(int arg0, String arg1) {
        this.instructionList.add(new TypeInsnNode(arg0, arg1));
    }

    public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
        this.instructionList.add(new FieldInsnNode(arg0, arg1, arg2, arg3));
    }

    public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3) {
        this.instructionList.add(new MethodInsnNode(arg0, arg1, arg2, arg3));
    }

    public void visitInvokeDynamicInsn(String arg0, String arg1, MethodHandle arg2, Object... arg3) {
        this.instructionList.add(new InvokeDynamicInsnNode(arg0, arg1, arg2, arg3));
    }

    public void visitJumpInsn(int arg0, LabelNode arg1) {
        this.instructionList.add(new JumpInsnNode(arg0, arg1));
    }

    public void visitLabel(Label arg0) {
        this.instructionList.add(new LabelNode(arg0));
    }

    public void visitLdcInsn(Object arg0) {
        this.instructionList.add(new LdcInsnNode(arg0));
    }

    public void visitIincInsn(int arg0, int arg1) {
        this.instructionList.add(new IincInsnNode(arg0, arg1));
    }

    public void visitTableSwitchInsn(int arg0, int arg1, LabelNode arg2,
                                     LabelNode[] arg3) {
        this.instructionList.add(new TableSwitchInsnNode(arg0, arg1, arg2, arg3));
    }

    public void visitLookupSwitchInsn(LabelNode arg0, int[] arg1, LabelNode[] arg2) {
        this.instructionList.add(new LookupSwitchInsnNode(arg0, arg1, arg2));
    }

    public void visitMultiANewArrayInsn(String arg0, int arg1) {
        this.instructionList.add(new MultiANewArrayInsnNode(arg0, arg1));
    }

    public void visitTryCatchBlock(LabelNode arg0, LabelNode arg1, LabelNode arg2,
                                   String arg3) {
        this.tryCatchBlockList.add(new TryCatchBlockNode(arg0, arg1, arg2, arg3));
    }


    public void visitLocalVariable(String arg0, String arg1, String arg2,
                                   LabelNode arg3, LabelNode arg4, int arg5) {
        this.localVariableList.add(new LocalVariableNode(arg0, arg1, arg2, arg3, arg4, arg5));
    }

    public void visitLineNumber(int arg0, LabelNode arg1) {
        this.instructionList.add(new LineNumberNode(arg0, arg1));
    }

    public void tableswitch(int min, int max, LabelNode defaultLabel, LabelNode[] cases) {
        this.instructionList.add(new TableSwitchInsnNode(min, max, defaultLabel, cases));
    }

    public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {
        this.instructionList.add(new FrameNode(arg0, arg1, arg2, arg3, arg4));
    }


    public InsnList getInstructionList() {
        return instructionList;
    }

    public List<TryCatchBlockNode> getTryCatchBlockList() {
        return tryCatchBlockList;
    }

    public List<LocalVariableNode> getLocalVariableList() {
        return localVariableList;
    }
}
