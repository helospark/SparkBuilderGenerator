package com.helospark.sparktemplatingplugin.ui.dialog;

import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

public class DialogUiJob extends UIJob {
    private Supplier<Integer> dialogSupplier;
    private Integer dialogResult;

    public DialogUiJob(Supplier<Integer> dialogSupplier) {
        super(Display.getDefault(), "Opening dialog");
        this.dialogSupplier = dialogSupplier;
    }

    @Override
    public IStatus runInUIThread(IProgressMonitor arg0) {
        dialogResult = dialogSupplier.get();
        return Status.OK_STATUS;
    }

    public Integer getDialogResult() {
        return dialogResult;
    }
}
