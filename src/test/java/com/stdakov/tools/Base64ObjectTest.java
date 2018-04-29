package com.stdakov.tools;

import com.stdakov.tools.dto.TestDto;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class Base64ObjectTest {
    @Test
    public void testEncryption() throws IOException, ClassNotFoundException {
        String property1 = "property1";
        String property2 = "property2";

        TestDto testDto = new TestDto(property1, property2);

        Assert.assertEquals(property1, testDto.getProperty1());
        Assert.assertEquals(property2, testDto.getProperty2());

        String encrypted = Base64Object.toString(testDto);

        Assert.assertTrue(encrypted != null);
        Assert.assertTrue(!encrypted.isEmpty());

        TestDto testDtoDecrypted = (TestDto) Base64Object.fromString(encrypted);

        Assert.assertEquals(testDto, testDtoDecrypted);
        Assert.assertEquals(testDto.toString(), testDtoDecrypted.toString());
        Assert.assertEquals(property1, testDtoDecrypted.getProperty1());
        Assert.assertEquals(property1, testDtoDecrypted.getProperty1());
        Assert.assertEquals(testDto.getProperty1(), testDtoDecrypted.getProperty1());
        Assert.assertEquals(testDto.getProperty2(), testDtoDecrypted.getProperty2());
    }
}
