package com.helospark.sparktemplatingplugin.support.classpath;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.helospark.sparktemplatingplugin.Activator;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class ClasspathScanCacheInitializationJob extends Job {
    private static final String JOB_NAME = "Preinitializing cache for content assist";
    private static final PluginLogger LOGGER = new PluginLogger(ClasspathScanCacheInitializationJob.class);
    private ClassInClasspathLocator classInClasspathLocator;
    private List<String> packagesToInit;

    public ClasspathScanCacheInitializationJob(ClassInClasspathLocator classInClasspathLocator, List<String> packagesToInit) {
        super(JOB_NAME);
        this.classInClasspathLocator = classInClasspathLocator;
        this.packagesToInit = packagesToInit;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask(JOB_NAME, packagesToInit.size());
            packagesToInit.stream()
                    .map(Collections::singletonList)
                    .forEach(basePackageSingletonList -> {
                        classInClasspathLocator.preInitializeCache(packagesToInit);
                        monitor.worked(1);
                    });
            monitor.done();
        } catch (Exception e) {
            LOGGER.error("Unable to inialize caches", e);
            return new Status(Status.ERROR, Activator.PLUGIN_ID, "Unable to inialize caches");
        }
        return Status.OK_STATUS;
    }

}
