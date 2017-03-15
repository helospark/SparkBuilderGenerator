package com.helospark.spark.builder.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreWrapper;

/**
 * Defines a plugin preference. Contains a key as unique identifier, a description to be shown to the user and a default
 * value of a certain type.
 * 
 * @author maudrain
 * @param <T> the type of default value attached to this preference.
 */
public interface PluginPreference<T> {

    /**
     * @return the key for this preference
     */
    String getKey();

    /**
     * @return the description of this preference
     */
    String getDescription();

    /**
     * @return the default value of this preference
     */
    T getDefaultValue();

    /**
     * Gets the current value for this preference
     * 
     * @param preferenceStore the preference store from which to extract the preference value
     * @return the current value for this preference
     */
    T getCurrentPreferenceValue(PreferenceStoreWrapper preferenceStore);

    /**
     * Creates a {@link FieldEditor} for this preference
     * 
     * @param parent the parent composite
     * @return the created {@link FieldEditor}
     */
    FieldEditor createFieldEditor(Composite parent);

    /**
     * Stores the default value for this preference
     * 
     * @param preferences the preferences
     */
    void putDefaultValue(IEclipsePreferences preferences);

}
