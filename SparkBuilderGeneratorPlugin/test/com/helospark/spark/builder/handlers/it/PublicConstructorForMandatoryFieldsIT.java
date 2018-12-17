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

public class PublicConstructorForMandatoryFieldsIT extends BaseBuilderGeneratorIT {
    @Mock
    private RegularBuilderUserPreferenceDialogOpener regularBuilderUserPreferenceDialogOpener;

    @BeforeMethod
    public void setUp() throws JavaModelException {
        super.init();
        underTest = new GenerateRegularBuilderHandler();
        given(preferenceStore.getBoolean("org.helospark.builder.showFieldFilterDialogForRegularBuilder")).willReturn(true);
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(regularBuilderUserPreferenceDialogOpener);
    }

    @Test(dataProvider = "testCasesForRegularBuilder")
    public void testMandatoryFields(String inputFile, String expectedOutputFile, boolean[] mandatoryStatuses) throws Exception {
        // GIVEN
        RegularBuilderDialogData dialogResult = RegularBuilderDialogData.builder()
                .withAddJacksonDeserializeAnnotation(false)
                .withShouldCreateCopyMethod(false)
                .withCreateDefaultConstructor(false)
                .withCreatePublicConstructorWithMandatoryFields(true)
                .withRegularBuilderFieldIncludeFieldIncludeDomains(
                        Arrays.asList(
                                new RegularBuilderFieldIncludeFieldIncludeDomain(true, mandatoryStatuses[0], "value1"),
                                new RegularBuilderFieldIncludeFieldIncludeDomain(true, mandatoryStatuses[1], "value2"),
                                new RegularBuilderFieldIncludeFieldIncludeDomain(true, mandatoryStatuses[2], "value3")))
                .build();
        given(regularBuilderUserPreferenceDialogOpener.open(any())).willReturn(Optional.of(dialogResult));

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
                { "public_constructor_with_mandatory_fields/gh_input.tjava", "public_constructor_with_mandatory_fields/gh_output.tjava", new boolean[] { true, false, false } },
                { "public_constructor_with_mandatory_fields/gh_input.tjava", "public_constructor_with_mandatory_fields/gh_output_when_none_selected.tjava", new boolean[] { false, false, false } },
                { "public_constructor_with_mandatory_fields/gh_input.tjava", "public_constructor_with_mandatory_fields/gh_output_when_all_selected.tjava", new boolean[] { true, true, true } },
        };
    }

}
