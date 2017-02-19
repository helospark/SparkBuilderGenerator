package com.helospark.sparktemplatingplugin.repository;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

public class EclipseRootFolderProvider {
    private static final String ROOT_CONFIGURATION_FOLDER = "com_helospark_SparkTemplatingPlugin";

    public File provideRootDirectory() {
        try {
            Location user = Platform.getConfigurationLocation();
            System.out.println(user.getURL().toString());
            File configurationLocation = new File(user.getURL().getPath());
            File pluginRootSettings = new File(configurationLocation, ROOT_CONFIGURATION_FOLDER);
            if (!pluginRootSettings.exists()) {
                pluginRootSettings.mkdir();
            }
            if (!pluginRootSettings.exists() || !pluginRootSettings.canWrite() || !pluginRootSettings.canRead()) {
                throw new RuntimeException("Cannot access to root directory.");
            }
            return pluginRootSettings;
        } catch (Exception e) {
            throw new RuntimeException("Cannot access config directory", e);

        }
    }
}
