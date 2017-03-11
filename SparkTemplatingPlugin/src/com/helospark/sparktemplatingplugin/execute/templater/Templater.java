package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class Templater {
    private static final PluginLogger LOGGER = new PluginLogger(Templater.class);
    private ScriptPreProcessor scriptPreProcessor;
    private ScriptInterpreter scriptInterpreter;

    public Templater(ScriptPreProcessor scriptPreProcessor, ScriptInterpreter scriptInterpreter) {
        this.scriptPreProcessor = scriptPreProcessor;
        this.scriptInterpreter = scriptInterpreter;
    }

    public void template(ExecutionEvent event, String program) {
        String preprocessedScript = scriptPreProcessor.preprocessScript(program);
        LOGGER.info("Preprocessed script: \n" + preprocessedScript);
        scriptInterpreter.interpret(event, preprocessedScript);
    }
}
