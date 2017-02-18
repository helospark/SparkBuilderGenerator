package com.helospark.sparktemplatingplugin.editor;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class TemplatingToolCompletionProcessor implements IContentAssistProcessor {

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        try {
            IDocument document = viewer.getDocument();
            String typedWord = getTypedWord(document, offset);
            List<String> possibleCompletitions = calculateProposals();
            return possibleCompletitions.stream()
                    .filter(word -> word.startsWith(typedWord))
                    .map(word -> new CompletionProposal(word, offset - typedWord.length(), typedWord.length(), word.length()))
                    .collect(Collectors.toList())
                    .toArray(new ICompletionProposal[0]);
        } catch (Exception e) {
            System.out.println(e);
            return new ICompletionProposal[0];
        }
    }

    private List<String> calculateProposals() {
        return Arrays.stream(String.class.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(method -> method.getReturnType())
                .map(method -> method.getName())
                .collect(Collectors.toList());
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
        return new char[] { 'a', 'b' };
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return "ERROR";
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

}
