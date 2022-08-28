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

public class BuilderWithSuperClassInstanceCopyFieldsE2ETest extends BaseBuilderGeneratorIT {
    @Mock
    private IType firstSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;

    @Mock
    private ICompilationUnit secondSuperClassICompilationUnit;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        // set first superclass
        given(firstSuperClassType.getCompilationUnit()).willReturn(firstSuperClassICompilationUnit);
        given(firstSuperClassType.getElementName()).willReturn("TestSuperClass");

        given(preferenceStore.getBoolean("org.helospark.builder.includeVisibleFieldsFromSuperclass")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.createBuilderCopyInstance")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.includeSetterFieldsFromSuperclass")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.includeParametersFromSuperclassConstructor")).willReturn(true);
    }

    @Test(dataProvider = "simpleExtendsDataProvider")
    public void testSimpleExtends(String inputFile, String superClassFile, String expectedOutputFile) throws Exception {
        // GIVEN
        TypeExtractorAnswerProvider typeExtractorAnswerProvider = new TypeExtractorAnswerProvider(Collections.singletonMap("TestClass", firstSuperClassType));
        given(iTypeExtractor.extract(any(TypeDeclaration.class)))
                .willAnswer(a -> typeExtractorAnswerProvider.provideAnswer(a));
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

    @DataProvider(name = "simpleExtendsDataProvider")
    public Object[][] simpleExtendsDataProvider() {
        return new Object[][] {
                { "superclass_instance_copy/1_base_child_input.tjava", "superclass_instance_copy/1_base_super_input.tjava", "superclass_instance_copy/1_base_output.tjava" },
                { "superclass_instance_copy/2_base_child_input.tjava", "superclass_instance_copy/2_base_super_input.tjava", "superclass_instance_copy/2_base_output.tjava" },
                { "superclass_instance_copy/3_base_child_input.tjava", "superclass_instance_copy/3_base_super_input.tjava", "superclass_instance_copy/3_base_output.tjava" },
                { "superclass_instance_copy/4_base_child_input.tjava", "superclass_instance_copy/4_base_super_input.tjava", "superclass_instance_copy/4_base_output.tjava" },
        };
    }

}
