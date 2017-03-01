package com.helospark.sparktemplatingplugin.support.classpath;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ClasspathScanCacheInitializationJob extends Job {
    private static final String JOB_NAME = "Preinitializing cache for content assist";
    private ClassInClasspathLocator classInClasspathLocator;
    private List<String> packagesToInit;

    public ClasspathScanCacheInitializationJob(ClassInClasspathLocator classInClasspathLocator, List<String> packagesToInit) {
        super(JOB_NAME);
        this.classInClasspathLocator = classInClasspathLocator;
        this.packagesToInit = packagesToInit;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        monitor.beginTask(JOB_NAME, packagesToInit.size());
        packagesToInit.stream()
                .map(Collections::singletonList)
                .forEach(basePackageSingletonList -> {
                    classInClasspathLocator.preInitializeCache(packagesToInit);
                    monitor.worked(1);
                });
        monitor.done();
        List<Class<?>> result = classInClasspathLocator.findClassesByName(packagesToInit, "SttMethod");
        System.out.println("##############################################");
        System.out.println(result);
        return Status.OK_STATUS;
    }

}
