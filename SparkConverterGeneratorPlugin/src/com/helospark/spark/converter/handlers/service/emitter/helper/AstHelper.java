package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;

public class AstHelper {

    public Type convertType(AST ast, TemplatedIType inputType) {
        SimpleType baseType = ast.newSimpleType(ast.newName(inputType.getType().getElementName()));
        if (inputType.getTemplates().size() > 0) {
            ParameterizedType parameterizedType = ast.newParameterizedType(baseType);
            for (TemplatedIType templateType : inputType.getTemplates()) {
                parameterizedType.typeArguments().add(convertType(ast, templateType));
            }
            return parameterizedType;
        }
        return baseType;
    }
}
