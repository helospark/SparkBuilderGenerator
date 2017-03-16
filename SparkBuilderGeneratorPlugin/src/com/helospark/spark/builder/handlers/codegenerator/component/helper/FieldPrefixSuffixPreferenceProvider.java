package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.PrefixSuffixHolder;

/**
 * Provides the prefix and suffix values set in Eclipse's setting.
 * Setting is located in Window->Preference->Code Style: Field (prefix, suffix)
 *
 * @author helospark
 */
public class FieldPrefixSuffixPreferenceProvider {
    private static final String PREFERENCE_VALUE_SEPARATOR = ",";
    private static final String FIELD_PREFIX_PROPERTY = "org.eclipse.jdt.core.codeComplete.fieldPrefixes";
    private static final String FIELD_SUFFIX_PROPERTY = "org.eclipse.jdt.core.codeComplete.fieldSuffixes";
    private PreferenceStoreProvider preferenceStoreProvider;

    public FieldPrefixSuffixPreferenceProvider(PreferenceStoreProvider preferenceStoreProvider) {
        this.preferenceStoreProvider = preferenceStoreProvider;
    }

    public PrefixSuffixHolder provideFieldPrefixAndSuffixPreference() {
        List<String> prefixes = Collections.emptyList();
        List<String> suffixes = Collections.emptyList();
        try {
            prefixes = getPreferenceList(FIELD_PREFIX_PROPERTY);
            suffixes = getPreferenceList(FIELD_SUFFIX_PROPERTY);
        } catch (Exception e) {
            System.out.println("[ERROR] while extracting prefix and suffix preferences");
            e.printStackTrace();
        }
        return PrefixSuffixHolder.builder()
                .withSuffixes(suffixes)
                .withPrefixes(prefixes)
                .build();
    }

    private List<String> getPreferenceList(String preferenceKey) {
        PreferenceStoreWrapper preferenceStore = preferenceStoreProvider.providerDefaultPreferenceStore();

        List<String> preferenceValueList = preferenceStore.getString(preferenceKey)
                .map(preference -> preference.split(PREFERENCE_VALUE_SEPARATOR))
                .map(preferenceArray -> Arrays.asList(preferenceArray))
                .orElse(Collections.emptyList());

        return preferenceValueList.stream()
                .map(preference -> preference.trim())
                .filter(preference -> !preference.isEmpty())
                .collect(Collectors.toList());
    }
}
