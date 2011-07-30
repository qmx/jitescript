package me.qmx.jitescript;

import org.objectweb.asm.Opcodes;

public enum JDKVersion implements Opcodes {
    V1_6(Opcodes.V1_6),
    V1_7(Opcodes.V1_7);

    private final int ver;

    JDKVersion(int ver) {
        this.ver = ver;
    }

    public int getVer() {
        return ver;
    }
}
