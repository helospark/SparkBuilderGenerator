package com.helospark.sparktemplatingplugin.ui.editor.completition.chain;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;

public class FieldCompletitionChainItem implements CompletitionChain {

    @Override
    public List<CompletitionProposalResponse> compute(CompletitionProposalRequest request) {
        if (request.getClazz().isPresent()) {
            return getFields(request.getClazz().get())
                    .stream()
                    .filter(field -> isFieldNameStartsWith(field, request.getExpression()))
                    .map(entry -> createResponseFor(entry))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // no static imported fields
        }

    }

    private boolean isFieldNameStartsWith(Field field, String expression) {
        return field.getName()
                .toUpperCase()
                .startsWith(expression.toUpperCase());
    }

    private CompletitionProposalResponse createResponseFor(Field entry) {
        return CompletitionProposalResponse.builder()
                .withAutocompleString(entry.getName())
                .withDescription("")
                .withDisplayName(entry.getName())
                .withType(entry.getType())
                .build();
    }

    private List<Field> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .collect(Collectors.toList());
    }

}
