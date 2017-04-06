package com.helospark.spark.builder.handlers.it;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertyInputDialogOpener;
import com.helospark.spark.builder.handlers.it.dummyService.ModifiedStagedDialogSettingsAnswerProvider;

public class StagedBuilderWithChangedDialogSettingsE2ETest extends BaseBuilderGeneratorIT {
    @Mock
    private StagedBuilderStagePropertyInputDialogOpener stagedBuilderStagePropertyInputDialogOpener;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateStagedBuilderHandler();
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(stagedBuilderStagePropertyInputDialogOpener);
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

    @Test
    public void testWithCancelClickedOnDialog() throws Exception {
        // GIVEN
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willReturn(null);
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_input.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}
