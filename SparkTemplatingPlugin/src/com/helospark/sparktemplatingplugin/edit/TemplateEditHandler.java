package com.helospark.sparktemplatingplugin.edit;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.TemplatingEditorOpener;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.ui.dialog.TemplateBrowseDialog;

public class TemplateEditHandler extends AbstractHandler {
    private ScriptRepository scriptRepository;
    private TemplatingEditorOpener templatingEditorOpener;

    public TemplateEditHandler() {
        this.scriptRepository = DiContainer.getDependency(ScriptRepository.class);
        this.templatingEditorOpener = DiContainer.getDependency(TemplatingEditorOpener.class);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        TemplateBrowseDialog templateBrowseDialog = new TemplateBrowseDialog(shell, scriptRepository, "Select an template to edit");
        templateBrowseDialog.setTitle("Edit template");
        templateBrowseDialog.open();
        ScriptEntity result = (ScriptEntity) templateBrowseDialog.getFirstResult();

        if (result != null) {
            templatingEditorOpener.openEditorForCommand(result.getCommandName());
        }

        return null;
    }
}
