package me.qmx.jitescript;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CodeBlockTest {

    @Test
    public void testLocalVarIndexMgmt() {
        CodeBlock codeBlock = CodeBlock.newCodeBlock(2)
                .ldc("arg1")
                .pushLocalVar("arg1")
                .ldc("arg2")
                .pushLocalVar("arg2");

        assertThat(codeBlock.arity()).isEqualTo(2);
        assertThat(codeBlock.getLocalVariables()).hasSize(2);
    }

}
