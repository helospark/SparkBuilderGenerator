package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.it.dummyService.FieldDeclarationAnswerProvider;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class OptionalInitializingE2ETest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(any(BuilderField.class))).willAnswer(inv -> FieldDeclarationAnswerProvider.provideAnswer(inv));
    }

    @Test(dataProvider = "testCasesForRegularBuilderOptionals")
    public void testOptionalInitializationForRegularBuilder(String inputFile, String expectedOutputFile) throws Exception {
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

    @DataProvider(name = "testCasesForRegularBuilderOptionals")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "optional_containing_input.tjava", "optional_containing_output.tjava" },
                { "optional_without_import_input.tjava", "optional_without_import_output.tjava" }
        };
    }

    @Test(dataProvider = "testCasesForStagedBuilderOptionals")
    public void testOptionalInitializationForStagedBuilder(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
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

    @DataProvider(name = "testCasesForStagedBuilderOptionals")
    public Object[][] stagedBuilderExampleFileProvider() {
        return new Object[][] {
                { "optional_containing_input.tjava", "optional_containing_staging_builder_output.tjava" }
        };
    }

    @Test
    public void testOptionalWithDisabledOptionalInitialization() throws Exception {
        // GIVEN
        underTest = new GenerateRegularBuilderHandler();

        given(preferenceStore.getBoolean("org.helospark.builder.initializeOptionalFieldsToEmpty")).willReturn(false);

        String input = readClasspathFile("optional_containing_input.tjava");
        String expectedResult = readClasspathFile("optional_containing_output_with_disabled_optional_initialization.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }
}