package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class BuilderCopyMethodIT extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(preferenceStore.getBoolean("org.helospark.builder.createBuilderCopyMethod")).willReturn(true);
        given(preferenceStore.getString("org.helospark.builder.copyBuilderMethodName")).willReturn(of("builderFrom"));
    }

    @Test(dataProvider = "testCasesForRegularBuilderCopyMethod")
    public void testCopyMethodForRegularBuilder(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        underTest = new GenerateRegularBuilderHandler();
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForRegularBuilderCopyMethod")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "mail_input.tjava", "mail_with_copy_method_output.tjava" },
                { "mail_with_copy_method_output.tjava", "mail_with_copy_method_output.tjava" }, // check previous builder deletion
        };
    }

    @Test(dataProvider = "testCasesWithGeneratedAnnotationAndJavadoc")
    public void testCopyMethodForRegularBuilderWithGeneratedAnnotationAndJavadoc(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("generate_javadoc_on_builder_method")).willReturn(true);

        underTest = new GenerateRegularBuilderHandler();
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesWithGeneratedAnnotationAndJavadoc")
    public Object[][] testCasesWithGeneratedAnnotationAndJavadoc() {
        return new Object[][] {
                { "mail_input.tjava", "mail_with_copy_method_output_when_generated_annotation_and_javadoc_is_enabled.tjava" },
                { "mail_with_copy_method_output_when_generated_annotation_and_javadoc_is_enabled.tjava",
                        "mail_with_copy_method_output_when_generated_annotation_and_javadoc_is_enabled.tjava" }, // check previous builder deletion
        };
    }

}