package com.helospark.spark.converter.handlers.service;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;

public class ConverterClassGenerator {

    public TypeDeclaration createConverter(CompilationUnitModificationDomain compilationUnitModificationDomain, String className) {
        AST ast = compilationUnitModificationDomain.getAst();
        TypeDeclaration converter = ast.newTypeDeclaration();
        converter.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        converter.setName(ast.newSimpleName(className));
        return converter;
    }

}
