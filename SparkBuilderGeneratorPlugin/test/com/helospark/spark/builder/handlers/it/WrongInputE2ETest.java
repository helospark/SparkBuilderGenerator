package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class WrongInputE2ETest extends BaseBuilderGeneratorIT {
    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
    }

    @Test
    public void testWhenCalledInNotJavaFileShouldNotChangeSource() throws Exception {
        // GIVEN
        given(handlerUtilWrapper.getActivePartId(dummyExecutionEvent)).willReturn("org.eclipse.jdt.ui.NotJdt");

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        verify(dialogWrapper).openInformationDialog("No active Java editor",
                "To generate builder execute this command in an active Java editor");
    }

    @Test
    public void testWhenCalledOnCompilationUnitWithoutTypeShouldThrow() throws Exception {
        // GIVEN
        super.setInput("");

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        verify(dialogWrapper).openInformationDialog("Error", "No types are present in the current java file");
    }

    @Test(dataProvider = "templateNameProvider")
    public void testWhenCalledWithInvalidTemplate(String templateName, String validTemplates) throws Exception {
        // GIVEN
        super.setInput("public class TestClass { private String asd; }");
        given(preferenceStore.getString(templateName)).willReturn(of("[notValid]Template"));

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        verify(dialogWrapper).openInformationDialog("Error", "Illegal template 'notValid' in context of [notValid]Template. Valid templates: {" + validTemplates + "}");
    }

    @DataProvider(name = "templateNameProvider")
    public Object[][] templateNameProvider() {
        return new Object[][] {
                { "create_builder_method_pattern", "className=TestClass" },
                { "builder_class_name_pattern", "className=TestClass" },
                { "build_method_name", "className=TestClass" },
                { "builders_method_name_pattern", "fieldName=asd, FieldName=Asd" },
        };
    }

}