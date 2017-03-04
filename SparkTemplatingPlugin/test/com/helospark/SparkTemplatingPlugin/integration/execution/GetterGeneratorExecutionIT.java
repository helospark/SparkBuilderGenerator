package com.helospark.SparkTemplatingPlugin.integration.execution;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.wrapper.SttField;

public class GetterGeneratorExecutionIT extends ExecutionITSetup {
    private String script = ""
            + "# for (SttField field : currentClass.getFields()) {\n"
            + "    public ${field.getTypeSignature()} get${field.getElementName()}() {\n"
            + "      return ${field.getElementName()};\n"
            + "    }\n"
            + "  \n"
            + "# }";

    private String expectedResult = ""
            + "    public String getfirstField() {\n"
            + "      return firstField;\n"
            + "    }\n"
            + "  \n"
            + "    public Integer getsecondField() {\n"
            + "      return secondField;\n"
            + "    }\n"
            + "  \n";

    @Override
    @BeforeTest
    public void initialize() {
        super.initialize();
        SttField firstField = mock(SttField.class);
        given(firstField.getTypeSignature()).willReturn("String");
        given(firstField.getElementName()).willReturn("firstField");

        SttField secondField = mock(SttField.class);
        given(secondField.getTypeSignature()).willReturn("Integer");
        given(secondField.getElementName()).willReturn("secondField");

        List<SttField> fields = Arrays.asList(firstField, secondField);
        given(type.getFields()).willReturn(fields);
    }

    @Test
    public void testGenerateShouldGenerateExpectedResult() {
        // GIVEN

        // WHEN
        underTest.template(dummyExecutionEvent, script);

        // THEN
        Assert.assertEquals(templatingResult.getBufferContent(), expectedResult);
    }

}
