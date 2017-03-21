package com.helospark.spark.builder.preferences.impl;

import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

import com.helospark.spark.builder.NamedElement;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreWrapper;

public class NamedElementListPluginPreference<T extends NamedElement> extends AbstractPluginPreference<T> {
    private List<T> values;

    public NamedElementListPluginPreference(String key, String description, List<T> values, T defaultValue) {
        super(key, description, defaultValue);
        this.values = values;
    }

    @Override
    public T getCurrentPreferenceValue(PreferenceStoreWrapper preferenceStore) {
        String preferenceValue = preferenceStore.getString(this.getKey()).orElse(null);
        return values.stream()
                .filter(v -> v.getId().equals(preferenceValue))
                .findFirst()
                .orElse(null);
    }

    @Override
    public FieldEditor createFieldEditor(Composite parent) {
        String[][] entryNamesAndValues = new String[values.size()][2];
        for (int i = 0; i < values.size(); ++i) {
            entryNamesAndValues[i][0] = values.get(i).getDisplayName();
            entryNamesAndValues[i][1] = values.get(i).getId();
        }
        return new ComboFieldEditor(this.getKey(), this.getDescription(), entryNamesAndValues, parent);
    }

    @Override
    public void putDefaultValue(IEclipsePreferences preferences) {
        preferences.put(this.getKey(), this.getDefaultValue().getId());
    }

}
