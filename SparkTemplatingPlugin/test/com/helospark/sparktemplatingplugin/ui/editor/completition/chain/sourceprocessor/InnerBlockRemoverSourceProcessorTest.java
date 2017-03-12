package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.sourceprocessor;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable.InnerBlockRemoverSourceProcessor;

public class InnerBlockRemoverSourceProcessorTest {
    private InnerBlockRemoverSourceProcessor underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new InnerBlockRemoverSourceProcessor();
    }

    @Test(dataProvider = "dataProvider")
    public void testSimple(String input, String expectedResult) {
        // GIVEN

        // WHEN
        String result = underTest.process(input);

        // THEN
        assertEquals(result, expectedResult);
    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider() {
        return new Object[][] {
                {
                        "if () {\n" +
                                "  if () { asd }\n" +
                                "  if() {\n" +
                                "     a",

                        "if () {\n" +
                                "  if () \n" +
                                "  if() {\n" +
                                "     a"
                },
                {
                        "int a; {\n" +
                                "int b; {\n" +
                                "   }\n" +
                                "}\n" +
                                "int b;",

                        "int a; \n" +
                                "int b;"
                }
        };
    }
}
