package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;

import org.eclipse.jdt.core.JavaModelException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;

public class HappyFlowE2ETest extends BaseBuilderGeneratorIT {

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
    }

    @Test(dataProvider = "baseTestCasesProvider")
    public void testWithBaseSettings(String inputFile, String expectedOutputFile) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "baseTestCasesProvider")
    public Object[][] exampleFileProvider() {
        return new Object[][] {
                { "no_field_input.tjava", "no_field_output.tjava" },
                { "singlefield_input.tjava", "singlefield_output.tjava" },
                { "primitive_field_input.tjava", "primitive_field_output.tjava" },
                { "multi_field_input.tjava", "multi_field_output.tjava" },
                { "annotated_fields_input.tjava", "annotated_fields_output.tjava" },
                { "import_and_package_input.tjava", "import_and_package_base_output.tjava" },
                { "multi_field_staged_builder_output.tjava", "multi_field_output.tjava" },
                { "static_field_input.tjava", "static_field_output.tjava" }
        };
    }

    @Test(dataProvider = "testCasesWithSingleBooleanValueSetToTrue")
    public void testWithSingleBooleanPreferenceSetToTrue(String inputFile, String expectedOutputFile, String setting) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        given(preferenceStore.getBoolean(setting)).willReturn(true);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesWithSingleBooleanValueSetToTrue")
    public Object[][] testCasesWithSingleBooleanValueSetToTrue() {
        return new Object[][] {
                { "multi_field_input.tjava", "multi_field_output_with_builder_javadoc.tjava", "generate_javadoc_on_builder_method" },
                { "multi_field_input.tjava", "multi_field_output_with_class_javadoc.tjava", "generate_javadoc_on_builder_class" },
                { "multi_field_input.tjava", "multi_field_output_with_method_javadoc.tjava", "generate_javadoc_on_each_builder_method" },
                { "multi_field_input.tjava", "multi_field_output_with_return_nonnull.tjava", "add_nonnull_on_return" },
                { "multi_field_input.tjava", "multi_field_output_with_method_nonnull.tjava", "add_nonnull_on_parameter" },
                { "multi_field_input.tjava", "multi_field_output_with_generated.tjava", "add_generated_annotation" },
                { "multi_field_input.tjava", "multi_field_output.tjava", "override_previous_builder" },
                { "multi_field_output.tjava", "multi_field_output.tjava", "override_previous_builder" },
                { "import_and_package_input.tjava", "import_and_package_output_with_nonnull.tjava", "add_nonnull_on_parameter" },
        };
    }

    @Test
    public void testWithCustomBuilderNames() throws Exception {
        // GIVEN
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_output_with_custom_builder_names.tjava");

        given(preferenceStore.getString("create_builder_method_pattern")).willReturn(of("customGetBuilder"));
        given(preferenceStore.getString("builder_class_name_pattern")).willReturn(of("CustomBuilder"));
        given(preferenceStore.getString("build_method_name")).willReturn(of("customBuild"));
        given(preferenceStore.getString("builders_method_name_pattern")).willReturn(of("withCustom[FieldName]"));
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testWithCustomBuilderNamesWithDifferentTemplates() throws Exception {
        // GIVEN
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_output_with_templated_builder_names.tjava");

        given(preferenceStore.getString("create_builder_method_pattern")).willReturn(of("create[className]Builder"));
        given(preferenceStore.getString("builder_class_name_pattern")).willReturn(of("[className]Builder"));
        given(preferenceStore.getString("build_method_name")).willReturn(of("build[className]"));
        given(preferenceStore.getString("builders_method_name_pattern")).willReturn(of("with[fieldName]or[FieldName]"));
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test(dataProvider = "testCasesWithWithPrefixAndSuffixRemoval")
    public void testWithWithPrefixAndSuffixRemoval(String inputFileName, String outputFileName) throws Exception {
        // GIVEN
        String input = readClasspathFile(inputFileName);
        String expectedResult = readClasspathFile(outputFileName);

        given(preferenceStore.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(true);
        given(preferenceStore.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(of("prefix"));
        given(preferenceStore.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of("Suffix, B"));
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesWithWithPrefixAndSuffixRemoval")
    public Object[][] testCasesWithWithPrefixAndSuffixRemoval() {
        return new Object[][] {
                { "multi_field_input.tjava", "multi_field_output.tjava" }, // no
                // prefix
                // or
                // postfix
                { "prefix_input.tjava", "prefix_output.tjava" },
                { "suffix_input.tjava", "suffix_output.tjava" },
                { "prefix_suffix_input.tjava", "prefix_suffix_output.tjava" },
        };
    }

    @Test
    public void testWithFullBooleansTrue() throws Exception {
        // GIVEN
        String input = readClasspathFile("multi_field_output.tjava");
        String expectedResult = readClasspathFile("multi_field_output_full.tjava");

        given(preferenceStore.getBoolean("generate_javadoc_on_builder_method")).willReturn(true);
        given(preferenceStore.getBoolean("generate_javadoc_on_builder_class")).willReturn(true);
        given(preferenceStore.getBoolean("generate_javadoc_on_each_builder_method")).willReturn(true);
        given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(true);
        given(preferenceStore.getBoolean("add_nonnull_on_parameter")).willReturn(true);
        given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(true);
        given(preferenceStore.getBoolean("override_previous_builder")).willReturn(true);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testWithNoOverridingPreviousBuilderResultShouldContainTwoBuilders() throws Exception {
        // GIVEN
        String input = readClasspathFile("multi_field_output.tjava"); // multi_field_output_with_duplicate_builder
        String expectedResult = readClasspathFile("multi_field_output_with_duplicate_builder.tjava");

        given(preferenceStore.getBoolean("override_previous_builder")).willReturn(false);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }
}
