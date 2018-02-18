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

public class BuilderInstanceCopyForStagedBuilderIT extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateStagedBuilderHandler();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));

        given(preferenceStore.getBoolean("org.helospark.builder.createBuilderCopyInstance")).willReturn(true);
        given(preferenceStore.getString("org.helospark.builder.copyBuilderInstanceMethodName")).willReturn(of("builderFrom"));
    }

    @Test(dataProvider = "baseTestCasesProvider")
    public void testStagedBuilderWithEnabledCopyBuilderGenerationShouldNotChangeBehavior(String inputFile, String expectedOutputFile) throws Exception {
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
                { "annotated_fields_input.tjava", "annotated_fields_staged_output.tjava" } // test builder regeneration
        };
    }

}