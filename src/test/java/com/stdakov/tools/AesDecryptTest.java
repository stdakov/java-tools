package com.stdakov.tools;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AesDecryptTest {
    @Test
    public void testEncryption() {
        String value = "This is a test";
        AesDecrypt aesDecrypt = new AesDecrypt();
        Optional crypt = aesDecrypt.encrypt(value);

        Assert.assertTrue(crypt.isPresent());
        Assert.assertNotEquals(crypt.get().toString(), value);

        Optional decrypt = aesDecrypt.decrypt(crypt.get().toString());

        Assert.assertTrue(decrypt.isPresent());
        Assert.assertNotEquals(decrypt.get().toString(), crypt.get().toString());
        Assert.assertEquals(decrypt.get().toString(), value);

        AesDecrypt aesDecrypt1 = new AesDecrypt();
        aesDecrypt1.setKey("1aXf0@qX!J6lpAvA");

        Optional crypt1 = aesDecrypt1.encrypt(value);
        Assert.assertTrue(crypt1.isPresent());
        Assert.assertNotEquals(crypt1.get().toString(), value);
        Assert.assertNotEquals(crypt1.get().toString(), crypt.get().toString());

    }
}
