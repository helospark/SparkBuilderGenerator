package com.helospark.sparktemplatingplugin.ui.editor;

import java.util.List;

import com.helospark.sparktemplatingplugin.IDocumented;

public class DocumentationProvider {
    private List<IDocumented> documented;

    public DocumentationProvider(List<IDocumented> documented) {
        this.documented = documented;
    }

    public String provideDocumentation(Class<?> forClass) {
        return documented.stream()
                .filter(d -> d.getClass().equals(forClass))
                .findFirst()
                .map(d -> d.getDocumentation())
                .orElse("<i>Not documented yet</i>");
    }
}
