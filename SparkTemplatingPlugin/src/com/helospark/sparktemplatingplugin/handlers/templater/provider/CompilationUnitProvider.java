package com.helospark.sparktemplatingplugin.handlers.templater.provider;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.sparktemplatingplugin.handlers.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.handlers.templater.domain.ScriptExposedPair;

public class CompilationUnitProvider implements ScriptExposedProvider {

    public ICompilationUnit provideCurrentICompiltionUnit(ExecutionEvent event) {
        IEditorPart editor = getActiveEditor(event);
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        return manager.getWorkingCopy(editor.getEditorInput());
    }

    public IEditorPart getActiveEditor(ExecutionEvent event) {
        return HandlerUtil.getActiveEditor(event);
    }

    @Override
    public ScriptExposedPair provide(ExecutionEvent executionEvent) {
        return new ScriptExposedPair("currentCompilationUnit", provideCurrentICompiltionUnit(executionEvent));
    }

}
