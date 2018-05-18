package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;

public class JacksonAnnotationWithStagedBuilderIT extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(preferenceStore.getBoolean("org.helospark.builder.addJacksonDeserializeAnnotation")).willReturn(true);

        underTest = new GenerateStagedBuilderHandler();
    }

    @Test(dataProvider = "testCasesForStagedBuilder")
    public void testJacksonAnnotations(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForStagedBuilder")
    public Object[][] testCasesForStagedBuilder() {
        return new Object[][] {
                { "jackson/mail_input.tjava", "jackson/mail_with_staged_builder_output.tjava" }
        };
    }

}