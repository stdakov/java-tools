package com.stdakov.tools;

import org.junit.Assert;
import org.junit.Test;

public class ExecuteShellCommandTest {
    @Test
    public void testExecuteCommand() {
        String output = ExecuteShellCommand.execute("ls -l");

        Assert.assertNotNull(output);
        Assert.assertFalse(output.isEmpty());
    }
}
