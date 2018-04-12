package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class BuilderWithSuperSetterE2ETest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();
    @Mock
    private IType firstSuperClassType;
    @Mock
    private IType secondSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;
    @Mock
    private ICompilationUnit secondSuperClassICompilationUnit;

    private TypeExtractorAnswerProvider typeExtractorAnswerProvider;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        // first super class
        given(firstSuperClassType.getCompilationUnit()).willReturn(firstSuperClassICompilationUnit);
        given(firstSuperClassType.getElementName()).willReturn("TestSuperClass");

        // second super class
        given(secondSuperClassType.getCompilationUnit()).willReturn(secondSuperClassICompilationUnit);
        given(secondSuperClassType.getElementName()).willReturn("TestSuperSuperClass");

        // When getting the superClass return the correct type
        Map<String, IType> map = new HashMap<>();
        map.put("TestClass", firstSuperClassType);
        map.put("TestSuperClass", secondSuperClassType);
        typeExtractorAnswerProvider = new TypeExtractorAnswerProvider(map);

        given(iTypeExtractor.extract(any(TypeDeclaration.class)))
                .willAnswer(a -> typeExtractorAnswerProvider.provideAnswer(a));

        given(preferenceStore.getBoolean("org.helospark.builder.includeSetterFieldsFromSuperclass")).willReturn(true);
    }

    @Test(dataProvider = "superClassWithSetterDataProvider")
    public void testSuperClassWithSetters(String inputFile, String superClassFile, String expectedOutputFile) throws Exception {
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

    @DataProvider(name = "superClassWithSetterDataProvider")
    public Object[][] superClassWithSetterDataProvider() {
        return new Object[][] {
                { "superclass_with_setter/1_base_child_input.tjava", "superclass_with_setter/1_base_super_input.tjava",
                        "superclass_with_setter/1_base_output.tjava" },
                { "superclass_with_setter/1_base_child_input.tjava", "superclass_with_setter/2_base_super_input_with_duplicated_field.tjava",
                        "superclass_with_setter/2_base_output_with_duplicated_field.tjava" },
                { "superclass_with_setter/1_base_child_input.tjava", "superclass_with_setter/6_base_super_input_with_wrong_setter.tjava",
                        "superclass_with_setter/6_base_output_with_wrong_setter.tjava" }
        };
    }

    @Test
    public void testSuperClassWithSettersWhenSettingIsTurnedOff() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("org.helospark.builder.includeSetterFieldsFromSuperclass")).willReturn(false);

        String superClassInput = readClasspathFile("superclass_with_setter/1_base_super_input.tjava");
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);

        String input = readClasspathFile("superclass_with_setter/1_base_child_input.tjava");
        String expectedResult = readClasspathFile("superclass_with_setter/9_turned_off_setting_output.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testSuperClassWithSetterAndTwoSuperClasses() throws Exception {
        // GIVEN
        String superClassInput = readClasspathFile("superclass_with_setter/3_super_class.tjava");
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);

        String superSuperClassInput = readClasspathFile("superclass_with_setter/3_super_super_class.tjava");
        super.setCompilationUnitInput(secondSuperClassICompilationUnit, superSuperClassInput);

        String input = readClasspathFile("superclass_with_setter/1_base_child_input.tjava");
        String expectedResult = readClasspathFile("superclass_with_setter/3_base_output_with_two_superclass.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test(dataProvider = "superClassWithSuperSetterStagedSettingDataProvider")
    public void testSuperClassWithSetterGenerationForStagedBuilder(String inputFile, String superClassFile, String expectedOutputFile) throws Exception {
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

    @DataProvider(name = "superClassWithSuperSetterStagedSettingDataProvider")
    public Object[][] superClassWithSuperSetterStagedSettingDataProvider() {
        return new Object[][] {
                { "superclass_with_setter/1_base_child_input.tjava", "superclass_with_setter/1_base_super_input.tjava",
                        "superclass_with_setter/4_base_output_staged.tjava" }
        };
    }

    @Test
    public void testSetterGenerationWithoutSuperclass() throws Exception {
        // GIVEN
        String input = readClasspathFile("superclass_with_setter/5_base_child_input_without_superclass.tjava");
        String expectedResult = readClasspathFile("superclass_with_setter/5_base_output_without_superclass.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test(dataProvider = "superClassWithSetterAndDataProviderDataProvider")
    public void testSuperClassWithBothSettersAndConstructor(String inputFile, String superClassFile, String expectedOutputFile) throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("org.helospark.builder.includeParametersFromSuperclassConstructor")).willReturn(true);

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

    @DataProvider(name = "superClassWithSetterAndDataProviderDataProvider")
    public Object[][] superClassWithSetterAndDataProviderDataProvider() {
        return new Object[][] {
                { "superclass_with_setter/1_base_child_input.tjava", "superclass_with_setter/7_base_super_input_with_constructor.tjava",
                        "superclass_with_setter/7_base_output_with_constructor.tjava" },
                { "superclass_with_setter/1_base_child_input.tjava", "superclass_with_setter/8_base_super_input_with_constructor_and_setter.tjava",
                        "superclass_with_setter/8_base_output_with_deduplicated_field.tjava" },
        };
    }
}
