package com.helospark.spark.builder;

import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {
    public static final String PLUGIN_ID = "com.helospark.SparkBuilderGenerator";

    private static final String ICON = "icons/builder_32.gif";

    private static Activator plugin;
    private static PluginLogger pluginLogger = new PluginLogger();

    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        // Have a log at the very first place in the lifecycle, where this plugin can log
        pluginLogger.info(PLUGIN_ID + " plugin has been started");
        DiContainer.initializeDiContainer();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        pluginLogger.info(PLUGIN_ID + " plugin has been stopped");
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static Image getIcon() {
        return Optional.ofNullable(getImageDescriptor(ICON))
                .map(descriptor -> descriptor.createImage())
                .orElse(null);
    }
}
