package com.helospark.sparktemplatingplugin.ui.editor.completition.chain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedObjectProvider;
import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;

public class ScriptExposedObjectCompletitionChainItem implements CompletitionChain {
    private ScriptExposedObjectProvider scriptExposedObjectProvider;

    public ScriptExposedObjectCompletitionChainItem(ScriptExposedObjectProvider scriptExposedObjectProvider) {
        this.scriptExposedObjectProvider = scriptExposedObjectProvider;
    }

    @Override
    public List<CompletitionProposalResponse> compute(CompletitionProposalRequest request) {
        if (!request.getClazz().isPresent()) {
            Map<String, Class<?>> map = scriptExposedObjectProvider.getExposedObjects();

            return map.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(request.getExpression()))
                    .map(entry -> createCompletitionProposalFor(entry))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private CompletitionProposalResponse createCompletitionProposalFor(Map.Entry<String, Class<?>> asd) {
        return CompletitionProposalResponse.builder()
                .withAutocompleString(asd.getKey())
                .withDescription("")
                .withDisplayName(asd.getKey())
                .withType(asd.getValue())
                .build();
    }
}
