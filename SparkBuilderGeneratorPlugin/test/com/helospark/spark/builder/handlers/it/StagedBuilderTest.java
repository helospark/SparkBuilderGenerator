package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class StagedBuilderTest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateStagedBuilderHandler();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
    }

    @Test(dataProvider = "baseTestCasesProvider")
    public void testStagedBuilderWithBaseSettings(String inputFile, String expectedOutputFile) throws Exception {
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
                { "singlefield_output.tjava", "singlefield_staged_output.tjava" },
                { "multi_field_staged_builder_output.tjava", "multi_field_staged_builder_output.tjava" },
                { "multi_field_output.tjava", "multi_field_staged_builder_output.tjava" },
                { "static_field_input.tjava", "static_field_output_with_staged_builder.tjava" },
                { "multi_field_output_for_staged_builder_with_default_preferences.tjava", "multi_field_staged_builder_output_with_leftover_generated_annotation.tjava" }
        };
    }

    @Test
    public void testStagedBuilderWithSkippedBuilderMethod() throws Exception {
        // GIVEN
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_output_for_staged_builder_with_skipped_builder_method.tjava");
        given(preferenceStore.getBoolean("org.helospark.builder.skipStaticBuilderMethod")).willReturn(true);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testStagedBuilderWithAllOptionsEnabledInPreferences() throws Exception {
        // GIVEN
        String input = readClasspathFile("mail_input.tjava");
        String expectedResult = readClasspathFile("mail_output_with_all_preferences_enabled.tjava");

        given(preferenceStore.getString("builder_class_name_pattern")).willReturn(of("Builder"));
        given(preferenceStore.getString("build_method_name")).willReturn(of("build"));
        given(preferenceStore.getString("builders_method_name_pattern")).willReturn(of("[fieldName]"));
        given(preferenceStore.getBoolean("generate_javadoc_on_builder_method")).willReturn(true);
        given(preferenceStore.getBoolean("generate_javadoc_on_builder_class")).willReturn(true);
        given(preferenceStore.getBoolean("generate_javadoc_on_each_builder_method")).willReturn(true);
        given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(true);
        given(preferenceStore.getBoolean("add_nonnull_on_parameter")).willReturn(true);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.generateJavadocOnStageInterface")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.skipStaticBuilderMethod")).willReturn(true);
        given(preferenceStore.getString("org.helospark.builder.stagedEditorLastStageInterfaceName")).willReturn(of("IBuildStage"));
        given(preferenceStore.getString("org.helospark.builder.stagedEditorStageInterfaceName")).willReturn(of("I[FieldName]Stage"));
        given(preferenceStore.getBoolean("org.helospark.builder.addGeneratedAnnotationOnStageInterface")).willReturn(true);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testStagedBuilderWithSkippedBuilderMethodOnEmptyClassShouldFallBackToUseBuilder() throws Exception {
        // GIVEN
        String input = readClasspathFile("no_field_input.tjava");
        String expectedResult = readClasspathFile("no_field_staged_output.tjava");
        given(preferenceStore.getBoolean("org.helospark.builder.skipStaticBuilderMethod")).willReturn(true);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}
