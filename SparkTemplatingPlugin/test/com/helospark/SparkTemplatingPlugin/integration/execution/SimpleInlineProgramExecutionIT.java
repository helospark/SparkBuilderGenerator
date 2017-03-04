package com.helospark.SparkTemplatingPlugin.integration.execution;

import static org.mockito.BDDMockito.given;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SimpleInlineProgramExecutionIT extends ExecutionITSetup {

    @BeforeTest
    public void initalize() {
        super.initialize();
    }

    @BeforeMethod
    public void beforeMethod() {
        templatingResult.clearBuffer();
        given(type.getElementName()).willReturn("mockName");
    }

    @Test(dataProvider = "programWithExpectedResult")
    public void testSimpleStringProgramWithExpectedResult(String program, String expected) {
        // GIVEN

        // WHEN
        underTest.template(dummyExecutionEvent, program);

        // THEN
        String content = templatingResult.getBufferContent();
        Assert.assertEquals(content, expected);
    }

    @DataProvider(name = "programWithExpectedResult")
    public static Object[][] createSuccessData() {
        return new Object[][] {
                { "test", "test" },
                { "hello\nworld", "hello\nworld" },
                { "# for (int i = 0; i < 3; ++i)\n${i}", "012" },
                { "# for (int i = 0; i < 3; ++i) { \n${i}\n # }", "0\n1\n2\n" },
                { "${currentClass.getElementName()}", "mockName" },
                { "currentClass.getElementName()", "currentClass.getElementName()" },
                { "#result.append(\"hey\")", "hey" },
                { "#String test = \"hey\";\n # result.append(test)", "hey" },
                { "This is the ${currentClass.getElementName()} class", "This is the mockName class" },
                { "#if (currentClass.getElementName().equals(\"mockName\")) { \nTrue\n # }", "True\n" },
                { "#if (!currentClass.getElementName().equals(\"mockName\")) {\nTrue\n # } else {\nFalse\n#}", "False\n" },
                { "Is it mockName: ${currentClass.getElementName().equals(\"mockName\") ? \"True\" : \"False\" }?", "Is it mockName: True?" },
        };
    }
}
