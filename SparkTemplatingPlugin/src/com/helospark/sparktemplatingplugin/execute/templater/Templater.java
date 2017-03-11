package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;
import com.helospark.sparktemplatingplugin.ui.dialog.DialogUiHandler;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

import bsh.EvalError;
import bsh.TargetError;

public class Templater {
    private static final PluginLogger LOGGER = new PluginLogger(Templater.class);
    private ScriptPreProcessor scriptPreProcessor;
    private ScriptInterpreter scriptInterpreter;
    private DialogUiHandler dialogUiHandler;

    public Templater(ScriptPreProcessor scriptPreProcessor, ScriptInterpreter scriptInterpreter,
            DialogUiHandler dialogUiHandler) {
        this.scriptPreProcessor = scriptPreProcessor;
        this.scriptInterpreter = scriptInterpreter;
        this.dialogUiHandler = dialogUiHandler;
    }

    public void template(ExecutionEvent event, String program) {
        String preprocessedScript = scriptPreProcessor.preprocessScript(program);
        LOGGER.info("Preprocessed script: \n" + preprocessedScript);
        try {
            scriptInterpreter.interpret(event, preprocessedScript);
        } catch (TargetError e) {
            if (e.getTarget() instanceof MissingObjectException) {
                dialogUiHandler.openError("ERROR", e.getTarget().getMessage());
            } else {
                throw new RuntimeException("Target exception");
            }
        } catch (EvalError evalError) {
            throw new RuntimeException(evalError);
        }
    }
}
