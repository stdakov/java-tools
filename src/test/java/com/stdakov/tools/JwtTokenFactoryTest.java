package com.stdakov.tools;

import com.stdakov.tools.dto.TestDto;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

public class JwtTokenFactoryTest {
    @Test
    public void test() throws IOException, ClassNotFoundException, InterruptedException {
        String property1 = "property1";
        String property2 = "property2";

        TestDto testDto = new TestDto(property1, property2);

        Assert.assertEquals(property1, testDto.getProperty1());
        Assert.assertEquals(property2, testDto.getProperty2());

        JwtTokenFactory jwtTokenFactory = new JwtTokenFactory();
        jwtTokenFactory.setExpSeconds(10);
        jwtTokenFactory.setPrivateKey("key1");
        String encrypted = jwtTokenFactory.generate(testDto);
        jwtTokenFactory.setPrivateKey("key2");
        String encrypted2 = jwtTokenFactory.generate(testDto);

        Assert.assertNotEquals(encrypted, encrypted2);

        TestDto decrypted = (TestDto) jwtTokenFactory.parse(encrypted);
        Assert.assertEquals(decrypted, testDto);
        Assert.assertEquals(decrypted.toString(), testDto.toString());
        Assert.assertEquals(property1, decrypted.getProperty1());
        Assert.assertEquals(property2, decrypted.getProperty2());

        TestDto decrypted2 = (TestDto) jwtTokenFactory.parse(encrypted2);
        Assert.assertEquals(decrypted2, testDto);
        Assert.assertEquals(decrypted2, decrypted);
        Assert.assertEquals(decrypted2.toString(), testDto.toString());
        Assert.assertEquals(decrypted2.toString(), decrypted.toString());
        Assert.assertEquals(property1, decrypted2.getProperty1());
        Assert.assertEquals(property2, decrypted2.getProperty2());

        Thread.sleep(10000);

        TestDto decryptedExpired = (TestDto) jwtTokenFactory.parse(encrypted);
    }
}
