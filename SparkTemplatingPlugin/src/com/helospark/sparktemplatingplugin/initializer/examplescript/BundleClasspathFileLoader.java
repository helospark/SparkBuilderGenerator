package com.helospark.sparktemplatingplugin.initializer.examplescript;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.helospark.sparktemplatingplugin.Activator;

public class BundleClasspathFileLoader {

    public File loadFileFromClasspath(String path) {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
        URL fileURL = bundle.getEntry(path);
        try {
            return new File(FileLocator.resolve(fileURL).toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
