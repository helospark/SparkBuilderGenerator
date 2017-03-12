package com.helospark.sparktemplatingplugin.ui.editor.completition.chain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.support.ImplicitImportList;
import com.helospark.sparktemplatingplugin.support.classpath.ClassInClasspathLocator;
import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable.CodeCleanerForLocalVariableFinder;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable.RegexMatchingLocalVariableExtractor;

public class LocalVariableCompletitionChain implements CompletitionChain {
    private CodeCleanerForLocalVariableFinder codeCleanerForLocalVariableFinder;
    private RegexMatchingLocalVariableExtractor regexMatchingLocalVariableExtractor;
    private ClassInClasspathLocator classInClasspathLocator;

    public LocalVariableCompletitionChain(CodeCleanerForLocalVariableFinder codeCleanerForLocalVariableFinder,
            RegexMatchingLocalVariableExtractor regexMatchingLocalVariableExtractor, ClassInClasspathLocator classInClasspathLocator) {
        this.codeCleanerForLocalVariableFinder = codeCleanerForLocalVariableFinder;
        this.regexMatchingLocalVariableExtractor = regexMatchingLocalVariableExtractor;
        this.classInClasspathLocator = classInClasspathLocator;
    }

    @Override
    public List<CompletitionProposalResponse> compute(CompletitionProposalRequest request) {
        if (!request.getClazz().isPresent()) {
            String cleanedSource = codeCleanerForLocalVariableFinder.cleanSource(request);
            return regexMatchingLocalVariableExtractor.matchLocalVariableWithRegex(cleanedSource)
                    .entrySet()
                    .stream()
                    .map(entry -> createResponse(entry))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private CompletitionProposalResponse createResponse(Map.Entry<String, String> entry) {
        List<Class<?>> possibleClasses = classInClasspathLocator.findClassesByName(ImplicitImportList.IMPLICIT_IMPORT_LIST, entry.getValue());
        Class<?> clazz = possibleClasses.stream()
                .findFirst()
                .orElse(Object.class);
        return createCompletitionProposalResponse(entry.getKey(), clazz);
    }

    private CompletitionProposalResponse createCompletitionProposalResponse(String key, Class<?> clazz) {
        return CompletitionProposalResponse.builder()
                .withAutocompleString(key)
                .withDescription("")
                .withDisplayName(key)
                .withType(clazz)
                .build();
    }

}
