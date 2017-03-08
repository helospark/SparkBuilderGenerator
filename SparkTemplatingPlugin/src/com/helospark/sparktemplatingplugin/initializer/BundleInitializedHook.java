package com.helospark.sparktemplatingplugin.initializer;

import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.BundleContext;

import com.helospark.sparktemplatingplugin.initializer.examplescript.ExampleScriptInitializerJob;
import com.helospark.sparktemplatingplugin.initializer.examplescript.ExampleScriptInitializerVersionFilteringDecorator;

public class BundleInitializedHook {
    private ExampleScriptInitializerVersionFilteringDecorator exampleScriptInitializer;

    public BundleInitializedHook(ExampleScriptInitializerVersionFilteringDecorator exampleScriptInitializer) {
        this.exampleScriptInitializer = exampleScriptInitializer;
    }

    public void onBundleInitialized(BundleContext context) {
        initializeExampleScripts();
    }

    private void initializeExampleScripts() {
        ExampleScriptInitializerJob exampleScriptInitializerJob = new ExampleScriptInitializerJob(exampleScriptInitializer);
        exampleScriptInitializerJob.setPriority(Job.INTERACTIVE); // Highest priority
        exampleScriptInitializerJob.schedule();
    }
}
