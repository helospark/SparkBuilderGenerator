package com.helospark.sparktemplatingplugin.scriptimport.job;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.helospark.sparktemplatingplugin.Activator;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class ImportJob extends Job {
    private static final PluginLogger LOGGER = new PluginLogger(ImportJob.class);
    private ImportJobWorker importJobWorker;
    private String zipFileName;

    public ImportJob(ImportJobWorker importJobWorker, String zipFileName) {
        super("Importing templates");
        this.importJobWorker = importJobWorker;
        this.zipFileName = zipFileName;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask("Importing templates", 2);
            ImportJobResult result = importJobWorker.importToScriptRepository(zipFileName);
            monitor.worked(1);

            List<String> skippedCommands = result.getSkippedCommands();
            if (!skippedCommands.isEmpty()) {
                reportSkippedCommandsOnUiThread(skippedCommands);
            }
            monitor.done();
        } catch (Exception e) {
            LOGGER.error("Unable to import templates", e);
            return new Status(Status.ERROR, Activator.PLUGIN_ID, 1, "Unable to import templates", e);
        }

        return Status.OK_STATUS;
    }

    private void reportSkippedCommandsOnUiThread(List<String> skippedCommands) {
        SkippedCommandUiJob skippedCommandUiJob = new SkippedCommandUiJob(skippedCommands);
        skippedCommandUiJob.schedule();
    }

}
