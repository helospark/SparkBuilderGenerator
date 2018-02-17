package com.helospark.spark.builder.handlers.it;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderUserPreferenceDialogOpener;
import com.helospark.spark.builder.handlers.it.dummyService.RegularBuilderFilterDialogAnswerProvider;

public class RegularBuilderWithUserSelectedFieldsIT extends BaseBuilderGeneratorIT {
    @Mock
    private RegularBuilderUserPreferenceDialogOpener regularBuilderUserPreferenceDialogOpener;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
        given(preferenceStore.getBoolean("org.helospark.builder.showFieldFilterDialogForRegularBuilder")).willReturn(true);
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(regularBuilderUserPreferenceDialogOpener);
    }

    @Test(dataProvider = "filteredDialogDataProvider")
    public void testFilteringDialog(String inputFile, String expectedOutputFile, List<Integer> includedFields) throws Exception {
        // GIVEN
        RegularBuilderFilterDialogAnswerProvider regularBuilderFilterDialogAnswerProvider = new RegularBuilderFilterDialogAnswerProvider(includedFields, false);
        given(regularBuilderUserPreferenceDialogOpener.open(any(RegularBuilderDialogData.class))).willAnswer(regularBuilderFilterDialogAnswerProvider::provideAnswer);

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "filteredDialogDataProvider")
    public Object[][] filteredDialogDataProvider() {
        return new Object[][] {
                { "multi_field_input.tjava", "multi_field_output.tjava", asList(0, 1, 2, 3) },
                { "multi_field_input.tjava", "multi_field_output_with_only_first_two_field_included.tjava", asList(0, 1) },
                { "multi_field_input.tjava", "multi_field_output_with_no_fields_included.tjava", emptyList() },
                { "no_field_input.tjava", "no_field_output.tjava", emptyList() }
        };
    }

    @Test(dataProvider = "cancelPressedOnDialogGeneratorDataProvider")
    public void testFilteringDialogWhenCancelPressedShouldNotGenerateOrDeleteBuilder(String inputFile) throws Exception {
        // GIVEN
        given(regularBuilderUserPreferenceDialogOpener.open(any(RegularBuilderDialogData.class))).willReturn(empty());

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(inputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "cancelPressedOnDialogGeneratorDataProvider")
    public Object[][] cancelPressedOnDialogGeneratorDataProvider() {
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
