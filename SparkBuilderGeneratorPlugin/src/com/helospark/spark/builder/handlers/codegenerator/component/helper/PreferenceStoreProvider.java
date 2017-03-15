package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

import com.helospark.spark.builder.Activator;

public class PreferenceStoreProvider {

    public PreferenceStoreWrapper providerDefaultPreferenceStore() {
        IPreferenceStore[] listOfPreferenceStoresToUse = new IPreferenceStore[] {
                Activator.getDefault().getPreferenceStore(),
                new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.jdt.core") // to get prefix, postfix preference value
        };

        return new PreferenceStoreWrapper(new ChainedPreferenceStore(listOfPreferenceStoresToUse));
    }
}
