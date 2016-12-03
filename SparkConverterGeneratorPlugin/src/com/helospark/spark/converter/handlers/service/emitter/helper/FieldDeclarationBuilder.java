package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class FieldDeclarationBuilder {
    private ImportPopulator importPopulator;

    public FieldDeclarationBuilder(ImportPopulator importPopulator) {
        this.importPopulator = importPopulator;
    }

    public FieldDeclaration getFieldDeclaration(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest dependency) {
        String fieldName = dependency.getFieldName();
        String className = dependency.getClassName();
        String fullyQualifiedName = dependency.getFullyQualifiedName();

        return getFieldDeclaration(compilationUnit, fieldName, className, fullyQualifiedName);
    }

    public FieldDeclaration getFieldDeclaration(CompilationUnitModificationDomain compilationUnit, String fieldName, String className, String fullyQualifiedName) {
        AST ast = compilationUnit.getAst();
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName(fieldName));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName(className)));
        importPopulator.addImport(compilationUnit, fullyQualifiedName);
        return fieldDeclaration;
    }

}
