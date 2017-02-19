package com.helospark.sparktemplatingplugin.handlers;

import java.net.URI;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.ide.IDE;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.handlers.executor.ExecutorDialog;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class DialogHandler extends AbstractHandler {
    private ScriptRepository scriptRepository;

    public DialogHandler() {
        scriptRepository = DiContainer.getDependency(ScriptRepository.class);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            FilteredItemsSelectionDialog asd = new ExecutorDialog(shell, scriptRepository);
            asd.setInitialPattern("?");
            asd.open();
            ScriptEntity result = (ScriptEntity) asd.getFirstResult();

            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IWorkbenchPage page = window.getActivePage();

            URI uri = scriptRepository.getUriForCommand(result.getCommandName());
            IDE.openEditor(page, uri, "com.helospark.sparktemplatingplugin.editor.TemplatingToolEditor", true);

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
