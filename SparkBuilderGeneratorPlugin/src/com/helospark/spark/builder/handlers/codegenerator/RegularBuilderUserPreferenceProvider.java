package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_BUILDER_COPY_METHOD;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.REGULAR_BUILDER_SHOW_FIELD_FILTERING_DIALOG;

import java.util.List;
import java.util.Optional;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.handlers.codegenerator.converter.RegularBuilderDialogDataConverter;
import com.helospark.spark.builder.handlers.codegenerator.converter.RegularBuilderUserPreferenceConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Return the user preference for the regular builder.
 * This is done via a one-time preference window, not via the plugin-preference.
 * Optional.empty is returned if the user cancels the operation.
 * @author helospark
 */
public class RegularBuilderUserPreferenceProvider {
    private RegularBuilderUserPreferenceDialogOpener regularBuilderUserPreferenceDialogOpener;
    private PreferencesManager preferencesManager;
    private RegularBuilderDialogDataConverter regularBuilderDialogDataConverter;
    private RegularBuilderUserPreferenceConverter regularBuilderUserPreferenceConverter;

    public RegularBuilderUserPreferenceProvider(RegularBuilderUserPreferenceDialogOpener regularBuilderUserPreferenceDialogOpener, PreferencesManager preferencesManager,
            RegularBuilderDialogDataConverter regularBuilderDialogDataConverter, RegularBuilderUserPreferenceConverter regularBuilderUserPreferenceConverter) {
        this.regularBuilderUserPreferenceDialogOpener = regularBuilderUserPreferenceDialogOpener;
        this.preferencesManager = preferencesManager;
        this.regularBuilderDialogDataConverter = regularBuilderDialogDataConverter;
        this.regularBuilderUserPreferenceConverter = regularBuilderUserPreferenceConverter;
    }

    public Optional<RegularBuilderUserPreference> getPreference(List<BuilderField> builderFields) {
        RegularBuilderUserPreference defaultRregularBuilderUserPreference = createDefaultPreference(builderFields);
        if (preferencesManager.getPreferenceValue(REGULAR_BUILDER_SHOW_FIELD_FILTERING_DIALOG)) {
            RegularBuilderDialogData regularBuilderDialogData = regularBuilderDialogDataConverter.convertInput(defaultRregularBuilderUserPreference);
            Optional<RegularBuilderDialogData> dialogOutput = regularBuilderUserPreferenceDialogOpener.open(regularBuilderDialogData);
            return dialogOutput.map(result -> regularBuilderUserPreferenceConverter.convertOutput(builderFields, result));
        } else {
            return Optional.of(defaultRregularBuilderUserPreference);
        }
    }

    private RegularBuilderUserPreference createDefaultPreference(List<BuilderField> builderFields) {
        return RegularBuilderUserPreference.builder()
                .withBuilderFields(builderFields)
                .withGenerateCopyMethod(preferencesManager.getPreferenceValue(CREATE_BUILDER_COPY_METHOD))
                .build();
    }

}
