package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;
import java.util.function.Predicate;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

public class IsRegularBuilderCopyMethodEnabledPredicate implements Predicate<RegularBuilderUserPreference> {
    private final PluginLogger pluginLogger = new PluginLogger();

    @Override
    public boolean test(RegularBuilderUserPreference preference) {
        return preference.isGenerateCopyMethod() &&
                !builderFieldsContainConstructorField(preference.getBuilderFields());
    }

    private boolean builderFieldsContainConstructorField(List<BuilderField> builderFields) {
        boolean containsConstructorField = builderFields
                .stream()
                .filter(field -> field instanceof ConstructorParameterSetterBuilderField)
                .findAny()
                .isPresent();
        if (containsConstructorField) {
            pluginLogger.info("Generating copy method for the builder was selected, but builder contains unaccessable fields");
        }
        return containsConstructorField;
    }
}
