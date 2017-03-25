package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_LAST_STAGE_INTERFACE_NAME;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_STAGE_INTERFACE_NAME;
import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.Map;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class StagedBuilderInterfaceNameProvider {
    private PreferencesManager preferencesManager;
    private CamelCaseConverter camelCaseConverter;
    private TemplateResolver templateResolver;

    public StagedBuilderInterfaceNameProvider(PreferencesManager preferencesManager, CamelCaseConverter camelCaseConverter, TemplateResolver templateResolver) {
        this.preferencesManager = preferencesManager;
        this.camelCaseConverter = camelCaseConverter;
        this.templateResolver = templateResolver;
    }

    public String provideInterfaceNameFrom(NamedVariableDeclarationField namedVariableDeclarationField) {
        String stageBuilderInterfaceNameTemplate = preferencesManager.getPreferenceValue(STAGED_BUILDER_STAGE_INTERFACE_NAME);
        Map<String, String> templates = new HashMap<>();
        templates.put("fieldName", namedVariableDeclarationField.getOriginalFieldName());
        templates.put("FieldName", camelCaseConverter.toUpperCamelCase(namedVariableDeclarationField.getOriginalFieldName()));
        return templateResolver.resolveTemplate(stageBuilderInterfaceNameTemplate, templates);
    }

    public String provideBuildStageInterfaceName() {
        String stageBuilderInterfaceNameTemplate = preferencesManager.getPreferenceValue(STAGED_BUILDER_LAST_STAGE_INTERFACE_NAME);
        return templateResolver.resolveTemplate(stageBuilderInterfaceNameTemplate, emptyMap());
    }

}
