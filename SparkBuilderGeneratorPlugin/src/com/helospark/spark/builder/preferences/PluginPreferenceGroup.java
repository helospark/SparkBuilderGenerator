package com.helospark.spark.builder.preferences;

import java.util.List;

/**
 * Represent a preference grouping.
 * @author helospark
 */
public class PluginPreferenceGroup {
    private String name;
    private List<PluginPreference<?>> preferences;

    public PluginPreferenceGroup(String name, List<PluginPreference<?>> preferences) {
        this.name = name;
        this.preferences = preferences;
    }

    public String getName() {
        return name;
    }

    public List<PluginPreference<?>> getPreferences() {
        return preferences;
    }

}
