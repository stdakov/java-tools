package com.stdakov.tools.dto;

import java.io.Serializable;
import java.util.Objects;

public class TestDto implements Serializable {
    private final static long serialVersionUID = 1L;

    private String property1;
    private String property2;

    public TestDto() {

    }

    public TestDto(String property1, String property2) {
        this.property1 = property1;
        this.property2 = property2;
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDto testDto = (TestDto) o;
        return Objects.equals(property1, testDto.property1) &&
                Objects.equals(property2, testDto.property2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(property1, property2);
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "property1='" + property1 + '\'' +
                ", property2='" + property2 + '\'' +
                '}';
    }
}