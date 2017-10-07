package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.FieldDeclarationAnswerProvider;

public class OptionalInitializingE2ETest extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        given(fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(any(FieldDeclaration.class))).willAnswer(inv -> FieldDeclarationAnswerProvider.provideAnswer(inv));
    }

    @Test(dataProvider = "testCasesForOptionals")
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

    @DataProvider(name = "testCasesForOptionals")
    public Object[][] exampleFileProvider() {
        return new Object[][] {
                { "optional_containing_input.tjava", "optional_containing_output.tjava" }
        };
    }
}