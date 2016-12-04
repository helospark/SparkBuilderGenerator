package com.helospark.sparktemplatingplugin.handlers.templater;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.handlers.templater.domain.ScriptExposedPair;

import bsh.Interpreter;

public class ScriptInterpreter {
    private TemplatingResultFactory templatingResultFactory;
    private List<ScriptExposed> scriptExposed;
    private List<ScriptExposedProvider> scriptExposedProviders;

    public ScriptInterpreter(TemplatingResultFactory templatingResultFactory, List<ScriptExposed> scriptExposed, List<ScriptExposedProvider> scriptExposedProviders) {
        this.scriptExposed = scriptExposed;
        this.templatingResultFactory = templatingResultFactory;
        this.scriptExposedProviders = scriptExposedProviders;
    }

    public void interpret(ExecutionEvent event, String program) {
        try {
            Interpreter bsh = new Interpreter();
            TemplatingResult templatingResult = templatingResultFactory.createTemplatingResult(event);
            bsh.set(templatingResult.getScriptName(), templatingResult);

            for (ScriptExposed scriptExposed : scriptExposed) {
                bsh.set(scriptExposed.getScriptName(), scriptExposed);
            }
            for (ScriptExposedProvider provider : scriptExposedProviders) {
                ScriptExposedPair scriptExposedPair = provider.provide(event);
                bsh.set(scriptExposedPair.getName(), scriptExposedPair.getExposedObject());
            }

            bsh.eval(program);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
