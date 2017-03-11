package com.helospark.sparktemplatingplugin.scriptexport.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.helospark.sparktemplatingplugin.Activator;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class ExportJob extends Job {
    private static final PluginLogger LOGGER = new PluginLogger(ExportJob.class);
    private ExportJobWorker exportJobWorker;
    private String fileName;

    public ExportJob(ExportJobWorker exportJobWorker, String fileName) {
        super("Exporting to file " + fileName);
        this.exportJobWorker = exportJobWorker;
        this.fileName = fileName;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask("Zipping", 1);
            exportJobWorker.export(fileName);
            monitor.done();
        } catch (Exception e) {
            LOGGER.error("Unable to export templates", e);
            return new Status(Status.ERROR, Activator.PLUGIN_ID, 1, "Unable to export templates", e);
        }
        return Status.OK_STATUS;
    }

}
