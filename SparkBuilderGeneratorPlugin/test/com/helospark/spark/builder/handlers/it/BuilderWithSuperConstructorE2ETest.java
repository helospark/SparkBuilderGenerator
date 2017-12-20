package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;
import com.helospark.spark.builder.handlers.it.dummyService.TypeExtractorAnswerProvider;

public class BuilderWithSuperConstructorE2ETest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();
    @Mock
    private IType firstSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;

    private TypeExtractorAnswerProvider typeExtractorAnswerProvider;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        given(firstSuperClassType.getCompilationUnit()).willReturn(firstSuperClassICompilationUnit);
        given(firstSuperClassType.getElementName()).willReturn("TestSuperClass");

        // When getting the superClass return the correct type
        typeExtractorAnswerProvider = new TypeExtractorAnswerProvider(Collections.singletonMap("TestClass", firstSuperClassType));
        given(iTypeExtractor.extract(any(TypeDeclaration.class)))
                .willAnswer(a -> typeExtractorAnswerProvider.provideAnswer(a));

        given(preferenceStore.getBoolean("org.helospark.builder.includeVisibleFieldsFromSuperclass")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.includeParametersFromSuperclassConstructor")).willReturn(true);
    }

    @Test(dataProvider = "superClassWithConstructorDataProvider")
    public void testSuperClassWithConstructor(String inputFile, String superClassFile, String expectedOutputFile) throws Exception {
        // GIVEN

        String superClassInput = readClasspathFile(superClassFile);
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "superClassWithConstructorDataProvider")
    public Object[][] superClassWithConstructorDataProvider() {
        return new Object[][] {
                { "superclass_with_constructor/base_child_input.tjava", "superclass_with_constructor/base_super_input.tjava",
                        "superclass_with_constructor/base_output.tjava" },
                { "superclass_with_constructor/repro_issue_child_input.tjava", "superclass_with_constructor/repro_issue_super_input.tjava",
                        "superclass_with_constructor/repro_issue_output.tjava" },
                { "superclass_with_constructor/base_child_input.tjava", "superclass_with_constructor/multi_constructor_super_input.tjava",
                        "superclass_with_constructor/multi_constructor_output_with_not_prefer_empty_constructor.tjava" },
                { "superclass_with_constructor/base_child_input.tjava", "superclass_with_constructor/private_constructor_super_input.tjava",
                        "superclass_with_constructor/private_constructor_output.tjava" },
                { "superclass_with_constructor/base_child_input.tjava", "superclass_with_constructor/no_visible_constructor_super_input.tjava",
                        "superclass_with_constructor/no_visible_constructor_output.tjava" },
        };
    }

    @Test
    public void testSuperClassWithConstructorWithPreferEmptyConstructor() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("org.helospark.builder.preferToUseEmptySuperclassConstructor")).willReturn(true);

        String superClassInput = readClasspathFile("superclass_with_constructor/multi_constructor_super_input.tjava");
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);

        String input = readClasspathFile("superclass_with_constructor/base_child_input.tjava");
        String expectedResult = readClasspathFile("superclass_with_constructor/multi_constructor_output_with_prefer_empty_constructor_setting.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test(dataProvider = "superClassWithConstructorStagedSettingDataProvider")
    public void testSuperClassWithConstructorStagedTest(String inputFile, String superClassFile, String expectedOutputFile) throws Exception {
        // GIVEN
        // set staged builder
        underTest = new GenerateStagedBuilderHandler();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));

        String superClassInput = readClasspathFile(superClassFile);
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "superClassWithConstructorStagedSettingDataProvider")
    public Object[][] superClassWithConstructorStagedSettingDataProvider() {
        return new Object[][] {
                { "superclass_with_constructor/base_child_input.tjava", "superclass_with_constructor/base_super_input.tjava",
                        "superclass_with_constructor/base_output_staged.tjava" },
                { "superclass_with_constructor/repro_issue_child_input.tjava", "superclass_with_constructor/repro_issue_super_input.tjava",
                        "superclass_with_constructor/repro_issue_output_staged.tjava" },
        };
    }

    @Test
    public void testClassWithoutSuperclass() throws Exception {
        // GIVEN
        String input = readClasspathFile("superclass_with_constructor/class_without_parent_input.tjava");
        String expectedResult = readClasspathFile("superclass_with_constructor/class_without_parent_output.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }
}
