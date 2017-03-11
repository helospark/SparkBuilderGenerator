package com.helospark.sparktemplatingplugin.create;

import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.TemplatingEditorOpener;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class TemplateCreateHandler extends AbstractHandler {
    private static final PluginLogger LOGGER = new PluginLogger(TemplateCreateHandler.class);
    private ScriptRepository scriptRepository;
    private TemplatingEditorOpener templatingEditorOpener;

    public TemplateCreateHandler() {
        this.scriptRepository = DiContainer.getDependency(ScriptRepository.class);
        this.templatingEditorOpener = DiContainer.getDependency(TemplatingEditorOpener.class);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            Optional<String> optionalCommandName = getCommandNameFromUser();
            if (optionalCommandName.isPresent()) {
                String commandName = optionalCommandName.get();
                scriptRepository.saveNewScript(new ScriptEntity(commandName, ""));
                templatingEditorOpener.openEditorForCommand(commandName);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to create template", e);
        }
        return null;
    }

    private Optional<String> getCommandNameFromUser() {
        Optional<String> result = Optional.empty();
        Optional<String> previousError = Optional.empty();
        do {
            previousError = Optional.empty();
            result = queryCommandNameFromUser(previousError);
            if (!result.isPresent()) {
                break;
            }
            if (scriptRepository.loadByCommandName(result.get()).isPresent()) {
                previousError = Optional.of("Command already exists with that name, please select a different");
            }
        } while (previousError.isPresent());
        return result;
    }

    private Optional<String> queryCommandNameFromUser(Optional<String> previousError) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        InputDialog inputDialog = new InputDialog(shell, "Enter command name", composeMessage(previousError), "", null);
        int result = inputDialog.open();
        if (result == InputDialog.OK) {
            return Optional.ofNullable(inputDialog.getValue());
        }
        return Optional.empty();
    }

    private String composeMessage(Optional<String> previousError) {
        String result = "Enter the command name! This is the string you use to call this command";
        if (previousError.isPresent()) {
            result += "\n" + previousError.get();
        }
        return result;
    }

}
