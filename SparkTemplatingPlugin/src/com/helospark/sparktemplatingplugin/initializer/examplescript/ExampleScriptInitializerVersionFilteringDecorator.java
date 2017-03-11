package com.helospark.sparktemplatingplugin.initializer.examplescript;

import java.util.Objects;

import com.helospark.sparktemplatingplugin.preferences.PreferenceStore;
import com.helospark.sparktemplatingplugin.support.BundleVersionProvider;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class ExampleScriptInitializerVersionFilteringDecorator {
    private static final PluginLogger LOGGER = new PluginLogger(ExampleScriptInitializerVersionFilteringDecorator.class);
    public static final String EXAMPLE_SCRIPTS_INITIALIZED_VERSION_KEY = "com.helospark.example.scripts.initialized.version";
    private ExampleScriptInitializer exampleScriptInitializer;
    private PreferenceStore preferenceStore;
    private BundleVersionProvider bundleVersionProvider;

    public ExampleScriptInitializerVersionFilteringDecorator(ExampleScriptInitializer exampleScriptInitializer, PreferenceStore preferenceStore,
            BundleVersionProvider bundleVersionProvider) {
        this.exampleScriptInitializer = exampleScriptInitializer;
        this.preferenceStore = preferenceStore;
        this.bundleVersionProvider = bundleVersionProvider;
    }

    public void initialize() {
        try {
            String currentVersion = bundleVersionProvider.provideVersion();
            if (!Objects.equals(currentVersion, preferenceStore.getPreference(EXAMPLE_SCRIPTS_INITIALIZED_VERSION_KEY))) {
                exampleScriptInitializer.initialize();
                preferenceStore.setPreference(EXAMPLE_SCRIPTS_INITIALIZED_VERSION_KEY, currentVersion);
            }
        } catch (Exception e) {
            LOGGER.error("Cannot initialize example scripts", e);
        }
    }
}
