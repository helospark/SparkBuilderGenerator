package com.helospark.sparktemplatingplugin.scriptexport.job;

import java.io.FileOutputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptZipper;

public class ExportJob extends Job {
    private ScriptRepository scriptRepository;
    private ScriptZipper scriptZipper;
    private String fileName;

    public ExportJob(ScriptRepository scriptRepository, ScriptZipper scriptZipper, String fileName) {
        super("Exporting to file " + fileName);
        this.scriptRepository = scriptRepository;
        this.scriptZipper = scriptZipper;
        this.fileName = fileName;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask("Zipping", 2);
            List<ScriptEntity> export = scriptRepository.loadAll();
            byte[] zipData = scriptZipper.createZip(export);
            monitor.worked(1);
            saveFile(zipData, fileName);
            monitor.worked(1);
            monitor.done();
        } catch (Exception e) {
            e.printStackTrace();
            return new Status(Status.ERROR, "unknown", 1, "ERROR", e);
        }
        return Status.OK_STATUS;
    }

    private void saveFile(byte[] dataToExport, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(dataToExport);
        } catch (Exception e) {
            throw new RuntimeException("Cannot save file " + fileName, e);
        }
    }

}
