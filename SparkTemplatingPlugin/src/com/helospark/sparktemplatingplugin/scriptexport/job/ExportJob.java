package com.helospark.sparktemplatingplugin.scriptexport.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ExportJob extends Job {
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
            e.printStackTrace();
            return new Status(Status.ERROR, "unknown", 1, "ERROR", e);
        }
        return Status.OK_STATUS;
    }

}
