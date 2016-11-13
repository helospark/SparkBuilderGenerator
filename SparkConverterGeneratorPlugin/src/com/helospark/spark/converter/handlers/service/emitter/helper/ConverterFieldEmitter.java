package com.helospark.spark.converter.handlers.service.emitter.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class ConverterFieldEmitter {
    private ImportPopulator importPopulator;

    public ConverterFieldEmitter(ImportPopulator importPopulator) {
        this.importPopulator = importPopulator;
    }

    public void addFields(TypeDeclaration newType, CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest) {
        AST ast = compilationUnit.getAst();
        for (ConverterTypeCodeGenerationRequest dependency : generationRequest.getDependencies()) {
            VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
            variableDeclarationFragment.setName(ast.newSimpleName(dependency.getFieldName()));
            FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
            fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
            fieldDeclaration.setType(ast.newSimpleType(ast.newSimpleName(dependency.getClassName())));
            newType.bodyDeclarations().add(findLastFieldIndex(newType), fieldDeclaration);

            importPopulator.addImport(compilationUnit, dependency.getFullyQualifiedName());
        }
    }

    private int findLastFieldIndex(TypeDeclaration newType) {
        return ((List<BodyDeclaration>) newType.bodyDeclarations())
                .stream()
                .filter(element -> element instanceof FieldDeclaration)
                .collect(Collectors.toList())
                .size();
    }

}
