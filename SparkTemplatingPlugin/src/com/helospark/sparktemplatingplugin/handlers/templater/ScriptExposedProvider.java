package com.helospark.sparktemplatingplugin.handlers.templater;

import org.eclipse.core.commands.ExecutionEvent;

public interface ScriptExposedProvider {

    public Object provide(ExecutionEvent executionEvent);

    public Class<?> getExposedObjectType();

    public String getExposedName();
}
