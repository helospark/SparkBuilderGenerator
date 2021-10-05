package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class BuilderWithCustomMethodsIT extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(preferenceStore.getBoolean("org.helospark.builder.keepCustomMethodsInBuilder")).willReturn(true);
        given(preferenceStore.getString("builder_class_name_pattern")).willReturn(of("[className]Builder"));

        underTest = new GenerateRegularBuilderHandler();
    }

    @Test(dataProvider = "testCasesForRegularBuilderCustomMethod")
    public void testKeepCustomMethodForBuilder(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testCustomMethodsREmovedWhenKeepCustomMethodIsFalse() throws Exception {
        // GIVEN
        String input = readClasspathFile("custom_method/class_with_custom_add_method_and_helper_input.tjava");
        String expectedResult = readClasspathFile("custom_method/class_with_custom_add_method_and_helper_disabled_output.tjava");
        super.setInput(input);
        given(preferenceStore.getBoolean("org.helospark.builder.keepCustomMethodsInBuilder")).willReturn(false);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testKeepCustomMethodForStagedBuilder() throws Exception {
        // GIVEN
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));

        String input = readClasspathFile("custom_method/stage_build_custom_method_input.tjava");
        String expectedResult = readClasspathFile("custom_method/stage_build_custom_method_output.tjava");
        super.setInput(input);

        // WHEN
        new GenerateStagedBuilderHandler().execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForRegularBuilderCustomMethod")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "custom_method/ticket_example_input.tjava", "custom_method/ticket_example_output.tjava" },
                { "custom_method/class_with_custom_add_method_and_helper_input.tjava", "custom_method/class_with_custom_add_method_and_helper_output.tjava" },
                { "custom_method/custom_method_that_looks_like_builder_method_input.tjava", "custom_method/custom_method_that_looks_like_builder_method_output.tjava" },
                { "custom_method/removed_fields_input.tjava", "custom_method/removed_fields_output.tjava" }
        };
    }

}