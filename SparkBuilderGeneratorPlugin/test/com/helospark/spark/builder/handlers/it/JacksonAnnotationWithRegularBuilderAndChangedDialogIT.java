package com.helospark.spark.builder.handlers.it;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderUserPreferenceDialogOpener;

public class JacksonAnnotationWithRegularBuilderAndChangedDialogIT extends BaseBuilderGeneratorIT {
    @Mock
    private RegularBuilderUserPreferenceDialogOpener regularBuilderUserPreferenceDialogOpener;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();

        given(preferenceStore.getBoolean("org.helospark.builder.addJacksonDeserializeAnnotation")).willReturn(true);
        given(preferenceStore.getBoolean("org.helospark.builder.showFieldFilterDialogForRegularBuilder")).willReturn(true);
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(regularBuilderUserPreferenceDialogOpener);
    }

    @Test(dataProvider = "testCasesForRegularBuilder")
    public void testWithDefaultEnabled(String inputFile, String expectedOutputFile, boolean includeJacksonAnnotations) throws Exception {
        // GIVEN
        RegularBuilderDialogData dialogResult = RegularBuilderDialogData.builder()
                .withAddJacksonDeserializeAnnotation(includeJacksonAnnotations)
                .withShouldCreateCopyMethod(false)
                .withRegularBuilderFieldIncludeFieldIncludeDomains(
                        Arrays.asList(new RegularBuilderFieldIncludeFieldIncludeDomain(true, "from"), new RegularBuilderFieldIncludeFieldIncludeDomain(true, "to")))
                .build();
        given(regularBuilderUserPreferenceDialogOpener.open(any())).willReturn(Optional.of(dialogResult));

        underTest = new GenerateRegularBuilderHandler();
        String input = readClasspathFile(inputFile);
        String expectedResult = readClasspathFile(expectedOutputFile);
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @DataProvider(name = "testCasesForRegularBuilder")
    public Object[][] regularBuilderExampleFileProvider() {
        return new Object[][] {
                { "jackson/mail_input.tjava", "jackson/mail_with_default_methodnames_output.tjava", true },
                { "jackson/mail_input.tjava", "jackson/mail_with_no_jackson_annotations.tjava", false }
        };
    }

}