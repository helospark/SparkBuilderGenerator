package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.HashMap;
import java.util.Map;

import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates the builder classes's method names.
 * 
 * @author helospark
 */
public class BuilderMethodNameBuilder {
    private VariableNameToUpperCamelCaseConverter variableNameToUpperCamelCaseConverter;
    private PreferencesManager preferencesManager;
    private TemplateResolver templateResolver;

    public BuilderMethodNameBuilder(VariableNameToUpperCamelCaseConverter variableNameToUpperCamelCaseConverter, PreferencesManager preferencesManager,
            TemplateResolver templateResolver) {
        this.variableNameToUpperCamelCaseConverter = variableNameToUpperCamelCaseConverter;
        this.preferencesManager = preferencesManager;
        this.templateResolver = templateResolver;
    }

    public String build(String fieldName) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("fieldName", fieldName);
        replacements.put("FieldName", variableNameToUpperCamelCaseConverter.convert(fieldName));
        String name = preferencesManager.getPreferenceValue(PluginPreferenceList.BUILDERS_METHOD_NAME_PATTERN);
        return templateResolver.resolveTemplate(name, replacements);
    }
}
