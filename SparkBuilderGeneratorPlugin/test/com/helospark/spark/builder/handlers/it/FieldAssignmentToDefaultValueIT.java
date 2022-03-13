package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.it.dummyService.FieldDeclarationAnswerProvider;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class FieldAssignmentToDefaultValueIT extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @Mock
    private IType firstSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        given(fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(any(BuilderField.class))).willAnswer(inv -> FieldDeclarationAnswerProvider.provideAnswer(inv));

        given(preferenceStore.getBoolean("org.helospark.builder.initializeBuilderFieldWithDefaultValue")).willReturn(true);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
    }

    @Test
    public void testDefaultValueForRegularBuilder() throws Exception {
        // GIVEN
        String inputFile = "default_value/regular_builder_default_value_input.tjava";
        String expectedOutputFile = "default_value/regular_builder_default_value_output.tjava";
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testDefaultValueForRegularBuilderWithDisabledOption() throws Exception {
        // GIVEN
        String inputFile = "default_value/regular_builder_default_value_input.tjava";
        String expectedOutputFile = "default_value/regular_builder_default_value_with_disabled_option_output.tjava";

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);
        given(preferenceStore.getBoolean("org.helospark.builder.initializeBuilderFieldWithDefaultValue")).willReturn(false);
        given(preferenceStore.getBoolean("org.helospark.builder.initializeCollectionToEmptyCollection")).willReturn(false);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testDefaultValueForRegularBuilderWithDisabledOptionAndEnabledEmptyInitializer() throws Exception {
        // GIVEN
        String inputFile = "default_value/regular_builder_default_value_input.tjava";
        String expectedOutputFile = "default_value/regular_builder_default_value_with_disabled_option_and_enabled_empty_output.tjava";

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);
        given(preferenceStore.getBoolean("org.helospark.builder.initializeBuilderFieldWithDefaultValue")).willReturn(false);
        given(preferenceStore.getBoolean("org.helospark.builder.initializeCollectionToEmptyCollection")).willReturn(true);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testDefaultValueForStagedBuilder() throws Exception {
        // GIVEN
        String inputFile = "default_value/regular_builder_default_value_input.tjava";
        String expectedOutputFile = "default_value/staged_builder_default_value_output.tjava";
        underTest = new GenerateStagedBuilderHandler();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}
