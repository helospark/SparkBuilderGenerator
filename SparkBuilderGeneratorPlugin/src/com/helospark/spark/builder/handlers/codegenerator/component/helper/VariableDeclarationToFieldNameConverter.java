package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.REMOVE_PREFIX_AND_POSTFIX_FROM_BUILDER_NAMES;

import java.util.List;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.PrefixSuffixHolder;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class VariableDeclarationToFieldNameConverter {
    private PreferencesManager preferencesManager;
    private FieldPrefixPostfixProvider fieldPrefixPostfixProvider;
    private CamelCaseConverter camelCaseConverter;

    public VariableDeclarationToFieldNameConverter(PreferencesManager preferencesManager, FieldPrefixPostfixProvider fieldPrefixPostfixProvider,
            CamelCaseConverter camelCaseConverter) {
        this.preferencesManager = preferencesManager;
        this.fieldPrefixPostfixProvider = fieldPrefixPostfixProvider;
        this.camelCaseConverter = camelCaseConverter;
    }

    public String convertToFieldName(VariableDeclarationFragment fragment) {
        String rawFieldName = fragment.getName().toString();
        String result = rawFieldName;
        if (preferencesManager.getPreferenceValue(REMOVE_PREFIX_AND_POSTFIX_FROM_BUILDER_NAMES)) {
            PrefixSuffixHolder prefixSuffixHolder = fieldPrefixPostfixProvider.provideFieldPrefixAndSuffixPreference();
            result = removeMatchingPrefix(result, prefixSuffixHolder.getPrefixes());
            result = removeMatchingSuffix(result, prefixSuffixHolder.getSuffixes());
        }
        return result;
    }

    private String removeMatchingPrefix(String fieldName, List<String> prefixes) {
        return prefixes.stream()
                .filter(prefix -> fieldName.startsWith(prefix))
                .filter(prefix -> isCharacterAfterPrefixCapital(fieldName, prefix))
                .findFirst()
                .map(prefix -> removePrefix(fieldName, prefix))
                .orElse(fieldName);
    }

    private boolean isCharacterAfterPrefixCapital(String fieldName, String prefix) {
        if (fieldName.length() > prefix.length()) {
            return Character.isUpperCase(fieldName.charAt(prefix.length()));
        }
        return false;
    }

    private String removePrefix(String result, String prefix) {
        return camelCaseConverter.toLowerCamelCase(result.substring(prefix.length()));
    }

    private String removeMatchingSuffix(String fieldName, List<String> suffixes) {
        return suffixes.stream()
                .filter(suffix -> fieldName.endsWith(suffix))
                .filter(suffix -> !suffix.equals(fieldName))
                .findFirst()
                .map(suffix -> removeSuffix(fieldName, suffix))
                .orElse(fieldName);
    }

    private String removeSuffix(String result, String suffix) {
        return result.substring(0, result.length() - suffix.length());
    }
}
