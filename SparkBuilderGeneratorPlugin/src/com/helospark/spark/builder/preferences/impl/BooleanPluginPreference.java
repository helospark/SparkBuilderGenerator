package com.helospark.spark.builder.preferences.impl;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreWrapper;
import com.helospark.spark.builder.preferences.PluginPreference;

/**
 * Default implementation of the {@link PluginPreference} for Boolean
 * preferences
 * 
 * @author maudrain
 */
public final class BooleanPluginPreference extends AbstractPluginPreference<Boolean> {

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
    public BooleanPluginPreference(String key, String description, Boolean defaultValue) {
        super(key, description, defaultValue);
    }

    @Override
    public Boolean getCurrentPreferenceValue(PreferenceStoreWrapper preferenceStore) {
        return preferenceStore.getBoolean(this.getKey());
    }

    @Override
    public FieldEditor createFieldEditor(Composite parent) {
        return new BooleanFieldEditor(this.getKey(), this.getDescription(), parent);
    }

    @Override
    public void putDefaultValue(IEclipsePreferences preferences) {
        preferences.putBoolean(this.getKey(), this.getDefaultValue().booleanValue());
    }
}
