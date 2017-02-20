package com.helospark.sparktemplatingplugin.execute.templater.domain;

import java.util.Objects;

public class ScriptExposedPair {
    private String name;
    private Object exposedObject;

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ScriptExposedPair)) {
            return false;
        }
        ScriptExposedPair castOther = (ScriptExposedPair) other;
        return Objects.equals(name, castOther.name) && Objects.equals(exposedObject, castOther.exposedObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, exposedObject);
    }

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
