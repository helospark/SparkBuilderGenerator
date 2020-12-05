package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_LAST_STAGE_INTERFACE_NAME;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_STAGE_INTERFACE_NAME;
import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.Map;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Provides the interface name for the staged builder.
 * @author helospark
 */
public class StagedBuilderInterfaceNameProvider {
    private PreferencesManager preferencesManager;
    private CamelCaseConverter camelCaseConverter;
    private TemplateResolver templateResolver;

    public StagedBuilderInterfaceNameProvider(PreferencesManager preferencesManager, CamelCaseConverter camelCaseConverter, TemplateResolver templateResolver) {
        this.preferencesManager = preferencesManager;
        this.camelCaseConverter = camelCaseConverter;
        this.templateResolver = templateResolver;
    }

    public String provideInterfaceNameFrom(BuilderField builderField) {
        String stageBuilderInterfaceNameTemplate = preferencesManager.getPreferenceValue(STAGED_BUILDER_STAGE_INTERFACE_NAME);
        Map<String, String> templates = new HashMap<>();
        templates.put("fieldName", builderField.getBuilderFieldName());
        templates.put("FieldName", camelCaseConverter.toUpperCamelCase(builderField.getBuilderFieldName()));
        return templateResolver.resolveTemplate(stageBuilderInterfaceNameTemplate, templates);
    }

    public String provideBuildStageInterfaceName() {
        String stageBuilderInterfaceNameTemplate = preferencesManager.getPreferenceValue(STAGED_BUILDER_LAST_STAGE_INTERFACE_NAME);
        return templateResolver.resolveTemplate(stageBuilderInterfaceNameTemplate, emptyMap());
    }

}
