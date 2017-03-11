package com.helospark.sparktemplatingplugin.ui.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class DialogUiHandler {

    public void openError(String title, String errorMessage) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        DialogUiJob uiJob = new DialogUiJob(() -> {
            MessageDialog.openError(shell, "ERROR", errorMessage);
            return 0;
        });
        uiJob.schedule();
    }
}
