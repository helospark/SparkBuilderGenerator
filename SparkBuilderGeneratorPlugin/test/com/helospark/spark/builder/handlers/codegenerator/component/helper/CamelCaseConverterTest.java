package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CamelCaseConverterTest {
    private CamelCaseConverter underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new CamelCaseConverter();
    }

    @Test(dataProvider = "toLowerCamelCaseDataProvider")
    public void testToLowerCamelCaseShouldReturnExpectedResult(String input, String expected) {
        // GIVEN

        // WHEN
        String result = underTest.toLowerCamelCase(input);

        // THEN
        assertEquals(expected, result);
    }

    @DataProvider(name = "toLowerCamelCaseDataProvider")
    public Object[][] toLowerCamelCaseDataProvider() {
        return new Object[][] {
                { "ClassName", "className" },
                { "className", "className" },
                { "a", "a" },
                { "A", "A" },
                { null, null }
        };
    }

    @Test(dataProvider = "toUpperCamelCaseDataProvider")
    public void testToUpperCamelCaseShouldReturnExpectedResult(String input, String expected) {
        // GIVEN

        // WHEN
        String result = underTest.toUpperCamelCase(input);

        // THEN
        assertEquals(expected, result);
    }

    @DataProvider(name = "toUpperCamelCaseDataProvider")
    public Object[][] toUpperCamelCaseDataProvider() {
        return new Object[][] {
                { "className", "ClassName" },
                { "ClassName", "ClassName" },
                { "a", "a" },
                { "A", "A" },
                { null, null }
        };
    }
}
