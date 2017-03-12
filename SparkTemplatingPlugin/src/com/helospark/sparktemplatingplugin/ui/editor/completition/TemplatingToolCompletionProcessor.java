package com.helospark.sparktemplatingplugin.ui.editor.completition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;

public class TemplatingToolCompletionProcessor implements IContentAssistProcessor {
    private static final PluginLogger LOGGER = new PluginLogger(TemplatingToolCompletionProcessor.class);
    private final Set<Character> allowedExpressionCharacters = Arrays.asList('.', '(', ')', '\"', '\'')
            .stream()
            .collect(Collectors.toSet());
    private List<CompletitionChain> chain;

    public TemplatingToolCompletionProcessor(List<CompletitionChain> chain) {
        this.chain = chain;
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        try {
            IDocument document = viewer.getDocument();
            String typedWord = getTypedWord(document, offset);
            String fullExpression = getFullExpression(document, offset);
            List<CompletitionProposalResponse> possibleCompletitions = calculateProposals(document, offset,
                    fullExpression);
            return possibleCompletitions.stream()
                    .filter(word -> word.getAutocompleString().toUpperCase().startsWith(typedWord.toUpperCase()))
                    .map(word -> new CompletionProposal(word.getAutocompleString(), offset - typedWord.length(),
                            typedWord.length(), word.getAutocompleString().length(),
                            null, word.getDisplayName(), null,
                            word.getDescription()))
                    .collect(Collectors.toList())
                    .toArray(new ICompletionProposal[0]);
        } catch (Exception e) {
            LOGGER.error("Cannot create completition proposals", e);
            return new ICompletionProposal[0];
        }
    }

    private String getFullExpression(IDocument document, int offset) throws BadLocationException {
        int i = offset - 1;
        String result = "";
        while (i >= 0) {
            Character currentChar = document.getChar(i);
            if (!(Character.isJavaIdentifierPart(currentChar) || Character.isWhitespace(currentChar)
                    || allowedExpressionCharacters.contains(currentChar))) {
                return result;
            }
            result = currentChar + result;
            --i;
        }
        return result;
    }

    private List<CompletitionProposalResponse> calculateProposals(IDocument document, int offset,
            String fullExpression) {
        Class<?> clazz = null;
        String[] splitted = fullExpression.split("\\.", -1);
        for (int i = 0; i < splitted.length; ++i) {
            String currentElement = splitted[i].trim();
            CompletitionProposalRequest request = CompletitionProposalRequest.builder()
                    .withClazz(Optional.ofNullable(clazz))
                    .withDocument(document)
                    .withExpression(currentElement.trim())
                    .withOffset(offset)
                    .withCompletitionOffset(offset)
                    .build();
            List<CompletitionProposalResponse> response = chain.stream()
                    .flatMap(chain -> chain.compute(request).stream())
                    .collect(Collectors.toList());

            if (i == splitted.length - 1) {
                return response;
            } else {
                Optional<CompletitionProposalResponse> computedResponse = getChainItemResponse(currentElement,
                        response);
                if (!computedResponse.isPresent()) {
                    return Collections.emptyList();
                }
                clazz = computedResponse.get().getType();
                if (clazz == null) {
                    return Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }

    private Optional<CompletitionProposalResponse> getChainItemResponse(String currentElement,
            List<CompletitionProposalResponse> response) {
        List<CompletitionProposalResponse> filteredList = response.stream()
                .filter(res -> currentElement.replaceAll("\\(.*\\)", "").equals(res.getAutocompleString()))
                .collect(Collectors.toList());
        if (filteredList.isEmpty()) {
            return Optional.empty();
        } else if (filteredList.size() > 1) {
            LOGGER.warn("Multiple possible completitions, choosing first");
        }
        CompletitionProposalResponse computedResponse = filteredList.get(0);
        return Optional.ofNullable(computedResponse);
    }

    private String getTypedWord(IDocument document, int offset) throws BadLocationException {
        int i = offset - 1;
        String result = "";
        while (i >= 0 && Character.isJavaIdentifierPart(document.getChar(i))) {
            result = document.getChar(i) + result;
            --i;
        }
        return result;
    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return new char[] { '.' };
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return "Completition ERROR";
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

}
