package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import java.util.Optional;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Extracts the fully qualified name for the given declaration.
 * Implicit assumption of this class, that the AST was created with enabled binding.
 * Extracting fully qualified names require resolving of binding, which can possible be a costly operation.
 * @author helospark
 */
public class FullyQualifiedNameExtractor {
    private static final char GENERIC_PARAMETER_START_CHARACTER = '<';
    private PluginLogger pluginLogger;

    public FullyQualifiedNameExtractor() {
        pluginLogger = new PluginLogger();
    }

    public Optional<String> getFullyQualifiedBaseTypeName(BuilderField builderField) {
        return getFullyQualifiedParameterizedTypeName(builderField)
                .map(value -> deleteGenericTypeFromString(value));
    }

    private String deleteGenericTypeFromString(String value) {
        int startOfGenericType = value.indexOf(GENERIC_PARAMETER_START_CHARACTER);
        if (startOfGenericType == -1) {
            return value;
        } else {
            return value.substring(0, startOfGenericType);
        }
    }

    public Optional<String> getFullyQualifiedParameterizedTypeName(BuilderField builderField) {
        ITypeBinding resolvedBinding = builderField.getFieldType().resolveBinding();
        Optional<String> result = Optional.ofNullable(resolvedBinding)
                .map(value -> value.getQualifiedName());
        if (!result.isPresent()) {
            pluginLogger.warn("Cannot extract fully qualified name of field declaration '" + String.valueOf(builderField) + "'");
        }
        return result;
    }
}