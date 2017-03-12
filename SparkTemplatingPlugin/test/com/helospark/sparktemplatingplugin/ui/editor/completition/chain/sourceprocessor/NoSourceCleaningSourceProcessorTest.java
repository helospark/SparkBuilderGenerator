package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.sourceprocessor;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable.NoSourceCleaningSourceProcessor;

public class NoSourceCleaningSourceProcessorTest {
    private NoSourceCleaningSourceProcessor underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new NoSourceCleaningSourceProcessor();
    }

    @Test(dataProvider = "dataProvider")
    public void testSimple(String input, String expected) {
        // GIVEN

        // WHEN
        String result = underTest.process(input);

        // THEN
        assertEquals(result, expected);
    }

    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider() {
        return new Object[][] {
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println(i) // this\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println(i) \n" +
                                "}"
                },
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System/*.out*/.println(i) // this\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.println(i) \n" +
                                "}"
                },
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println(\"i\") // this\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println() \n" +
                                "}"
                },
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println('i') // this\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println() \n" +
                                "}"
                },
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println('i') /**/\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println() \n" +
                                "}"
                },
                {
                        "//for (int i = 0; i < 10; ++i) {\n" +
                                " System.out.println('i') /**/\n" +
                                "}",

                        "\n" +
                                " System.out.println() \n" +
                                "}"
                },
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " String a = /* asd\" */ \"StringContent\"\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " String a =  \n" +
                                "}",
                },
                {
                        "for (int i = 0; i < 10; ++i) {\n" +
                                " String a = /* asd\" */\"StringContent\"\n" +
                                "}",

                        "for (int i = 0; i < 10; ++i) {\n" +
                                " String a = \n" +
                                "}",
                },
        };
    }
}
