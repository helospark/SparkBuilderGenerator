package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.CurrentlySelectedApplicableClassesClassNameProvider;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class ClassSelectionE2ETest extends BaseBuilderGeneratorIT {
    @Mock
    protected CurrentlySelectedApplicableClassesClassNameProvider currentlySelectedApplicableClassesClassNameProvider;

    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
        given(preferenceStore.getBoolean("org.helospark.builder.alwaysGenerateBuilderToFirstClass")).willReturn(false);
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(currentlySelectedApplicableClassesClassNameProvider);
    }

    @Test(dataProvider = "multiClassTestFileNameProvider")
    public void testMultipleClassSelection(String inputFile, String expectedOutputFile, Optional<String> currentClassName) throws Exception {
        // GIVEN
        given(currentlySelectedApplicableClassesClassNameProvider.provideCurrentlySelectedClassName(any(ICompilationUnit.class)))
                .willReturn(currentClassName);
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "multiClassTestFileNameProvider")
    public Object[][] multiClassTestFileNameProvider() {
        return new Object[][] {
                { "multi_classes_input.tjava", "multi_classes_output_with_main_class_generated_builder.tjava", empty() },
                { "multi_classes_input.tjava", "multi_classes_output_with_main_class_generated_builder.tjava", of("TestClass") },
                { "multi_classes_input.tjava", "multi_classes_output_with_main_class_generated_builder.tjava", of("NoSuchClass") },
                { "multi_classes_input.tjava", "multi_classes_output_with_secondary_class_builder.tjava", of("SecondaryClass") },
                { "multi_classes_input.tjava", "multi_class_output_with_nested_static_class_builder.tjava", of("NestedStaticClass") },
                { "nested_class_input.tjava", "nested_class_output_with_second_nested_generated.tjava", of("SecondNestedClass") }
        };
    }

    @Test(dataProvider = "testInputWithException")
    public void testMultipleClassSelectionWhenExceptionIsThrownAlwaysGenerateToFirstClass(String inputFile, String outputFile) throws Exception {
        // GIVEN
        given(currentlySelectedApplicableClassesClassNameProvider.provideCurrentlySelectedClassName(any(ICompilationUnit.class)))
                .willThrow(new RuntimeException());
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(outputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testInputWithException")
    public Object[][] testInputWithException() {
        return new Object[][] {
                { "multi_classes_input.tjava", "multi_classes_output_with_main_class_generated_builder.tjava" },
                { "nested_class_input.tjava", "nested_class_output_with_root_type_builder_generated.tjava" },
                { "multi_field_input.tjava", "multi_field_output.tjava" },
        };
    }

    @Test(dataProvider = "multiClassTestWithStagedBuilder")
    public void testMultipleClassSelectionWithStagedBuilder(String inputFile, String expectedOutputFile, Optional<String> currentClassName) throws Exception {
        // GIVEN
        underTest = new GenerateStagedBuilderHandler();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(data -> dialogAnswerProvider.provideAnswer(data));

        given(currentlySelectedApplicableClassesClassNameProvider.provideCurrentlySelectedClassName(any(ICompilationUnit.class)))
                .willReturn(currentClassName);

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "multiClassTestWithStagedBuilder")
    public Object[][] multiClassTestWithStagedBuilder() {
        return new Object[][] {
                { "nested_class_input.tjava", "nested_class_output_with_staged_builder_on_second_nested_class.tjava", of("SecondNestedClass") },
                { "multi_classes_input.tjava", "multi_classes_output_with_staged_builder.tjava", of("NestedStaticClass") }
        };
    }

}
