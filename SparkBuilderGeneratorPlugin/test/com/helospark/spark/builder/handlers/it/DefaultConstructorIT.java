package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Collections;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.TypeExtractorAnswerProvider;

public class DefaultConstructorIT extends BaseBuilderGeneratorIT {
    @Mock
    private IType firstSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;
    private TypeExtractorAnswerProvider typeExtractorAnswerProvider;

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

    @Test
    public void testBuilderShouldNotGenerateDefaultConstructorWhenSuperConstructorIsCalled() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("org.helospark.builder.includeVisibleFieldsFromSuperclass")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.includeParametersFromSuperclassConstructor")).willReturn(true);

        given(firstSuperClassType.getCompilationUnit()).willReturn(firstSuperClassICompilationUnit);
        given(firstSuperClassType.getElementName()).willReturn("TestSuperClass");

        // When getting the superClass return the correct type
        typeExtractorAnswerProvider = new TypeExtractorAnswerProvider(Collections.singletonMap("TestClass", firstSuperClassType));
        given(iTypeExtractor.extract(any(TypeDeclaration.class)))
                .willAnswer(a -> typeExtractorAnswerProvider.provideAnswer(a));

        String superClassInput = readClasspathFile("default_constructor/base_super_input.tjava");
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);
        String input = readClasspathFile("default_constructor/base_child_input.tjava");
        String expectedResult = readClasspathFile("default_constructor/base_output.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testEnabledGeneratedAnnotation() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        String input = readClasspathFile("default_constructor/mail_input.tjava");
        String expectedResult = readClasspathFile("default_constructor/mail_output_with_annotated_default_constructor.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}
