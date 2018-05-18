package com.helospark.spark.builder.handlers.codegenerator.converter;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_JACKSON_DESERIALIZE_ANNOTATION;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_METHOD_TO_INSTANTIATE_BUILDER_BASED_ON_INSTANCE;

import java.util.ArrayList;
import java.util.List;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates the result of the dialog as user preferences.
 * <p>
 * Note that this converter also sets state in the preference manager, to override the default preference values.
 * @author helospark
 */
public class RegularBuilderUserPreferenceConverter {
    private PreferencesManager preferencesManager;

    public RegularBuilderUserPreferenceConverter(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    public RegularBuilderUserPreference convertOutput(List<BuilderField> builderFields, RegularBuilderDialogData result) {
        // TODO: automate the process
        // TODO: Saparate dialog input and output
        preferencesManager.addOverride(CREATE_METHOD_TO_INSTANTIATE_BUILDER_BASED_ON_INSTANCE, result.isShouldCreateInstanceCopy());
        preferencesManager.addOverride(ADD_JACKSON_DESERIALIZE_ANNOTATION, result.isAddJacksonDeserializeAnnotation());
        return RegularBuilderUserPreference.builder()
                .withBuilderFields(filterFieldsBasedOnDialogOutput(builderFields, result.getRegularBuilderFieldIncludeFieldIncludeDomains()))
                .withGenerateCopyMethod(result.isShouldCreateInstanceCopy())
                .withAddJacksonDeserializer(result.isAddJacksonDeserializeAnnotation())
                .build();
    }

    private List<BuilderField> filterFieldsBasedOnDialogOutput(List<BuilderField> builderFields,
            List<RegularBuilderFieldIncludeFieldIncludeDomain> dialogOutput) {
        if (dialogOutput.size() != builderFields.size()) {
            throw new IllegalStateException("Dialog should not change the number of elements in the list");
        }
        List<BuilderField> result = new ArrayList<>();
        for (int i = 0; i < dialogOutput.size(); ++i) {
            if (dialogOutput.get(i).isIncludeField()) {
                result.add(builderFields.get(i));
            }
        }
        return result;
    }

}
