package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertyInputDialogOpener;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class StagedBuilderTest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();
    @Mock
    private StagedBuilderStagePropertyInputDialogOpener stagedBuilderStagePropertyInputDialogOpener;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateStagedBuilderHandler();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(stagedBuilderStagePropertyInputDialogOpener);
    }

    @Test(dataProvider = "baseTestCasesProvider")
    public void testWithBaseSettings(String inputFile, String expectedOutputFile) throws Exception {
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
        };
    }

}
