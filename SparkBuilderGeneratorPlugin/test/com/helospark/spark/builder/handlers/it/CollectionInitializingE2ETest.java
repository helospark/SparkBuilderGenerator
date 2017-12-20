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

public class CollectionInitializingE2ETest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(any(BuilderField.class))).willAnswer(inv -> FieldDeclarationAnswerProvider.provideAnswer(inv));
    }

    @Test(dataProvider = "testCasesForRegularBuilderCollections")
    public void testCollectionsInitializationForRegularBuilder(String inputFile, String expectedOutputFile) throws Exception {
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

    @DataProvider(name = "testCasesForRegularBuilderCollections")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "class_with_collections_input.tjava", "class_with_collections_output.tjava" }
        };
    }

    @Test(dataProvider = "testCasesForStagedBuilderCollections")
    public void testCollectionsInitializationForStagedBuilder(String inputFile, String expectedOutputFile) throws Exception {
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

    @DataProvider(name = "testCasesForStagedBuilderCollections")
    public Object[][] stagedBuilderExampleFileProvider() {
        return new Object[][] {
                { "class_with_collections_input.tjava", "class_with_collections_staged_builder_output.tjava" }
        };
    }

    @Test
    public void testCollectionsWithDisabledCollectionInitialization() throws Exception {
        // GIVEN
        underTest = new GenerateRegularBuilderHandler();

        given(preferenceStore.getBoolean("org.helospark.builder.initializeCollectionToEmptyCollection")).willReturn(false);

        String input = readClasspathFile("class_with_collections_input.tjava");
        String expectedResult = readClasspathFile("class_with_collections_with_turned_off_setting_output.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }
}