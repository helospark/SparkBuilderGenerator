package com.helospark.sparktemplatingplugin.execute.templater.provider;

import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.impl.SttCompilationUnitImpl;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.NullSttCompilationUnit;

public class CompilationUnitProvider implements ScriptExposedProvider {

    public static final String EXPOSED_NAME = "currentCompilationUnit";

    public SttCompilationUnit provideCurrentICompiltionUnit(ExecutionEvent event) {
        return (SttCompilationUnit) provide(event);
    }

    public IEditorPart getActiveEditor(ExecutionEvent event) {
        return HandlerUtil.getActiveEditor(event);
    }

    @Override
    public Object provide(ExecutionEvent executionEvent) {
        Optional<IEditorPart> editor = Optional.ofNullable(getActiveEditor(executionEvent));
        return editor
                .map(editorPart -> getCompilationUnit(editorPart))
                .orElse(new NullSttCompilationUnit("No editor is currently open"));
    }

    private SttCompilationUnit getCompilationUnit(IEditorPart editor) {
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        return new SttCompilationUnitImpl(manager.getWorkingCopy(editor.getEditorInput()));
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
