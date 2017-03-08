package com.helospark.sparktemplatingplugin.initializer.examplescript;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ExampleScriptInitializerJob extends Job {
    private static final String EXAMPLE_SCRIPT_INITIALIZER_JOB_NAME = "Initializing example jobs...";
    private ExampleScriptInitializerVersionFilteringDecorator exampleScriptInitializer;

    public ExampleScriptInitializerJob(ExampleScriptInitializerVersionFilteringDecorator exampleScriptInitializer) {
        super(EXAMPLE_SCRIPT_INITIALIZER_JOB_NAME);
        this.exampleScriptInitializer = exampleScriptInitializer;
    }

    @Override
    protected IStatus run(IProgressMonitor paramIProgressMonitor) {
        exampleScriptInitializer.initialize();
        return Status.OK_STATUS;
    }

}
