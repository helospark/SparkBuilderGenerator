package com.helospark.spark.builder.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.helospark.spark.builder.Activator;

public class PluginPreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        initializeDefaultValues(DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID));
    }

    private void initializeDefaultValues(IEclipsePreferences iEclipsePreferences) {
        for (PluginPreference<?> jeneratePreference : PluginPreferenceList.getAllPreferences()) {
            jeneratePreference.putDefaultValue(iEclipsePreferences);
        }
    }
}
