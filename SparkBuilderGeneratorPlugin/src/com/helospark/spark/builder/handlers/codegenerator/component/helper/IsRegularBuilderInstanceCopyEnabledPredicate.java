package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_METHOD_TO_INSTANTIATE_BUILDER_BASED_ON_INSTANCE;

import java.util.List;
import java.util.function.Predicate;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Returns whether instance copying is enabled and possible.
 * @author helospark
 */
public class IsRegularBuilderInstanceCopyEnabledPredicate implements Predicate<RegularBuilderUserPreference> {
    private final PluginLogger pluginLogger = new PluginLogger();

    private PreferencesManager preferencesManager;

    public IsRegularBuilderInstanceCopyEnabledPredicate(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    @Override
    public boolean test(RegularBuilderUserPreference preference) {
        return preferencesManager.getPreferenceValue(CREATE_METHOD_TO_INSTANTIATE_BUILDER_BASED_ON_INSTANCE) &&
                !builderFieldsContainConstructorField(preference.getBuilderFields());
    }

    private boolean builderFieldsContainConstructorField(List<BuilderField> builderFields) {
        boolean containsConstructorField = builderFields
                .stream()
                .filter(field -> field instanceof ConstructorParameterSetterBuilderField)
                .findAny()
                .isPresent();
        if (containsConstructorField) {
            pluginLogger.info("Generating copy method for the builder was selected, but builder contains unaccessable fields");
        }
        return containsConstructorField;
    }
}
