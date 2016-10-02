package com.helospark.spark.converter.preferences;

import com.helospark.spark.converter.Activator;

/**
 * Default implementation of the {@link PreferencesManager}
 * 
 * @author maudrain
 */
public final class PreferencesManager {

    public <T> T getPreferenceValue(PluginPreference<T> preference) {
        return preference.getCurrentPreferenceValue(Activator.getDefault().getPreferenceStore());
    }
}
