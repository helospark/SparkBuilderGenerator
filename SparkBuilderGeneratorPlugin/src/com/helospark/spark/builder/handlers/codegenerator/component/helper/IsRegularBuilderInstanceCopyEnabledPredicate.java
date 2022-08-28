package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_METHOD_TO_INSTANTIATE_BUILDER_BASED_ON_INSTANCE;

import java.util.function.Predicate;

import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Returns whether instance copying is enabled and possible.
 * @author helospark
 */
public class IsRegularBuilderInstanceCopyEnabledPredicate implements Predicate<RegularBuilderUserPreference> {
    private PreferencesManager preferencesManager;

    public IsRegularBuilderInstanceCopyEnabledPredicate(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    @Override
    public boolean test(RegularBuilderUserPreference preference) {
        return preferencesManager.getPreferenceValue(CREATE_METHOD_TO_INSTANTIATE_BUILDER_BASED_ON_INSTANCE);
    }
}
