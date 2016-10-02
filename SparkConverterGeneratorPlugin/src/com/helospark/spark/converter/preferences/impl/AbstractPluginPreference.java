package com.helospark.spark.converter.preferences.impl;

import com.helospark.spark.converter.preferences.PluginPreference;

/**
 * Abstract implementation of the {@link PluginPreference}
 * 
 * @author maudrain
 * @param <T>
 *            the type of preference value
 */
public abstract class AbstractPluginPreference<T> implements PluginPreference<T> {

    private final String key;
    private final String description;
    private final T defaultValue;

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
    public AbstractPluginPreference(String key, String description, T defaultValue) {
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    @Override
    public final String getKey() {
        return key;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public final T getDefaultValue() {
        return defaultValue;
    }
}
