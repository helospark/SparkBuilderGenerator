package com.helospark.spark.builder.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.helospark.spark.builder.Activator;

/**
 * Wrapper around Eclipse's dialog system.
 * 
 * @author helospark
 */
public class DialogWrapper {
    private CurrentShellProvider currentShellProvider;

    public DialogWrapper(CurrentShellProvider currentShellProvider) {
        this.currentShellProvider = currentShellProvider;
    }

    public void openInformationDialog(String title, String message) {
        MessageDialog.openInformation(getShell(), title, message);
    }

    public void openErrorDialogWithStatus(String title, String message, MultiStatus status) {
        ErrorDialog.openError(getShell(), title, message, status);
    }

    public void openErrorDialogWithStacktrace(String title, String message, Throwable exception) {
        openErrorDialogWithStatus(title, message, createMultiStatus(exception));
    }

    private Shell getShell() {
        return currentShellProvider.provideCurrentShell();
    }

    private static MultiStatus createMultiStatus(Throwable exception) {
        List<Status> childStatuses = new ArrayList<>();
        for (StackTraceElement stackTrace : exception.getStackTrace()) {
            childStatuses.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, stackTrace.toString()));
        }

        return new MultiStatus(Activator.PLUGIN_ID,
                IStatus.ERROR, childStatuses.toArray(new Status[] {}),
                exception.toString(), exception);
    }
}
