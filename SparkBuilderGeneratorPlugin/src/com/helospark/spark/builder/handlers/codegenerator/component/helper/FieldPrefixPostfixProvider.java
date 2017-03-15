package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.PrefixSuffixHolder;

public class FieldPrefixPostfixProvider {
    private static final String PREFERENCE_VALUE_SEPARATOR = ",";
    private static final String FIELD_PREFIX_PROPERTY = "org.eclipse.jdt.core.codeComplete.fieldPrefixes";
    private static final String FIELD_SUFFIX_PROPERTY = "org.eclipse.jdt.core.codeComplete.fieldSuffixes";
    private PreferenceStoreProvider preferenceStoreProvider;

    public FieldPrefixPostfixProvider(PreferenceStoreProvider preferenceStoreProvider) {
        this.preferenceStoreProvider = preferenceStoreProvider;
    }

    public PrefixSuffixHolder provideFieldPrefixAndSuffixPreference() {
        List<String> prefixes = Collections.emptyList();
        List<String> suffixes = Collections.emptyList();
        try {
            prefixes = getPreferenceList(FIELD_PREFIX_PROPERTY);
            suffixes = getPreferenceList(FIELD_SUFFIX_PROPERTY);
        } catch (Exception e) {

        }
        return PrefixSuffixHolder.builder()
                .withSuffixes(suffixes)
                .withPrefixes(prefixes)
                .build();
    }

    private List<String> getPreferenceList(String preferenceKey) {
        PreferenceStoreWrapper preferenceStore = preferenceStoreProvider.providerDefaultPreferenceStore();

        List<String> preferenceValueList = preferenceStore.getString(preferenceKey)
                .map(p -> p.split(PREFERENCE_VALUE_SEPARATOR))
                .map(p -> Arrays.asList(p))
                .orElse(Collections.emptyList());

        return preferenceValueList.stream()
                .map(p -> p.trim())
                .filter(p -> !p.isEmpty())
                .collect(Collectors.toList());
    }
}
