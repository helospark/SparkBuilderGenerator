package com.helospark.sparktemplatingplugin.handlers.templater.domain;

public class ScriptExposedPair {
    private String name;
    private Object exposedObject;

    public ScriptExposedPair(String name, Object exposedObject) {
        this.name = name;
        this.exposedObject = exposedObject;
    }

    public String getName() {
        return name;
    }

    public Object getExposedObject() {
        return exposedObject;
    }

}
