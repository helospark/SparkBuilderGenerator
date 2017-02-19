package com.helospark.sparktemplatingplugin.scriptexport;

import java.io.File;
import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptZipper;
import com.helospark.sparktemplatingplugin.scriptexport.job.ExportJob;

public class ExportTamplatesHandler extends AbstractHandler {
    private ScriptRepository scriptRepository;
    private ScriptZipper scriptZipper;

    public ExportTamplatesHandler() {
        this.scriptRepository = DiContainer.getDependency(ScriptRepository.class);
        this.scriptZipper = DiContainer.getDependency(ScriptZipper.class);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Optional<String> result = createSaveDialog();

        if (result.isPresent()) {
            String fileName = result.get();
            File file = new File(fileName);
            if (file.exists()) {
                throw new RuntimeException("File already exists " + file.getAbsolutePath());
            }

            ExportJob exportJob = new ExportJob(scriptRepository, scriptZipper, fileName);
            exportJob.schedule();
        }
        return null;
    }

    private Optional<String> createSaveDialog() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setFilterExtensions(new String[] { "*.zip" });
        String resultFileName = dialog.open();
        return Optional.ofNullable(resultFileName);
    }

    public ScriptRepository getScriptRepository() {
        return scriptRepository;
    }

}
