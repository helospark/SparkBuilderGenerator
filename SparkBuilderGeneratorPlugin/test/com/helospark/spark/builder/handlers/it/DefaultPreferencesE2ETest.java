package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.NamedElement;
import com.helospark.spark.builder.handlers.GenerateDefaultBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateRegularBuilderHandler;
import com.helospark.spark.builder.handlers.GenerateStagedBuilderHandler;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertyInputDialogOpener;
import com.helospark.spark.builder.handlers.it.dummyService.NoDialogOperationPerformedStagedBuilderDialogAnswerProvider;
import com.helospark.spark.builder.preferences.PluginPreferenceList;

public class DefaultPreferencesE2ETest extends BaseBuilderGeneratorIT {
    private NoDialogOperationPerformedStagedBuilderDialogAnswerProvider dialogAnswerProvider = new NoDialogOperationPerformedStagedBuilderDialogAnswerProvider();
    @Mock
    private StagedBuilderStagePropertyInputDialogOpener stagedBuilderStagePropertyInputDialogOpener;

    @BeforeMethod
    public void beforeMethod() throws JavaModelException {
        super.init();
        given(stagedBuilderStagePropertyInputDialogOpener.open(any(List.class))).willAnswer(invocation -> dialogAnswerProvider.provideAnswer(invocation));
        setDefaultPreferences();
    }

    private void setDefaultPreferences() {
        PluginPreferenceList.getAllPreferences()
                .stream()
                .flatMap(preferenceGroup -> preferenceGroup.getPreferences().stream())
                .forEach(preference -> setPreferenceValue(preference.getKey(), preference.getDefaultValue()));
    }

    private void setPreferenceValue(String key, Object defaultValue) {
        if (defaultValue instanceof Boolean) {
            given(preferenceStore.getBoolean(key)).willReturn((Boolean) defaultValue);
        } else if (defaultValue instanceof String) {
            given(preferenceStore.getString(key)).willReturn(of((String) defaultValue));
        } else if (defaultValue instanceof NamedElement) {
            given(preferenceStore.getString(key)).willReturn(of(((NamedElement) defaultValue).getId()));
        } else {
            throw new IllegalArgumentException("Unknown preference, test should fail, preference class: " + defaultValue.getClass());
        }
    }

    @Override
    protected void diContainerOverrides() {
        super.diContainerOverrides();
        DiContainer.addDependency(stagedBuilderStagePropertyInputDialogOpener);
    }

    @Test
    public void testRegularBuilderWithDefaultPreferenceSettings() throws Exception {
        // GIVEN
        underTest = new GenerateRegularBuilderHandler();
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_output_for_regular_builder_with_default_preferences.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testDefaultBuilderWithDefaultPreferenceSettings() throws Exception {
        // GIVEN
        underTest = new GenerateDefaultBuilderHandler();
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_output_for_regular_builder_with_default_preferences.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

    @Test
    public void testStagedBuilderWithDefaultPreferenceSettings() throws Exception {
        // GIVEN
        underTest = new GenerateStagedBuilderHandler();
        String input = readClasspathFile("multi_field_input.tjava");
        String expectedResult = readClasspathFile("multi_field_output_for_staged_builder_with_default_preferences.tjava");
        super.setInput(input);

        // WHEN
        underTest.execute(dummyExecutionEvent);

        // THEN
        super.assertEqualsJavaContents(outputCaptor.getValue(), expectedResult);
    }

}
