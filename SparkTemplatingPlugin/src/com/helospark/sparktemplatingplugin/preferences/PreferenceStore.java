package com.helospark.sparktemplatingplugin.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import com.helospark.sparktemplatingplugin.Activator;

public class PreferenceStore {

    public void setPreference(String key, String value) {
        try {
            IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
            prefs.put(key, value);
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPreference(String key) {
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
        return prefs.get(key, null);
    }

    public void setBooleanPreference(String key, boolean value) {
        setPreference(key, String.valueOf(value));
    }

    public boolean getBooleanPreference(String key) {
        return Boolean.valueOf(getPreference(key));
    }
}
