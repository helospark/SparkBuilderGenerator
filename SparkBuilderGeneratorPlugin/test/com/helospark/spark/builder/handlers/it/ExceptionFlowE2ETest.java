package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderAstRemover;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.exception.PluginException;

public class ExceptionFlowE2ETest extends BaseBuilderGeneratorIT {
    @Mock
    private RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator regularBuilderCompilationUnitGenerator;
    @Mock
    private BuilderAstRemover builderAstRemover;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(builderAstRemover);
        DiContainer.addDependency(regularBuilderCompilationUnitGenerator);
        given(regularBuilderCompilationUnitGenerator.canHandle(BuilderType.REGULAR)).willReturn(true);
    }

    @Test
    public void testWhenPreviousBuilderRemovingFailsShouldShowDialog() throws Exception {
        // GIVEN
        BuilderRemover builderRemover = DiContainer.getDependency(BuilderRemover.class);
        willThrow(new RuntimeException("Cause"))
                .given(builderAstRemover)
                .removeBuilder(any(ASTRewrite.class), any(CompilationUnit.class));
        super.setInput("class TestClass {}");
        CompilationUnitModificationDomain dummyCompilationUnitModificationDomain = CompilationUnitModificationDomain.builder().build();

        // WHEN
        builderRemover.removeExistingBuilderWhenNeeded(dummyCompilationUnitModificationDomain);

        // THEN
        verify(dialogWrapper).openInformationDialog("Error", "Error removing previous builder, skipping");
    }

    @Test
    public void testWhenPluginExceptionOccurresShouldShowDialog() throws Exception {
        // GIVEN
        willThrow(new PluginException("Cause"))
                .given(regularBuilderCompilationUnitGenerator)
                .generateBuilder(any(CompilationUnitModificationDomain.class));
        super.setInput("class TestClass {}");

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        verify(dialogWrapper).openInformationDialog("Error", "Cause");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Cause")
    public void testWhenUnexpectedExceptionOccurresShouldThrowExceptionOut() throws Exception {
        // GIVEN
        RuntimeException unexpectedException = new RuntimeException("Cause");
        willThrow(unexpectedException)
                .given(regularBuilderCompilationUnitGenerator)
                .generateBuilder(any(CompilationUnitModificationDomain.class));
        super.setInput("class TestClass {}");

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
    }

    @Test
    public void testWhenUnexpectedExceptionOccurresShouldShowErrorDialog() throws Exception {
        // GIVEN
        RuntimeException unexpectedException = new RuntimeException("Cause");
        willThrow(unexpectedException)
                .given(regularBuilderCompilationUnitGenerator)
                .generateBuilder(any(CompilationUnitModificationDomain.class));
        super.setInput("class TestClass {}");

        // WHEN
        try {
            underTest.execute(dummyExecutionEvent);
        } catch (Exception e) {
        }
        // THEN
        verify(dialogWrapper).openErrorDialogWithStacktrace("Error",
                "This error should not have happened!\nYou can create an issue on https://github.com/helospark/SparkTools with the below stacktrace",
                unexpectedException);
    }

    @Test
    public void testWhenUnexpectedErrorOccurresShouldShowErrorDialog() throws Exception {
        // GIVEN
        NoSuchMethodError unexpectedException = new NoSuchMethodError("Cause");
        willThrow(unexpectedException)
                .given(regularBuilderCompilationUnitGenerator)
                .generateBuilder(any(CompilationUnitModificationDomain.class));
        super.setInput("class TestClass {}");

        // WHEN
        try {
            underTest.execute(dummyExecutionEvent);
        } catch (Exception e) {
        }
        // THEN
        verify(dialogWrapper).openErrorDialogWithStacktrace("Error",
                "This error should not have happened!\nYou can create an issue on https://github.com/helospark/SparkTools with the below stacktrace",
                unexpectedException);
    }
}
