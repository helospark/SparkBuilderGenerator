package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.sourceprocessor;

import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable.RegexMatchingLocalVariableExtractor;

public class RegexMatchingLocalVariableExtractorTest {
    private RegexMatchingLocalVariableExtractor underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new RegexMatchingLocalVariableExtractor();
    }

    @Test(dataProvider = "dataProvider")
    public void testSimple(String input, Map<String, String> expected) {
        // GIVEN

        // WHEN
        Map<String, String> result = underTest.matchLocalVariableWithRegex(input);

        // THEN
        assertEquals(result, expected);
    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider() {
        return new Object[][] {
                {
                        "for (int i = 0; i < 10; ++i);",
                        Collections.singletonMap("i", "int")
                },
                {
                        "Integer asd = 3;\n" +
                                "for (asd = 0; i < 10; ++i);",
                        Collections.singletonMap("asd", "Integer")
                },
                {
                        "Integer asd = 3;\n" +
                                "for (int i = 0; i < 10; ++i);",
                        new HashMap<String, String>() {
                            {
                                put("asd", "Integer");
                                put("i", "int");
                            }
                        }
                },
                {
                        "Integer asd = 3;\n" +
                                "for (int i = 0; i < 10; ++i) {\n" +
                                "String h = \"a\";\n" +
                                "}",
                        new HashMap<String, String>() {
                            {
                                put("asd", "Integer");
                                put("i", "int");
                                put("h", "String");
                            }
                        }
                },
                {
                        "Integer asd = 3;\n" +
                                "for (int i = 0; i < 10; ++i) {\n" +
                                "String h;\n" +
                                "}",
                        new HashMap<String, String>() {
                            {
                                put("asd", "Integer");
                                put("i", "int");
                                put("h", "String");
                            }
                        }
                }
        };
    }
}
