package com.helospark.sparktemplatingplugin.handlers.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.handlers.templater.domain.ScriptExposedPair;

public interface ScriptExposedProvider {

    public ScriptExposedPair provide(ExecutionEvent executionEvent);
}
