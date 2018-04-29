package com.stdakov.tools;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class PathTest {
    @Test
    public void test() {
        String rootPath = Path.getRoot();
        Assert.assertTrue(rootPath != null);
        Assert.assertTrue(!rootPath.isEmpty());
        Assert.assertTrue(rootPath.endsWith("/java-tools/target"));
    }
}
