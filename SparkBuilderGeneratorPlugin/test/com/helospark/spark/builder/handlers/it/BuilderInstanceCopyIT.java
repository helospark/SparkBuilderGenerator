package com.helospark.spark.builder.handlers.it;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
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

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderUserPreferenceDialogOpener;
import com.helospark.spark.builder.handlers.it.dummyService.RegularBuilderFilterDialogAnswerProvider;
import com.helospark.spark.builder.handlers.it.dummyService.TypeExtractorAnswerProvider;

public class BuilderInstanceCopyIT extends BaseBuilderGeneratorIT {
    @Mock
    private RegularBuilderUserPreferenceDialogOpener regularBuilderUserPreferenceDialogOpener;
    @Mock
    private IType firstSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(preferenceStore.getBoolean("org.helospark.builder.createBuilderCopyInstance")).willReturn(true);
        given(preferenceStore.getString("org.helospark.builder.copyBuilderInstanceMethodName")).willReturn(of("builderFrom"));

        underTest = new GenerateRegularBuilderHandler();
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(regularBuilderUserPreferenceDialogOpener);
    }

    @Test(dataProvider = "testCasesForRegularBuilderCopyInstance")
    public void testCopyMethodForRegularBuilder(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForRegularBuilderCopyInstance")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "mail_input.tjava", "mail_with_copy_method_output.tjava" },
                { "mail_with_copy_method_output.tjava", "mail_with_copy_method_output.tjava" }, // check previous builder deletion
        };
    }

    @Test(dataProvider = "testCasesWithGeneratedAnnotationAndJavadoc")
    public void testCopyMethodForRegularBuilderWithGeneratedAnnotationAndJavadoc(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("generate_javadoc_on_builder_method")).willReturn(true);

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesWithGeneratedAnnotationAndJavadoc")
    public Object[][] testCasesWithGeneratedAnnotationAndJavadoc() {
        return new Object[][] {
                { "mail_input.tjava", "mail_with_copy_method_output_when_generated_annotation_and_javadoc_is_enabled.tjava" },
                { "mail_with_copy_method_output_when_generated_annotation_and_javadoc_is_enabled.tjava",
                        "mail_with_copy_method_output_when_generated_annotation_and_javadoc_is_enabled.tjava" }, // check previous builder deletion
        };
    }

    @Test(dataProvider = "testCasesForRegularBuilderCopyInstanceWithUserChangedDialog")
    public void testCopyMethodForRegularBuilderWhenChangingDialogSetting(String inputFile, String expectedOutputFile, boolean dialogPreference) throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("org.helospark.builder.showFieldFilterDialogForRegularBuilder")).willReturn(true);
        RegularBuilderFilterDialogAnswerProvider regularBuilderFilterDialogAnswerProvider = new RegularBuilderFilterDialogAnswerProvider(asList(0, 1, 2, 3, 4), dialogPreference);
        given(regularBuilderUserPreferenceDialogOpener.open(any(RegularBuilderDialogData.class))).willAnswer(regularBuilderFilterDialogAnswerProvider::provideAnswer);

        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForRegularBuilderCopyInstanceWithUserChangedDialog")
    public Object[][] testCasesForRegularBuilderCopyInstanceWithUserChangedDialog() {
        return new Object[][] {
                { "mail_input.tjava", "mail_with_copy_method_output.tjava", true },
                { "mail_input.tjava", "mail_without_copy_method_output.tjava", false },
        };
    }

    @Test
    public void testCopyMethodForRegularBuilderWhenFieldsIncludeSuperConstructorShouldGenerateProperCopyMethod() throws Exception {
        // GIVEN
        given(firstSuperClassType.getCompilationUnit()).willReturn(firstSuperClassICompilationUnit);
        given(firstSuperClassType.getElementName()).willReturn("TestSuperClass");

        // When getting the superClass return the correct type
        TypeExtractorAnswerProvider typeExtractorAnswerProvider = new TypeExtractorAnswerProvider(Collections.singletonMap("TestClass", firstSuperClassType));
        given(iTypeExtractor.extract(any(TypeDeclaration.class)))
                .willAnswer(a -> typeExtractorAnswerProvider.provideAnswer(a));

        given(preferenceStore.getBoolean("org.helospark.builder.includeVisibleFieldsFromSuperclass")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.includeParametersFromSuperclassConstructor")).willReturn(true);

        String superClassInput = readClasspathFile("superclass_with_constructor/base_super_input.tjava");
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);

        String input = readClasspathFile("superclass_with_constructor/base_child_input.tjava");
        String expectedResult = readClasspathFile("superclass_with_constructor/base_output_with_copy_constructor.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}