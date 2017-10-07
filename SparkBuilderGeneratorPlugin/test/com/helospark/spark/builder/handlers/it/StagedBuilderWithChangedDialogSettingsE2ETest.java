package com.helospark.spark.builder.handlers.it;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.ModifiedStagedDialogSettingsAnswerProvider;

public class StagedBuilderWithChangedDialogSettingsE2ETest extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateStagedBuilderHandler();
    }

    @Test
    public void testWithReversedOrderAndOptional() throws Exception {
        // GIVEN
        ModifiedStagedDialogSettingsAnswerProvider dialogAnswerProvider = new ModifiedStagedDialogSettingsAnswerProvider(Arrays.asList(3, 2, 1, 0), Arrays.asList(0, 1, 2));
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_staged_builder_output_with_reversed_order_and_optional.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testWithAllOptionalFields() throws Exception {
        // GIVEN
        ModifiedStagedDialogSettingsAnswerProvider dialogAnswerProvider = new ModifiedStagedDialogSettingsAnswerProvider(asList(0, 1, 2, 3), emptyList());
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_staged_output_with_all_optional_fields.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testWithRemovedElementsInDialog() throws Exception {
        // GIVEN
        ModifiedStagedDialogSettingsAnswerProvider dialogAnswerProvider = new ModifiedStagedDialogSettingsAnswerProvider(asList(0, 1), asList(0, 1));
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_staged_output_with_only_first_two_fields_selected.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test(dataProvider = "cancelClickedOnDialogDataProvider")
    public void testWithCancelClickedOnDialogShouldNotCreateOrDeleteBuilder(String inputFile) throws Exception {
        // GIVEN
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willReturn(empty());
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_input.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "cancelClickedOnDialogDataProvider")
    public Object[][] cancelClickedOnDialogDataProvider() {
        return new Object[][] {
                { "multi_field_input.tjava" },
                { "multi_field_output.tjava" },
                { "no_field_input.tjava" },
                { "no_field_output.tjava" },
                { "nested_class_output_with_staged_builder_on_second_nested_class.tjava" },
                { "annotated_fields_output.tjava" }
        };
    }

}
