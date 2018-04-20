package com.stdakov.tools;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class HashTest {
    @Test
    public void testEncryption() {
        String value = "This is a test";
        Optional sha1 = Hash.sha1(value);
        Optional md5 = Hash.md5(value);

        Assert.assertTrue(sha1.isPresent());
        Assert.assertNotEquals(sha1.get().toString(), value);

        Assert.assertTrue(md5.isPresent());
        Assert.assertNotEquals(md5.get().toString(), value);

        Assert.assertNotEquals(sha1.get().toString(), md5.get().toString());
    }
}
