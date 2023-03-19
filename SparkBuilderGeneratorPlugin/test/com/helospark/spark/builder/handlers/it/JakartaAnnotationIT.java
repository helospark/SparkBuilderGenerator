package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class JakartaAnnotationIT extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
    }

    @Test
    public void testUseJakartaAnnotation() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(true);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.use_jakarta_generated_annotation")).willReturn(true);

        String input = readClasspathFile("jakarta/input.tjava");
        String expectedResult = readClasspathFile("jakarta/output_with_jakarta_annotations.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testUseJavaxAnnotation() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(true);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.use_jakarta_generated_annotation")).willReturn(false);

        String input = readClasspathFile("jakarta/input.tjava");
        String expectedResult = readClasspathFile("jakarta/output_with_javax_annotations.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testShouldNotChangeCurrentAnnotationWhenBuilderIsRegenerated() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(true);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.use_jakarta_generated_annotation")).willReturn(true);

        String input = readClasspathFile("jakarta/output_with_javax_annotations.tjava");
        String expectedResult = readClasspathFile("jakarta/output_with_javax_annotations.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testShouldNotGenerateAnyAnnotationsWhenDisabled() throws Exception {
        // GIVEN
        given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(false);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(false);
        given(preferenceStore.getBoolean("org.helospark.builder.use_jakarta_generated_annotation")).willReturn(true);

        String input = readClasspathFile("jakarta/input.tjava");
        String expectedResult = readClasspathFile("jakarta/output_with_no_annotations.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }
}