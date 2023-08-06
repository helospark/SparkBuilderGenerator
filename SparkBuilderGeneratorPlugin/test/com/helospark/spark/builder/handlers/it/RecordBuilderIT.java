package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class RecordBuilderIT extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
    }

    @Test
    public void testCreateRecordBuilder() throws Exception {
        // GIVEN
        String input = readClasspathFile("record/record_input.tjava");
        String expectedResult = readClasspathFile("record/record_output.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testCreateRecordBuilderWithJsonAnnotation() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("org.helospark.builder.addJacksonDeserializeAnnotation")).willReturn(true);

        String input = readClasspathFile("record/record_input.tjava");
        String expectedResult = readClasspathFile("record/record_output_with_json_annotation.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}