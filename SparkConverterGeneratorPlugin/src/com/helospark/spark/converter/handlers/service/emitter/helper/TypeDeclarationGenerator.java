package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationGenerator {

    public TypeDeclaration createConverter(CompilationUnitModificationDomain compilationUnitModificationDomain, String className) {
        AST ast = compilationUnitModificationDomain.getAst();
        TypeDeclaration converter = ast.newTypeDeclaration();
        converter.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        converter.setName(ast.newSimpleName(className));
        return converter;
    }

}
