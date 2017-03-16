package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Optional;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Wrapper around Eclipse's IPreferenceStore.
 * 
 * @author helospark
 */
public class PreferenceStoreWrapper {
    private IPreferenceStore preferenceStore;

    public PreferenceStoreWrapper(IPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable(preferenceStore.getString(key));
    }

    public Boolean getBoolean(String key) {
        return Optional.ofNullable(preferenceStore.getBoolean(key))
                .orElse(false);
    }
}
