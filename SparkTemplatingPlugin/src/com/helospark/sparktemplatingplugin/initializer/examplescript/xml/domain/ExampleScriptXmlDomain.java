package com.helospark.sparktemplatingplugin.initializer.examplescript.xml.domain;

import java.util.List;

public class ExampleScriptXmlDomain {
    private String commandName;
    private List<String> revisionFileNames;

    public ExampleScriptXmlDomain(String commandName, List<String> revisionFileNames) {
        this.commandName = commandName;
        this.revisionFileNames = revisionFileNames;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getRevisionFileNames() {
        return revisionFileNames;
    }

}
