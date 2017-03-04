package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

public class Templater {
    private ScriptPreProcessor scriptPreProcessor;
    private ScriptInterpreter scriptInterpreter;

    public Templater(ScriptPreProcessor scriptPreProcessor, ScriptInterpreter scriptInterpreter) {
        this.scriptPreProcessor = scriptPreProcessor;
        this.scriptInterpreter = scriptInterpreter;
    }

    public void template(ExecutionEvent event, String program) {
        String preprocessedScript = scriptPreProcessor.preprocessScript(program);
        System.out.println("Preprocessed script: \n" + preprocessedScript);
        scriptInterpreter.interpret(event, preprocessedScript);
    }
}
