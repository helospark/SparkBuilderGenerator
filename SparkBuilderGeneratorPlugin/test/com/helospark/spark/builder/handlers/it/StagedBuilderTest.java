package com.helospark.spark.builder.handlers.it;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;

public class StagedBuilderTest extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateStagedBuilderHandler();
    }

    @Test(dataProvider = "baseTestCasesProvider")
    public void testWithBaseSettings(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "baseTestCasesProvider")
    public Object[][] exampleFileProvider() {
        return new Object[][] {
                { "multi_field_input.tjava", "multi_field_staged_builder_output.tjava" },
                { "annotated_fields_input.tjava", "annotated_fields_staged_output.tjava" },
                { "no_field_input.tjava", "no_field_staged_output.tjava" },
                { "primitive_field_input.tjava", "primitive_field_staged_output.tjava" },
                { "singlefield_input.tjava", "singlefield_staged_output.tjava" },
        };
    }

}
