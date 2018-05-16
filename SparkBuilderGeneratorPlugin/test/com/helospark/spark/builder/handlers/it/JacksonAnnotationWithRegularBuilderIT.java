package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.it.dummyService.FieldDeclarationAnswerProvider;

public class JacksonAnnotationWithRegularBuilderIT extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
        given(fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(any(BuilderField.class))).willAnswer(inv -> FieldDeclarationAnswerProvider.provideAnswer(inv));

        given(preferenceStore.getBoolean("org.helospark.builder.addJacksonDeserializeAnnotation")).willReturn(true);
    }

    @Test(dataProvider = "testCasesForRegularBuilder")
    public void testWithDefaultEnabled(String inputFile, String expectedOutputFile) throws Exception {
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

    @DataProvider(name = "testCasesForRegularBuilder")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "jackson/mail_input.tjava", "jackson/mail_with_default_methodnames_output.tjava" },
                { "jackson/mail_with_default_methodnames_output.tjava", "jackson/mail_with_default_methodnames_output.tjava" }, // previous removal
        };
    }

    @Test(dataProvider = "testCasesWithChangedDefault")
    public void testWithChangedDefault(String inputFile, String expectedOutputFile, String buildMethodName, String withName) throws Exception {
        // GIVEN
        given(preferenceStore.getString("build_method_name")).willReturn(of(buildMethodName));
        given(preferenceStore.getString("builders_method_name_pattern")).willReturn(of(withName));
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testWithChangedBuilderName() throws Exception {
        // GIVEN
        given(preferenceStore.getString("builder_class_name_pattern")).willReturn(of("[className]Builder"));
        String input = readClasspathFile("jackson/mail_input.tjava");
        String expectedResult = readClasspathFile("jackson/mail_with_changed_builder_output.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesWithChangedDefault")
    public Object[][] testCasesWithChangedDefault() {
        return new Object[][] {
                { "jackson/mail_input.tjava", "jackson/mail_with_changed_with_method_output.tjava", "build", "[fieldName]" },
                { "jackson/mail_input.tjava", "jackson/mail_with_changed_with_changed_method_output.tjava", "customBuild", "asd[FieldName]" },
        };
    }

    //
    //    @Test(dataProvider = "testCasesForStagedBuilderOptionals")
    //    public void testOptionalInitializationForStagedBuilder(String inputFile, String expectedOutputFile) throws Exception {
    //        // GIVEN
    //        underTest = new GenerateStagedBuilderHandler();
    //        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
    //        String input = readClasspathFile(inputFile);
    //        String expectedResult = readClasspathFile(expectedOutputFile);
    //        super.setInput(input);
    //
    //        // WHEN
    //        underTest.execute(dummyExecutionEvent);
    //
    //        // THEN
    //        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    //    }
    //
    //    @DataProvider(name = "testCasesForStagedBuilderOptionals")
    //    public Object[][] stagedBuilderExampleFileProvider() {
    //        return new Object[][] {
    //                { "optional_containing_input.tjava", "optional_containing_staging_builder_output.tjava" }
    //        };
    //    }
    //
    //    @Test
    //    public void testOptionalWithDisabledOptionalInitialization() throws Exception {
    //        // GIVEN
    //        underTest = new GenerateRegularBuilderHandler();
    //
    //        given(preferenceStore.getBoolean("org.helospark.builder.initializeOptionalFieldsToEmpty")).willReturn(false);
    //
    //        String input = readClasspathFile("optional_containing_input.tjava");
    //        String expectedResult = readClasspathFile("optional_containing_output_with_disabled_optional_initialization.tjava");
    //        super.setInput(input);
    //
    //        // WHEN
    //        underTest.execute(dummyExecutionEvent);
    //
    //        // THEN
    //        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    //    }
}