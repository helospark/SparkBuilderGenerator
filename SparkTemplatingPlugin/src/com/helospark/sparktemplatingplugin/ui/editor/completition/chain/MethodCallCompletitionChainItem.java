package com.helospark.sparktemplatingplugin.ui.editor.completition.chain;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.ProposalToDocumentationConverter;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;

public class MethodCallCompletitionChainItem implements CompletitionChain {
    private ProposalToDocumentationConverter proposalToDocumentationConverter;

    public MethodCallCompletitionChainItem(ProposalToDocumentationConverter proposalToDocumentationConverter) {
        this.proposalToDocumentationConverter = proposalToDocumentationConverter;
    }

    @Override
    public List<CompletitionProposalResponse> compute(CompletitionProposalRequest request) {
        String trimmedMethodCall = trimMethodCall(request.getExpression());
        if (request.getClazz().isPresent()) {
            return getMethods(request.getClazz().get())
                    .stream()
                    .filter(method -> method.getName().startsWith(trimmedMethodCall))
                    .map(entry -> createResponseFor(entry))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // no global methods
        }

    }

    private CompletitionProposalResponse createResponseFor(Method entry) {
        return CompletitionProposalResponse.builder()
                .withAutocompleString(entry.getName())
                .withDescription("")
                .withDisplayName(proposalToDocumentationConverter.convertToDisplayName(entry))
                .withType(entry.getReturnType())
                .build();
    }

    private List<Method> getMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toList());
    }

    public String trimMethodCall(String methodCall) {
        return methodCall.trim().replaceAll("\\(.*\\)", "");
    }
}
