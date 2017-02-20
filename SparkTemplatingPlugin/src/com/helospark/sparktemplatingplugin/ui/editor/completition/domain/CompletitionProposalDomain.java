package com.helospark.sparktemplatingplugin.ui.editor.completition.domain;

public class CompletitionProposalDomain {
    private String autocompleString;
    private String displayName;
    private String description;

    public CompletitionProposalDomain(String autocompleString, String displayName, String description) {
        this.autocompleString = autocompleString;
        this.displayName = displayName;
        this.description = description;
    }

    public String getAutocompleString() {
        return autocompleString;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

}
