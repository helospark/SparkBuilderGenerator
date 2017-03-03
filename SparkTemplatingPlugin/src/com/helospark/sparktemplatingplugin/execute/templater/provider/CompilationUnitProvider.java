package com.helospark.sparktemplatingplugin.execute.templater.provider;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;

public class CompilationUnitProvider implements ScriptExposedProvider {

    private static final String EXPOSED_NAME = "currentCompilationUnit";

    public SttCompilationUnit provideCurrentICompiltionUnit(ExecutionEvent event) {
        IEditorPart editor = getActiveEditor(event);
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        return new SttCompilationUnit(manager.getWorkingCopy(editor.getEditorInput()));
    }

    public IEditorPart getActiveEditor(ExecutionEvent event) {
        return HandlerUtil.getActiveEditor(event);
    }

    @Override
    public Object provide(ExecutionEvent executionEvent) {
        IEditorPart editor = getActiveEditor(executionEvent);
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        return new SttCompilationUnit(manager.getWorkingCopy(editor.getEditorInput()));
    }

    @Override
    public Class<?> getExposedObjectType() {
        return SttCompilationUnit.class;
    }

    @Override
    public String getExposedName() {
        return EXPOSED_NAME;
    }

}
