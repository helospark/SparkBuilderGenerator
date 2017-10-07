package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import java.util.Optional;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;

/**
 * Extracts the fully qualified name for the given declaration.
 * Implicit assumption of this class, that the AST was created with enabled binding.
 * Extracting fully qualified names require resolving of binding, which can possible be a costly operation.
 * @author helospark
 */
public class FullyQualifiedNameExtractor {

    public Optional<String> getFullyQualifiedBaseTypeName(FieldDeclaration fieldDeclaration) {
        return getFullyQualifiedParameterizedTypeName(fieldDeclaration)
                .map(value -> deleteGenericTypeFromString(value));
    }

    private String deleteGenericTypeFromString(String value) {
        int startOfGenericType = value.indexOf("<");
        if (startOfGenericType == -1) {
            return value;
        } else {
            return value.substring(0, startOfGenericType);
        }
    }

    public Optional<String> getFullyQualifiedParameterizedTypeName(FieldDeclaration fieldDeclaration) {
        ITypeBinding resolvedBinding = fieldDeclaration.getType().resolveBinding();
        return Optional.ofNullable(resolvedBinding)
                .map(value -> value.getQualifiedName());
    }
}