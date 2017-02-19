package com.helospark.sparktemplatingplugin.handlers.templater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;

public class ScriptExposedObjectProvider {
    private TemplatingResultFactory templatingResultFactory;
    private List<ScriptExposed> scriptExposedObjects;
    private List<ScriptExposedProvider> scriptExposedProviders;

    public ScriptExposedObjectProvider(TemplatingResultFactory templatingResultFactory, List<ScriptExposed> scriptExposedObjects,
            List<ScriptExposedProvider> scriptExposedProviders) {
        this.templatingResultFactory = templatingResultFactory;
        this.scriptExposedObjects = scriptExposedObjects;
        this.scriptExposedProviders = scriptExposedProviders;
    }

    public Map<String, Class<?>> getExposedObjects() {
        Map<String, Class<?>> result = new HashMap<>();
        for (ScriptExposed scriptExposed : scriptExposedObjects) {
            result.put(scriptExposed.getExposedName(), scriptExposed.getClass());
        }
        for (ScriptExposedProvider provider : scriptExposedProviders) {
            result.put(provider.getExposedName(), provider.getExposedObjectType());
        }
        result.put(templatingResultFactory.getExposedName(), templatingResultFactory.getExposedObjectType());
        return result;
    }

    public Map<String, Object> providerExposedObjects(ExecutionEvent executionEvent) {
        Map<String, Object> result = new HashMap<>();
        for (ScriptExposed scriptExposed : scriptExposedObjects) {
            result.put(scriptExposed.getExposedName(), scriptExposed);
        }
        for (ScriptExposedProvider provider : scriptExposedProviders) {
            result.put(provider.getExposedName(), provider.provide(executionEvent));
        }
        result.put(templatingResultFactory.getExposedName(), templatingResultFactory.createTemplatingResult(executionEvent));
        return result;
    }
}
