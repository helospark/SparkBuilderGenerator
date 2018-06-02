package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class DefaultConstructorIT extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        given(preferenceStore.getBoolean("org.helospark.builder.createPublicDefaultConstructor")).willReturn(true);
    }

    @Test(dataProvider = "testCasesForRegularBuilder")
    public void testWithDefaultEnabled(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForRegularBuilder")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "default_constructor/mail_input.tjava", "default_constructor/mail_output_with_default_constructor.tjava" },
                { "default_constructor/mail_input_with_existing_default_constructor.tjava", "default_constructor/mail_output_with_default_constructor.tjava" },
        };
    }

}
