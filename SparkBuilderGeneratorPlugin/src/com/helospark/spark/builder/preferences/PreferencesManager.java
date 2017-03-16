package com.helospark.spark.builder.preferences;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreProvider;

/**
 * Default implementation of the {@link PreferencesManager}
 * 
 * @author maudrain
 */
public class PreferencesManager {
    private PreferenceStoreProvider preferenceStoreProvider;

    public PreferencesManager(PreferenceStoreProvider preferenceStoreProvider) {
        this.preferenceStoreProvider = preferenceStoreProvider;
    }

    public <T> T getPreferenceValue(PluginPreference<T> preference) {
        return preference.getCurrentPreferenceValue(preferenceStoreProvider.providerDefaultPreferenceStore());
    }
}
