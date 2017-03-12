package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable;

import com.helospark.sparktemplatingplugin.execute.templater.ScriptPreProcessor;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;

public class CodeCleanerForLocalVariableFinder {
    private ScriptPreProcessor scriptPreProcessor;
    private NoSourceCleaningSourceProcessor noSourceCleaningSourceProcessor;
    private CurrentLineSubstringScriptPreprocessor currentLineSubstringScriptPreprocessor;
    private InnerBlockRemoverSourceProcessor innerBlockRemoverSourceProcessor;

    public CodeCleanerForLocalVariableFinder(ScriptPreProcessor scriptPreProcessor, NoSourceCleaningSourceProcessor noSourceCleaningSourceProcessor,
            CurrentLineSubstringScriptPreprocessor currentLineSubstringScriptPreprocessor, InnerBlockRemoverSourceProcessor innerBlockRemoverSourceProcessor) {
        this.scriptPreProcessor = scriptPreProcessor;
        this.noSourceCleaningSourceProcessor = noSourceCleaningSourceProcessor;
        this.currentLineSubstringScriptPreprocessor = currentLineSubstringScriptPreprocessor;
        this.innerBlockRemoverSourceProcessor = innerBlockRemoverSourceProcessor;
    }

    public String cleanSource(CompletitionProposalRequest request) {
        String source = request.getDocument().get();
        String preprocessedSource = scriptPreProcessor.preprocessScript(source);
        String cleanedSource = noSourceCleaningSourceProcessor.process(preprocessedSource);
        cleanedSource = currentLineSubstringScriptPreprocessor.process(source, request.getOffset());
        cleanedSource = innerBlockRemoverSourceProcessor.process(cleanedSource);
        return cleanedSource;
    }
}
