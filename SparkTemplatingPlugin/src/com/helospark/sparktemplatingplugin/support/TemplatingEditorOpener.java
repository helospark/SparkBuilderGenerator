package com.helospark.sparktemplatingplugin.support;

import java.net.URI;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.helospark.sparktemplatingplugin.Activator;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;

public class TemplatingEditorOpener {
    private ScriptRepository scriptRepository;

    public TemplatingEditorOpener(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }

    public void openEditorForCommand(String commandName) {
        try {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IWorkbenchPage page = window.getActivePage();

            URI uri = scriptRepository.getUriForCommand(commandName);
            IDE.openEditor(page, uri, Activator.EDITOR_ID, true);
        } catch (Exception e) {
            throw new RuntimeException("Unable to open editor", e);
        }
    }
}
