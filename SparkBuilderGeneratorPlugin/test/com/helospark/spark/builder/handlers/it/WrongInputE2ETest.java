package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WrongInputE2ETest extends BaseBuilderGeneratorIT {
    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
    }

    @Test
    public void testWhenCalledInNotJavaFileShouldNotChangeSource() throws Exception {
        // GIVEN
        given(handlerUtilWrapper.getActivePartId(dummyExecutionEvent)).willReturn("org.eclipse.jdt.ui.NotJdt");

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        verifyNoMoreInteractions(iBuffer);
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

}