package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;

public class WorkingCopyManagerWrapper {
    private HandlerUtilWrapper handlerUtilWrapper;

    public WorkingCopyManagerWrapper(HandlerUtilWrapper handlerUtilWrapper) {
        this.handlerUtilWrapper = handlerUtilWrapper;
    }

    public ICompilationUnit getCurrentCompilationUnit(ExecutionEvent event) {
        IEditorPart editor = handlerUtilWrapper.getActiveEditor(event);
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        return manager.getWorkingCopy(editor.getEditorInput());

    }
}
