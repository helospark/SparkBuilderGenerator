package com.helospark.sparktemplatingplugin.ui.editor.completition;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ProposalToDocumentationConverter {

    public String convertToDisplayName(Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.getName());
        stringBuilder.append("(");
        stringBuilder.append(Arrays.stream(method.getParameterTypes())
                .map(parameter -> parameter.getSimpleName())
                .collect(Collectors.joining(", ")));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
