package com.helospark.sparktemplatingplugin.support;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import com.helospark.sparktemplatingplugin.Activator;

public class BundleVersionProvider {
    private static final String VERSION_SEPARATOR = ".";

    public String provideVersion() {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
        Version version = bundle.getVersion();
        return version.getMajor() + VERSION_SEPARATOR + version.getMinor() + VERSION_SEPARATOR + version.getMicro();
    }
}
