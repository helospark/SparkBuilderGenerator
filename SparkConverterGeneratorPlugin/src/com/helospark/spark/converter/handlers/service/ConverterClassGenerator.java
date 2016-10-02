package com.helospark.spark.converter.handlers.service;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ConverterClassGenerator {

    public TypeDeclaration createConverter(AST ast) {
        TypeDeclaration converter = ast.newTypeDeclaration();
        converter.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        converter.setName(ast.newSimpleName("CoolClass"));
        return converter;
    }

}
