package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

public interface ScriptExposedProvider {

    public Object provide(ExecutionEvent executionEvent);

    public Class<?> getExposedObjectType();

    public String getExposedName();
}
