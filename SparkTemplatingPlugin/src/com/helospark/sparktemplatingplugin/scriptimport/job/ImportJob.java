package com.helospark.sparktemplatingplugin.scriptimport.job;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptUnzipper;

public class ImportJob extends Job {
    private ScriptRepository scriptRepository;
    private ScriptUnzipper scriptUnzipper;
    private String zipFileName;

    public ImportJob(ScriptUnzipper scriptUnzipper, ScriptRepository scriptRepository, String zipFileName) {
        super("Importing templates");
        this.scriptRepository = scriptRepository;
        this.scriptUnzipper = scriptUnzipper;
        this.zipFileName = zipFileName;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask("Importing templates", 2);
            List<String> skippedCommands = new ArrayList<>();
            List<ScriptEntity> scriptEntities = scriptUnzipper.extract(zipFileName);
            monitor.worked(1);
            for (ScriptEntity scriptEntity : scriptEntities) {
                if (scriptRepository.loadByCommandName(scriptEntity.getCommandName()).isPresent()) {
                    skippedCommands.add(scriptEntity.getCommandName());
                } else {
                    scriptRepository.saveNewScript(scriptEntity);
                }
            }
            monitor.worked(1);

            if (!skippedCommands.isEmpty()) {
                reportSkippedCommandsOnUiThread(skippedCommands);
            }
            monitor.done();
        } catch (Exception e) {
            throw new RuntimeException("Template importing failed", e);
        }

        return Status.OK_STATUS;
    }

    private void reportSkippedCommandsOnUiThread(List<String> skippedCommands) {
        SkippedCommandUiJob skippedCommandUiJob = new SkippedCommandUiJob(skippedCommands);
        skippedCommandUiJob.schedule();
    }

}
