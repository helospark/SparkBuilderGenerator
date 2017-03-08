package com.helospark.sparktemplatingplugin.initializer.examplescript.domain;

import java.util.List;

import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class ExampleScriptDescriptorDomain {
    private String commandName;
    private List<ScriptEntity> revisions;

    public ExampleScriptDescriptorDomain(String commandName, List<ScriptEntity> revisions) {
        this.commandName = commandName;
        this.revisions = revisions;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<ScriptEntity> getRevisions() {
        return revisions;
    }

}
