package com.helospark.spark.builder.handlers;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class CurrentShellProvider {

    public Shell provideCurrentShell() {
        return PlatformUI.getWorkbench().getDisplay().getActiveShell();
    }
}
