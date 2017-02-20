package com.helospark.sparktemplatingplugin.delete;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.ui.dialog.TemplateBrowseDialog;

public class TemplateDeleteHandler extends AbstractHandler {
    private ScriptRepository scriptRepository;

    public TemplateDeleteHandler() {
        this.scriptRepository = DiContainer.getDependency(ScriptRepository.class);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        TemplateBrowseDialog templateBrowseDialog = new TemplateBrowseDialog(shell, scriptRepository, "Select an template to delete");
        templateBrowseDialog.setTitle("Delete template");
        templateBrowseDialog.open();
        ScriptEntity result = (ScriptEntity) templateBrowseDialog.getFirstResult();

        if (result != null) {
            boolean confirmed = MessageDialog.openConfirm(shell, "Confirm delete", "Are you sure you want to delete " + result.getCommandName() + "?");
            if (confirmed) {
                scriptRepository.deleteByCommandName(result.getCommandName());
            }
        }

        return null;
    }
}
