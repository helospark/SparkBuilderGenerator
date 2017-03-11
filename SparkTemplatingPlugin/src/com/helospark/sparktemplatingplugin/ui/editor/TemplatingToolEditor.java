package com.helospark.sparktemplatingplugin.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.text.JavaColorManager;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.repository.CommandNameToFilenameMapper;
import com.helospark.sparktemplatingplugin.ui.editor.cache.EditorCacheInitializer;

public class TemplatingToolEditor extends TextEditor {
    private EditorCacheInitializer editorCacheInitializer;
    private IColorManager colorManager;
    boolean commandNameModified = false;
    private CommandNameToFilenameMapper commandNameToFilenameMapper;
    private String commandName = "Unknown";

    @SuppressWarnings("restriction")
    public TemplatingToolEditor() {
        colorManager = new JavaColorManager();
        editorCacheInitializer = DiContainer.getDependency(EditorCacheInitializer.class);
        commandNameToFilenameMapper = DiContainer.getDependency(CommandNameToFilenameMapper.class);
        setSourceViewerConfiguration(new TemplatingToolEditorConfiguration(colorManager));
        setDocumentProvider(new TemplatingToolDocumentProvider());
    }

    @Override
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        setDirty(false);
    }

    @Override
    public void doSaveAs() {
        doSave(getProgressMonitor());
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        editorCacheInitializer.initializeCaches();
        commandName = commandNameToFilenameMapper.mapToCommandName(getPartName());
        setPartName(commandName);
        firePropertyChange(IEditorPart.PROP_TITLE);
    }

    public void setDirty(boolean dirty) {
        if (commandNameModified != dirty) {
            commandNameModified = dirty;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || commandNameModified;
    }

    @Override
    public String getTitleToolTip() {
        return commandName + " - SparkTemplatingTool";
    }
}
