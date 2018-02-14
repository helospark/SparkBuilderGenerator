package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationToVariableNameConverter {
    private CamelCaseConverter camelCaseConverter;

    public TypeDeclarationToVariableNameConverter(CamelCaseConverter camelCaseConverter) {
        this.camelCaseConverter = camelCaseConverter;
    }

    public String convert(TypeDeclaration builderType) {
        return camelCaseConverter.toLowerCamelCase(builderType.getName().toString());
    }

}
