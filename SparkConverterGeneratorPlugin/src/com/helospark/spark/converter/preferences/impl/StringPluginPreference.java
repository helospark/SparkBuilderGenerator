package com.helospark.spark.converter.preferences.impl;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

import com.helospark.spark.converter.preferences.PluginPreference;

/**
 * Default implementation of the {@link PluginPreference} for String preferences
 * 
 * @author maudrain
 */
public final class StringPluginPreference extends AbstractPluginPreference<String> {

    /**
     * Constructor
     * 
     * @param key
     *            the key for this preference
     * @param description
     *            the description for this preference
     * @param defaultValue
     *            the default value of this preference
     */
    public StringPluginPreference(String key, String description, String defaultValue) {
        super(key, description, defaultValue);
    }

    @Override
    public String getCurrentPreferenceValue(IPreferenceStore preferenceStore) {
        return preferenceStore.getString(this.getKey());
    }

    @Override
    public FieldEditor createFieldEditor(Composite parent) {
        return new StringFieldEditor(this.getKey(), this.getDescription(), parent);
    }

    @Override
    public void putDefaultValue(IEclipsePreferences preferences) {
        preferences.put(this.getKey(), this.getDefaultValue());
    }

}
