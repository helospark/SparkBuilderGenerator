package com.helospark.sparktemplatingplugin.editor.completition;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.helospark.sparktemplatingplugin.editor.DocumentationProvider;
import com.helospark.sparktemplatingplugin.handlers.templater.ScriptExposedObjectProvider;

public class TemplatingToolCompletionProcessor implements IContentAssistProcessor {
    private ScriptExposedObjectProvider scriptExposedObjectProvider;
    private DocumentationProvider documentationProvider;
    private Set<Character> allowedExpressionCharacters = Arrays.asList('.', '(', ')', '\"', '\'').stream().collect(Collectors.toSet());

    public TemplatingToolCompletionProcessor(ScriptExposedObjectProvider scriptExposedObjectProvider, DocumentationProvider documentationProvider) {
        this.scriptExposedObjectProvider = scriptExposedObjectProvider;
        this.documentationProvider = documentationProvider;
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        try {
            IDocument document = viewer.getDocument();
            String typedWord = getTypedWord(document, offset);
            String fullExpression = getFullExpression(document, offset);
            List<String> possibleCompletitions = calculateProposals(fullExpression);
            return possibleCompletitions.stream()
                    .filter(word -> word.startsWith(typedWord))
                    .map(word -> new CompletionProposal(word, offset - typedWord.length(), typedWord.length(), word.length(), null, null, null,
                            "<h1>asd</h1><p>Super method, use this"))
                    .collect(Collectors.toList())
                    .toArray(new ICompletionProposal[0]);
        } catch (Exception e) {
            System.out.println(e);
            return new ICompletionProposal[0];
        }
    }

    private String getFullExpression(IDocument document, int offset) throws BadLocationException {
        int i = offset - 1;
        String result = "";
        while (i >= 0) {
            Character currentChar = document.getChar(i);
            if (!(Character.isJavaIdentifierPart(currentChar) || Character.isWhitespace(currentChar) || allowedExpressionCharacters.contains(currentChar))) {
                return result;
            }
            result = currentChar + result;
            --i;
        }
        return result;
    }

    private List<String> calculateProposals(String fullExpression) {
        if (!fullExpression.contains(".")) {
            return staticSuggestions(fullExpression);
        } else {
            return reflectionSuggestions(fullExpression);
        }
    }

    private List<String> reflectionSuggestions(String fullExpression) {
        Map<String, Class<?>> map = scriptExposedObjectProvider.getExposedObjects();
        String[] splitted = fullExpression.split("\\.", -1);
        Class<?> clazz = map.get(trimMethodCall(splitted[0]));
        int index = 1;
        while (index < splitted.length - 1 && clazz != null) {
            Map<String, Class<?>> reflectiveSubSuggestions = getReflectiveSuggestions(clazz);
            clazz = reflectiveSubSuggestions.get(trimMethodCall(splitted[index]));
            ++index;
        }
        if (clazz == null) {
            return Collections.emptyList();
        }
        return getReflectiveSuggestions(clazz)
                .entrySet()
                .stream()
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    public String trimMethodCall(String methodCall) {
        return methodCall.trim().replaceAll("\\(.*\\)", "");
    }

    private Map<String, Class<?>> getReflectiveSuggestions(Class<?> clazz) {
        Map<String, Class<?>> result = new HashMap<>();
        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toList());
        for (Method method : methods) {
            result.put(method.getName(), method.getReturnType());
        }
        return result;
    }

    private List<String> staticSuggestions(String fullExpression) {
        return scriptExposedObjectProvider.getExposedObjects()
                .entrySet()
                .stream()
                .map(o -> o.getKey())
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
