package com.helospark.sparktemplatingplugin.scriptimport.job;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

public class SkippedCommandUiJob extends UIJob {
    private List<String> skippedCommands;

    public SkippedCommandUiJob(List<String> skippedCommands) {
        super("Showing skipped command imports");
        this.skippedCommands = skippedCommands;
    }

    @Override
    public IStatus runInUIThread(IProgressMonitor monitor) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        MessageDialog.openConfirm(shell, "Some commands were not imported", composeMessage());
        return Status.OK_STATUS;
    }

    private String composeMessage() {
        String result = "";
        result += "Due to name collision with existing commands, the following commands were not imported: ";
        skippedCommands.stream()
                .collect(Collectors.joining(", "));
        result += "\nIf you would like to import these, please rename or delete your currently existing"
                + "commands with the same name and rerun import";
        return result;
    }

}
