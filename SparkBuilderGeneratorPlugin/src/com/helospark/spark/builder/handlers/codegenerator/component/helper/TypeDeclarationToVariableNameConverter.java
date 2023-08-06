package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Converts the given AbstractTypeDeclaration to a variable name using common Java camelCase convention.
 * @author helospark
 */
public class TypeDeclarationToVariableNameConverter {
    private CamelCaseConverter camelCaseConverter;

    public TypeDeclarationToVariableNameConverter(CamelCaseConverter camelCaseConverter) {
        this.camelCaseConverter = camelCaseConverter;
    }

    public String convert(AbstractTypeDeclaration builderType) {
        return camelCaseConverter.toLowerCamelCase(builderType.getName().toString());
    }

}
