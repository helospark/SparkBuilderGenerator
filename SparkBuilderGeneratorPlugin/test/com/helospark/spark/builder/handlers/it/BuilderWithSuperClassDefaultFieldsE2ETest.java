package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class BuilderWithSuperClassDefaultFieldsE2ETest extends BaseBuilderGeneratorIT {
    @Mock
    private IType firstSuperClassType;
    @Mock
    private ICompilationUnit firstSuperClassICompilationUnit;

    @Mock
    private IType secondSuperClassType;
    @Mock
    private ICompilationUnit secondSuperClassICompilationUnit;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        // set first superclass
        given(firstSuperClassType.getCompilationUnit()).willReturn(firstSuperClassICompilationUnit);
        given(firstSuperClassType.getElementName()).willReturn("A");

        // set second superclass
        given(secondSuperClassType.getCompilationUnit()).willReturn(secondSuperClassICompilationUnit);
        given(secondSuperClassType.getElementName()).willReturn("TestSuperSuperClass");

        given(preferenceStore.getBoolean("org.helospark.builder.includeVisibleFieldsFromSuperclass")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.initializeBuilderFieldWithDefaultValue")).willReturn(true);
    }

    @Test
    public void testSuperClassDefaultValue() throws Exception {
        // GIVEN
        given(iTypeExtractor.extract(any(TypeDeclaration.class)))
                .willReturn(of(firstSuperClassType))
                .willReturn(of(secondSuperClassType))
                .willReturn(empty());
        String superClassInput = readClasspathFile("superclass_with_default_value/superclass.tjava");
        super.setCompilationUnitInput(firstSuperClassICompilationUnit, superClassInput);
        String secondSuperClassInput = readClasspathFile("superclass_with_default_value/supersuperclass.tjava");
        super.setCompilationUnitInput(secondSuperClassICompilationUnit, secondSuperClassInput);

        String input = readClasspathFile("superclass_with_default_value/childclass.tjava");
        String expectedResult = readClasspathFile("superclass_with_default_value/result.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}
