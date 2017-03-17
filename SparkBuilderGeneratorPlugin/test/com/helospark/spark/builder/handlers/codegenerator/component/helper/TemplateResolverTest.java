package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TemplateResolverTest {
    private TemplateResolver underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new TemplateResolver();
    }

    @Test(dataProvider = "dataProvider")
    public void testResolveShouldReturnExpectedResult(String input, String expectedResult, Map<String, String> replacementes) {
        // GIVEN

        // WHEN
        String result = underTest.resolveTemplate(input, replacementes);

        // THEN
        assertEquals(expectedResult, result);
    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider() {
        return new Object[][] {
                { "test", "test", Collections.emptyMap() },
                { "test[template]", "testData", Collections.singletonMap("template", "Data") },
                { "test[template]", "test[template]", Collections.emptyMap() },
                { "test[a][b]", "testAB", new HashMap<String, String>() {
                    {
                        put("a", "A");
                        put("b", "B");
                    }
                } },
                { "[a]test[b]", "AtestB", new HashMap<String, String>() {
                    {
                        put("a", "A");
                        put("b", "B");
                    }
                } }
        };
    }
}
